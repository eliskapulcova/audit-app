package com.example.auditapi.domain.model

data class SonarIssuesResponse(
    val total: Int,
    val p: Int,
    val ps: Int,
    val paging: Paging,
    val effortTotal: Int?,
    val issues: List<SonarIssue>,
    val components: List<SonarComponent>,
    val facets: List<Any>
)

