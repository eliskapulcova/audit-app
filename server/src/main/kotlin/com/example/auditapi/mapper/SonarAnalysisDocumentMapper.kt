package com.example.auditapi.mapper

import com.example.auditapi.domain.model.*
import org.springframework.stereotype.Component


@Component
class SonarAnalysisDocumentMapper {

    fun responseToDocument(
        serviceKey: String,
        projectKey: String,
        version: Long,
        issuesResponse: SonarIssuesResponse,
        measuresResponse: SonarMeasuresResponse,
        componentTreeResponse: SonarComponentTreeResponse,
        qualityGateResponse: SonarQualityGateResponse
    ): SonarAnalysisDocument =
        SonarAnalysisDocument(
            serviceKey = serviceKey,
            projectKey = projectKey,
            analysisVersion = version,
            totalIssues = issuesResponse.total,
            effortTotal = issuesResponse.effortTotal,
            issues = issuesResponse.issues,
            components = issuesResponse.components,
            measures = measuresResponse.component.measures,
            componentTreeBase = componentTreeResponse.baseComponent,
            componentTrees = componentTreeResponse.components,
            qualityGateStatus = qualityGateResponse.projectStatus,
        )

    fun mapTopIssues(doc: SonarAnalysisDocument?): List<IssueDto> =
        doc?.issues
            ?.asSequence()
            ?.filter { it.status == "OPEN" }
            ?.sortedBy { severityOrderIndex(it.severity.name) }
            ?.take(20)
            ?.map {
                IssueDto(
                    id = it.key,
                    severity = mapSeverity(it.severity.name),
                    tool = "SonarQube",
                    ruleId = it.rule,
                    description = it.message,
                    filePath = it.component.substringAfter(":"),
                    firstDetected = it.creationDate
                )
            }
            ?.toList()
            ?: emptyList()

    fun getMeasure(doc: SonarAnalysisDocument?, metric: String): String {
        return doc?.measures?.find { it.metric == metric }?.value ?: "0"
    }

    fun mapGrade(score: Int): String =
        when (score) {
            in 90..100 -> "A"
            in 80..89 -> "B"
            in 70..79 -> "C"
            in 60..69 -> "D"
            else -> "F"
        }

    fun buildProjectSummary(
        doc: SonarAnalysisDocument?,
        healthScore: HealthScoreDto?
    ): ProjectSummaryDto? =
        doc?.let { document ->
            healthScore?.let { score ->

                val issues = document.issues.orEmpty()
                val criticalSeverities = setOf("BLOCKER", "CRITICAL")

                val openIssues = issues.count { it.status == "OPEN" }

                val criticalIssues = issues.count {
                    it.status == "OPEN" && it.severity.name in criticalSeverities
                }

                val coverage = getMeasure(document, "coverage")
                    ?.toDoubleOrNull()
                    ?: 0.0

                ProjectSummaryDto(
                    id = document.projectKey,
                    name = document.projectKey,
                    description = "",
                    repositoryCount = 1,
                    healthScore = score.score,
                    healthStatus = mapHealthStatus(document),
                    grade = score.grade,
                    totalIssues = openIssues,
                    criticalIssues = criticalIssues,
                    coverage = coverage,
                    lastScan = document.id.orEmpty(), // FIXME: should be date of analysis, not id
                    trend = 0,
                    tools = listOf("SonarQube")
                )
            }
        }

    private val severityOrder = listOf("BLOCKER", "CRITICAL", "MAJOR", "MINOR", "INFO")

    private fun severityOrderIndex(severity: String): Int =
        severityOrder.indexOf(severity).takeIf { it >= 0 } ?: Int.MAX_VALUE


    private fun mapSeverity(severity: String) = when (severity) {
        "BLOCKER", "CRITICAL" -> "Critical"
        "MAJOR" -> "Medium"
        "MINOR" -> "Low"
        "INFO" -> "Info"
        else -> "Unknown"
    }

    private fun mapHealthStatus(doc: SonarAnalysisDocument): String {
        val status = doc.qualityGateStatus?.status ?: "ERROR"

        return when (status) {
            "OK" -> "healthy"
            else -> "critical"
        }
    }
}