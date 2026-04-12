package com.example.auditapi.controller

import SonarAnalysisDocument
import com.example.auditapi.model.SonarComponentTreeResponse
import com.example.auditapi.model.SonarIssuesResponse
import com.example.auditapi.model.SonarMeasuresResponse
import com.example.auditapi.model.SonarQualityGateResponse
import com.example.auditapi.service.SonarAnalysisService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sonar")
class SonarController(
    private val service: SonarAnalysisService
) {

    @PostMapping("/analyze")
    fun analyze(
        @RequestParam repositoryKey: String,
        @RequestParam serviceKey: String,
        @RequestParam projectKey: String
    ) {
        service.analyzeAndCreate(repositoryKey, serviceKey, projectKey, branch = "main") //main just for now
    }

    @PostMapping(
        "/create/analysis",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun createAnalysis(
        @RequestParam repositoryKey: String,
        @RequestParam serviceKey: String,
        @RequestParam projectKey: String,

        @RequestPart("issues") issues: SonarIssuesResponse,
        @RequestPart("measures") measures: SonarMeasuresResponse,
        @RequestPart("qualityGate") qualityGate: SonarQualityGateResponse,
        @RequestPart("componentTree") componentTree: SonarComponentTreeResponse
    ): SonarAnalysisDocument {

        return service.createAnalysis(
            repositoryKey,
            serviceKey,
            projectKey,
            issues,
            measures,
            componentTree,
            qualityGate
        )
    }
}
