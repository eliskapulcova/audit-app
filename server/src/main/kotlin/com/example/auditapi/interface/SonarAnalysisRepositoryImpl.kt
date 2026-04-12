package com.example.auditapi.`interface`

import SonarAnalysisDocument
import com.example.auditapi.model.*
import org.bson.Document
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.*
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

            Aggregation.addFields()
                .addField("coverage")
                .withValue(
                    ConditionalOperators.`when`(ArrayOperators.IsArray.isArray("measures"))
                        .then(
                            ArrayOperators.ArrayElemAt
                                .arrayOf(
                                    ArrayOperators.Filter
                                        .filter("measures")
                                        .`as`("m")
                                        .by(ComparisonOperators.Eq.valueOf("m.metric").equalToValue("coverage"))
                                )
                                .elementAt(0)
                        )
                        .otherwise(Document("metric", "unknown"))
                )
                .build(),

            // 5️⃣ Extract duplications metric (keep whole object)
            Aggregation.addFields()
                .addField("duplications")
                .withValue(
                    ConditionalOperators.`when`(ArrayOperators.IsArray.isArray("measures"))
                        .then(
                            ArrayOperators.ArrayElemAt
                                .arrayOf(
                                    ArrayOperators.Filter
                                        .filter("measures")
                                        .`as`("m")
                                        .by(ComparisonOperators.Eq.valueOf("m.metric").equalToValue("duplicated_lines_density"))
                                )
                                .elementAt(0)
                        )
                        .otherwise(Document("metric", "unknown"))
                )
                .build(),


        // Group everything in one stage
            Aggregation.facet()
                .and(
                    Aggregation.unwind("issues", true),
                    Aggregation.group()
                        // Issue counts
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.type").equalToValue("BUG")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("bugs")
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.type").equalToValue("VULNERABILITY")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("vulnerabilities")
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.type").equalToValue("CODE_SMELL")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("codeSmells")
                        // Severity breakdown
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.severity").equalToValue("BLOCKER")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("blocker")
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.severity").equalToValue("CRITICAL")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("critical")
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.severity").equalToValue("MAJOR")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("major")
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.severity").equalToValue("MINOR")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("minor")
                        .sum(
                            ConditionalOperators.`when`(
                                BooleanOperators.And.and(
                                    ComparisonOperators.Eq.valueOf("issues.status").equalToValue("OPEN"),
                                    ComparisonOperators.Eq.valueOf("issues.severity").equalToValue("INFO")
                                )
                            ).then(1).otherwise(0)
                        ).`as`("info")
                        // Technical debt sum
                        .sum("issues.debtMinutes").`as`("technicalDebt")
                        // Pull first of flattened fields
                        .first("coverage").`as`("coverage")
                        .first("duplications").`as`("duplications")
                        .first(SonarAnalysisDocument::qualityGateStatus.name).`as`("qualityGateStatus")
                ).`as`("metrics")
                .and(
                    Aggregation.unwind("issues", true),
                    Aggregation.match(Criteria.where("issues.status").`is`("OPEN")),
                    Aggregation.group("issues.component").count().`as`("violations"),
                    Aggregation.sort(Sort.Direction.DESC, "violations"),
                    Aggregation.limit(5),
                    Aggregation.project("violations").and("_id").`as`("file")
                ).`as`("topFiles"),

            // Project to flatten the structure back to SonarAggregationResult
            Aggregation.project()
                .and("metrics.bugs").arrayElementAt(0).`as`("bugs")
                .and("metrics.vulnerabilities").arrayElementAt(0).`as`("vulnerabilities")
                .and("metrics.codeSmells").arrayElementAt(0).`as`("codeSmells")
                .and("metrics.blocker").arrayElementAt(0).`as`("blocker")
                .and("metrics.critical").arrayElementAt(0).`as`("critical")
                .and("metrics.major").arrayElementAt(0).`as`("major")
                .and("metrics.minor").arrayElementAt(0).`as`("minor")
                .and("metrics.info").arrayElementAt(0).`as`("info")
                .and("metrics.technicalDebt").arrayElementAt(0).`as`("technicalDebt")
                .and("metrics.coverage").arrayElementAt(0).`as`("coverage")
                .and("metrics.duplications").arrayElementAt(0).`as`("duplications")
                .and("metrics.qualityGateStatus").arrayElementAt(0).`as`("qualityGateStatus")
                .and("topFiles").`as`("topFiles")
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
            trendData = emptyList(), // TODO: implement using historical data
            topFiles = r.topFiles.map { FileSummaryDto(it.file, it.violations) },
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


