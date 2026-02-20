package com.example.auditapi.domain.model

data class ProjectDetailsResponse(
    val healthScore: HealthScoreDto? = null,
    val kpis: List<KPIDto>? = null,
    val sonarQubeData: SonarQubeDataDto?,
    val semgrepData: Any? = null,
    val phpcsData: Any? = null,
    val phpstanData: Any? = null,
    val trendData: List<TrendDataPointDto> = emptyList(),
    val topIssues: List<IssueDto> = emptyList(),
    val repositoryHealth: List<RepositoryHealthDto> = emptyList(),
    val projectSummary: ProjectSummaryDto? = null
)

data class HealthScoreDto(
    val score: Int,
    val grade: String,   // A-F
    val trend: Int,
    val previousScore: Int
)

data class KPIDto(
    val id: String,
    val label: String,
    val value: Any,
    val trend: Int,
    val isTrendPositive: Boolean,
    val sparklineData: List<Int>
)

data class SonarQubeDataDto(
    val healthStatus: String, // healthy | warning | critical
    val bugs: Int,
    val vulnerabilities: Int,
    val codeSmells: Int,
    val coverage: Double,
    val technicalDebt: Int, // minutes
    val duplications: Double,
    val qualityGate: String, // Passed | Failed
    val reliability: String, // A-E
    val security: String,
    val maintainability: String,
    val severityBreakdown: SeverityBreakdownDto,
    val trendData: List<Int>,
    val topFiles: List<FileSummaryDto>,
    val lastRun: String
)

data class IssueDto(
    val id: String,
    val severity: String, // "Critical" | "High" | "Medium" | "Low"
    val tool: String,     // "SonarQube"
    val ruleId: String,
    val ruleDocUrl: String,
    val description: String,
    val filePath: String,
    val line: Int?,
    val firstDetected: String
)

data class TrendDataPointDto(
    val date: String,
    val tools: Map<String, Int>
)

/*TrendDataPointDto(
date = document.analysisDate,
tools = mapOf(
"SonarQube" to document.totalIssues
)
)*/

data class RepositoryHealthDto(
    val repository: String,
    val healthStatuses: Map<String, String> // tool name -> HealthStatus
)

data class SeverityBreakdownDto(
    val blocker: Int,
    val critical: Int,
    val major: Int,
    val minor: Int,
    val info: Int
)

data class FileSummaryDto(
    val file: String,
    val violations: Int,
)

data class ProjectSummaryDto(
    val id: String,
    val name: String,
    val description: String,
    val repositoryCount: Int,
    val healthScore: Int,
    val healthStatus: String,
    val grade: String,
    val totalIssues: Int,
    val criticalIssues: Int,
    val coverage: Double,
    val lastScan: String,
    val trend: Int,
    val tools: List<String>
)
