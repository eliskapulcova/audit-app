package com.example.auditapi.controller

import com.example.auditapi.domain.model.*
import com.example.auditapi.service.SonarAnalysisService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sonar")
class SonarController(
    private val service: SonarAnalysisService
) {

    @PostMapping("/analyze")
    fun analyze(
        @RequestParam serviceKey: String,
        @RequestParam projectKey: String
    ) {
        service.analyzeAndCreate(serviceKey, projectKey, branch = "main") //main just for now
    }

    @PostMapping(
        "/create/analysis",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun createAnalysis(
        @RequestParam serviceKey: String,
        @RequestParam projectKey: String,

        @RequestPart("issues") issues: SonarIssuesResponse,
        @RequestPart("measures") measures: SonarMeasuresResponse,
        @RequestPart("qualityGate") qualityGate: SonarQualityGateResponse,
        @RequestPart("componentTree") componentTree: SonarComponentTreeResponse
    ): SonarAnalysisDocument {

        return service.createAnalysis(
            serviceKey,
            projectKey,
            issues,
            measures,
            componentTree,
            qualityGate
        )
    }

}
