package com.example.auditapi.service

import com.example.auditapi.domain.model.*

interface SonarAnalysisService {

    fun analyzeAndCreate(
        repositoryKey: String,
        serviceKey: String,
        projectKey: String,
        branch: String,
    ): SonarAnalysisDocument

    fun createAnalysis(
        repositoryKey: String,
        serviceKey: String,
        projectKey: String,
        sonarIssueResponse: SonarIssuesResponse,
        sonarMeasureResponse: SonarMeasuresResponse,
        sonarComponentTreeResponse: SonarComponentTreeResponse,
        sonarQualityGateResponse: SonarQualityGateResponse
    ): SonarAnalysisDocument

    fun getLatestAnalysis(projectKey: String): SonarAnalysisDocument?

    fun getAnalysisHistory(serviceKey: String): List<SonarAnalysisDocument>

    fun getAnalysisByVersion(
        serviceKey: String,
        version: Long
    ): SonarAnalysisDocument?
}
