package com.example.auditapi.domain.model

data class SonarQualityGateResponse(
    val projectStatus: ProjectStatus
)

data class ProjectStatus(
    val status: String,
    val conditions: List<Condition>,
    val ignoredConditions: Boolean,
    val period: Period?,
    val caycStatus: String?
)

data class Condition(
    val status: String,
    val metricKey: String,
    val comparator: String,
    val errorThreshold: String?,
    val actualValue: String?
)

data class Period(
    val mode: String,
    val date: String
)

