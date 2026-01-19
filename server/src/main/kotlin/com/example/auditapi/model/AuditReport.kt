package com.example.auditapi.model

import com.fasterxml.jackson.databind.JsonNode

data class AuditReport(
    val issueReport: IssueReport,
    val analysisReport: AnalysisReport
)
