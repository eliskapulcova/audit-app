package com.example.auditapi.service

import com.example.auditapi.domain.model.HealthScoreDto
import com.example.auditapi.domain.model.KPIDto
import com.example.auditapi.domain.model.ProjectDetailsResponse
import com.example.auditapi.domain.model.SonarAnalysisDocument

interface ProjectDetailsService {

    fun getProjectDetails(projectKey: String): ProjectDetailsResponse

    fun buildKpis(doc: SonarAnalysisDocument?): List<KPIDto>?

    fun calculateHealthScore(doc: SonarAnalysisDocument?): HealthScoreDto?
}