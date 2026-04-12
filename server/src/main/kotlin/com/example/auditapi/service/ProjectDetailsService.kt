package com.example.auditapi.service

import SonarAnalysisDocument
import com.example.auditapi.model.HealthScoreDto
import com.example.auditapi.model.KPIDto
import com.example.auditapi.model.ProjectDetailsResponse


interface ProjectDetailsService {

    fun getProjectDetails(projectKey: String): ProjectDetailsResponse

    fun buildKpis(doc: SonarAnalysisDocument?): List<KPIDto>?

    fun calculateHealthScore(doc: SonarAnalysisDocument?): HealthScoreDto?
}
