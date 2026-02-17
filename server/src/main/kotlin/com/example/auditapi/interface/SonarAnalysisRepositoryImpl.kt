package com.example.auditapi.`interface`

import com.example.auditapi.domain.model.*
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.*
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class SonarAnalysisRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : SonarAnalysisRepository {

    override fun save(analysis: SonarAnalysisDocument): SonarAnalysisDocument {
        return mongoTemplate.save(analysis)
    }

    override fun findLatestByProjectKey(projectKey: String): SonarAnalysisDocument? {
        return mongoTemplate.findOne(
            Query(
                SonarAnalysisDocument::projectKey isEqualTo projectKey
            ).with(
                Sort.by(
                    Sort.Direction.DESC,
                    SonarAnalysisDocument::analysisVersion.name
                )
            ).limit(1),
            SonarAnalysisDocument::class.java
        )
    }

    override fun aggregateSonarData(projectKey: String): SonarQubeDataDto? {

        // Match latest document for the project
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where(SonarAnalysisDocument::projectKey.name).`is`(projectKey)
            ),
            Aggregation.sort(Sort.Direction.DESC, SonarAnalysisDocument::analysisVersion.name),
            Aggregation.limit(1),

            // Flatten coverage
            Aggregation.addFields()
                .addField("coverage")
                .withValue(
                    ArrayOperators.ArrayElemAt
                        .arrayOf(
                            ArrayOperators.Filter
                                .filter(SonarAnalysisDocument::measures.name)
                                .`as`("m")
                                .by(ComparisonOperators.Eq.valueOf("m.metric").equalToValue("coverage"))
                        )
                        .elementAt(0)
                )
                .build(),

            // 5️⃣ Extract duplications metric (keep whole object)
            Aggregation.addFields()
                .addField("duplications")
                .withValue(
                    ArrayOperators.ArrayElemAt
                        .arrayOf(
                            ArrayOperators.Filter
                                .filter(SonarAnalysisDocument::measures.name)
                                .`as`("m")
                                .by(ComparisonOperators.Eq.valueOf("m.metric").equalToValue("duplicated_lines_density"))
                        )
                        .elementAt(0)
                )
                .build(),


        // Group everything in one stage
            Aggregation.group()
                // Issue counts
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.type").`is`("BUG")).then(1).otherwise(0)).`as`("bugs")
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.type").`is`("VULNERABILITY")).then(1).otherwise(0)).`as`("vulnerabilities")
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.type").`is`("CODE_SMELL")).then(1).otherwise(0)).`as`("codeSmells")
                // Severity breakdown
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.severity").`is`("BLOCKER")).then(1).otherwise(0)).`as`("blocker")
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.severity").`is`("CRITICAL")).then(1).otherwise(0)).`as`("critical")
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.severity").`is`("MAJOR")).then(1).otherwise(0)).`as`("major")
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.severity").`is`("MINOR")).then(1).otherwise(0)).`as`("minor")
                .sum(ConditionalOperators.`when`(Criteria.where("issues.status").`is`("OPEN")
                    .and("issues.severity").`is`("INFO")).then(1).otherwise(0)).`as`("info")
                // Technical debt sum
                .sum("issues.debtMinutes").`as`("technicalDebt")
                // Pull first of flattened fields
                .first("coverage").`as`("coverage")
                .first("duplications").`as`("duplications")
                .first(SonarAnalysisDocument::qualityGateStatus.name).`as`("qualityGate")
        )

        return mongoTemplate.aggregate(
            aggregation,
            mongoTemplate.getCollectionName(SonarAnalysisDocument::class.java),
            SonarAggregationResult::class.java
        ).uniqueMappedResult
            ?.let(::mapToSonarDto)
            ?: throw IllegalStateException("No Sonar analysis found for service: $projectKey")

    }

    private fun mapToSonarDto(r: SonarAggregationResult): SonarQubeDataDto {

        return SonarQubeDataDto(
            healthStatus = mapHealthStatus(r.qualityGateStatus),
            bugs = r.bugs,
            vulnerabilities = r.vulnerabilities,
            codeSmells = r.codeSmells,
            coverage = r.coverage?.value?.toDouble() ?: 0.0,
            technicalDebt = r.technicalDebt,
            duplications = r.duplications?.value?.toDouble() ?: 0.0,
            qualityGate = if (r.qualityGateStatus?.status == "OK") "Passed" else "Failed",
            reliability = calculateLetter(r.bugs),
            security = calculateLetter(r.vulnerabilities),
            maintainability = calculateLetter(r.codeSmells),
            severityBreakdown = SeverityBreakdownDto(
                blocker = r.blocker,
                critical = r.critical,
                major = r.major,
                minor = r.minor,
                info = r.info
            ),
            lastRun = Instant.now().toString()
        )
    }

    private fun calculateLetter(count: Int): String =
        when {
            count == 0 -> "A"
            count <= 2 -> "B"
            count <= 5 -> "C"
            count <= 10 -> "D"
            else -> "E"
        }

    private fun mapHealthStatus(doc: ProjectStatus?): String {
        val status = doc?.status

        return when (status) {
            "OK" -> "healthy"
            else -> "critical"
        }
    }

    override fun findByService(serviceKey: String): List<SonarAnalysisDocument> {
        return mongoTemplate.find(
            Query(
                SonarAnalysisDocument::serviceKey isEqualTo serviceKey
            ).with(
                Sort.by(
                    Sort.Direction.DESC,
                    SonarAnalysisDocument::analysisVersion.name
                )
            ),
            SonarAnalysisDocument::class.java
        )
    }

    override fun findByServiceAndVersion(
        serviceKey: String,
        version: Long
    ): SonarAnalysisDocument? {
        val query = Query(
            Criteria().andOperator(
                SonarAnalysisDocument::serviceKey isEqualTo serviceKey,
                SonarAnalysisDocument::analysisVersion isEqualTo version
            )
        )
        return mongoTemplate.findOne(query, SonarAnalysisDocument::class.java)
    }

    override fun getNextVersion(projectKey: String): Long {
        val latest = findLatestByProjectKey(projectKey)
        return (latest?.analysisVersion ?: 0L) + 1
    }
}


