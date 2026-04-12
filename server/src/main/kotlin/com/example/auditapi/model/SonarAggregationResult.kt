package com.example.auditapi.model

data class SonarAggregationResult(
    val bugs: Int = 0,
    val vulnerabilities: Int = 0,
    val codeSmells: Int = 0,

    val blocker: Int = 0,
    val critical: Int = 0,
    val major: Int = 0,
    val minor: Int = 0,
    val info: Int = 0,

    val technicalDebt: Int = 0,

    val topFiles: List<FileSummary> = emptyList(),

    val coverage: SonarMeasure? = null,
    val duplications: SonarMeasure? = null,

    val qualityGateStatus: ProjectStatus? = null
)

data class FileSummary(
    val file: String,
    val violations: Int,
)
