package com.example.auditapi.domain.model

data class SonarMeasuresResponse(
    val component: SonarComponentMeasures
)

data class SonarComponentMeasures(
    val measures: List<SonarMeasure>
)

data class SonarMeasure(
    val metric: String,
    val value: String? = null,
    val bestValue: Boolean? = null
)
