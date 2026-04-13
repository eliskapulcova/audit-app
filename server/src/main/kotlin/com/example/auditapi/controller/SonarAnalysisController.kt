package com.example.auditapi.controller

import com.example.auditapi.model.SonarAnalysisDocument
import com.example.auditapi.service.SonarAnalysisService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/services/{serviceKey}/analyses")
class SonarAnalysisController(
    private val sonarAnalysisService: SonarAnalysisService
) {

    @GetMapping("/latest")
    fun getLatest(@PathVariable serviceKey: String): ResponseEntity<Any> {
        val analysis = sonarAnalysisService.getLatestAnalysis(serviceKey)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(analysis)
    }

    @GetMapping
    fun getHistory(@PathVariable serviceKey: String): List<SonarAnalysisDocument> {
        return sonarAnalysisService.getAnalysisHistory(serviceKey)
    }

    @GetMapping("/{version}")
    fun getByVersion(
        @PathVariable serviceKey: String,
        @PathVariable version: Long
    ): ResponseEntity<Any> {
        val analysis = sonarAnalysisService.getAnalysisByVersion(serviceKey, version)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(analysis)
    }
}

