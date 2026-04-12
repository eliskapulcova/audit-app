package com.example.auditapi.controller

import com.example.auditapi.model.ProjectDetailsResponse
import com.example.auditapi.service.ProjectDetailsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/details")
class AggregatedAnalysisController(
    private val projectDetailsService: ProjectDetailsService
) {
    @GetMapping
    fun getProjectDetail(@RequestParam projectKey: String): ProjectDetailsResponse {
        return projectDetailsService.getProjectDetails(projectKey)
    }
}
