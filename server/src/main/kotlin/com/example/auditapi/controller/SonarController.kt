package com.example.auditapi.controller

import com.example.auditapi.service.SonarAnalysisService
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
}
