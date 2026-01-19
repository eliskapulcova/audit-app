package com.example.auditapi.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class AnalysisReportService(
    private val objectMapper: ObjectMapper
) {

    fun loadAnalysisReport(): JsonNode {
        val resource = ClassPathResource("age-verification-analysis-report.json")
        return objectMapper.readTree(resource.inputStream)
    }
}
