package com.example.auditapi.service

import com.example.auditapi.model.SonarAnalysisDocument
import com.example.auditapi.model.HealthScoreDto
import com.example.auditapi.model.KPIDto
import com.example.auditapi.model.ProjectDetailsResponse
import com.example.auditapi.`interface`.SonarAnalysisRepository
import com.example.auditapi.mapper.SonarAnalysisDocumentMapper
import org.springframework.stereotype.Service

@Service
class ProjectDetailsServiceImpl(
    private val sonarRepository: SonarAnalysisRepository,
    private val sonarMapper: SonarAnalysisDocumentMapper
): ProjectDetailsService {

   override fun getProjectDetails(projectKey: String): ProjectDetailsResponse {

        val sonarData = sonarRepository.aggregateSonarData(projectKey)
            ?: throw IllegalArgumentException("Project not found")

        val lastDocument = sonarRepository.findLatestByProjectKey(projectKey)

        val healthScore = calculateHealthScore(lastDocument)

        return ProjectDetailsResponse(
            healthScore = healthScore,
            kpis = buildKpis(lastDocument),
            sonarQubeData = sonarData,
            semgrepData = null,
            phpcsData = null,
            phpstanData = null,
            trendData = emptyList(),
            topIssues = sonarMapper.mapTopIssues(lastDocument),
            repositoryHealth = emptyList(),
            // FIXME: project summary should not be responsibility of Sonar mapper, it should take into account all tools
            projectSummary = sonarMapper.buildProjectSummary(lastDocument,healthScore)
        )
    }

   override fun buildKpis(doc: SonarAnalysisDocument?): List<KPIDto>? {
        // TODO: implement trend calculation - we need historical data for this
        val openIssues = doc?.issues?.count { it.status == "OPEN" }
       val coverage = if (doc == null) {
           0.0
       } else {
           sonarMapper.getMeasure(doc, "coverage").toDoubleOrNull() ?: 0.0
       }

        return listOf(
            KPIDto(
                id = "open-issues",
                label = "Open Issues",
                value = openIssues ?: 0,
                trend = 0,
                isTrendPositive = false,
                sparklineData = emptyList()
            ),
            KPIDto(
                id = "coverage",
                label = "Coverage",
                value = coverage,
                trend = 0,
                isTrendPositive = false,
                sparklineData = emptyList()
            )
        )
    }

    override fun calculateHealthScore(doc: SonarAnalysisDocument?): HealthScoreDto? =
        doc?.run {
            val openIssues = issues.count { it.status == "OPEN" }
            val coverage = sonarMapper.getMeasure(this, "coverage").toDoubleOrNull() ?: 0.0

            val score = (
                    100
                        .minus(openIssues * 2)
                        .plus((coverage / 10).toInt())
                    ).coerceIn(0, 100)

            HealthScoreDto(
                score = score,
                grade = sonarMapper.mapGrade(score),
                trend = 0,
                previousScore = score
            )
        }

}
