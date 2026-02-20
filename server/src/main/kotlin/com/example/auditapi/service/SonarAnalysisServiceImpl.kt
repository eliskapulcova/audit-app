package com.example.auditapi.service

import com.example.auditapi.client.SonarClient
import com.example.auditapi.domain.model.*
import com.example.auditapi.`interface`.SonarAnalysisRepository
import com.example.auditapi.mapper.SonarAnalysisDocumentMapper
import org.springframework.stereotype.Service

@Service
class SonarAnalysisServiceImpl(
    private val repository: SonarAnalysisRepository,
    private val sonarClient: SonarClient,
    private val mapper: SonarAnalysisDocumentMapper
) : SonarAnalysisService {

    override fun analyzeAndCreate(
        repositoryKey: String,
        serviceKey: String,
        projectKey: String,
        branch: String
    ): SonarAnalysisDocument {

        val sonarIssueResponse = sonarClient.fetchIssues(projectKey, branch)
        val sonarMeasureResponse = sonarClient.fetchMeasures(projectKey, branch)
        val sonarComponentTreeResponse = sonarClient.fetchComponentTree(projectKey, branch)
        val sonarQualityGateResponse = sonarClient.fetchQualityGate(projectKey)

        return createAnalysis(repositoryKey, serviceKey, projectKey, sonarIssueResponse, sonarMeasureResponse, sonarComponentTreeResponse, sonarQualityGateResponse)
    }

    override fun createAnalysis(
        repositoryKey: String,
        serviceKey: String,
        projectKey: String,
        sonarIssueResponse: SonarIssuesResponse,
        sonarMeasureResponse: SonarMeasuresResponse,
        sonarComponentTreeResponse: SonarComponentTreeResponse,
        sonarQualityGateResponse: SonarQualityGateResponse
    ): SonarAnalysisDocument {

        val nextVersion = repository.getNextVersion(serviceKey)

        val document = mapper.responseToDocument(
            repositoryKey = repositoryKey,
            serviceKey = serviceKey,
            projectKey = projectKey,
            version = nextVersion,
            issuesResponse = sonarIssueResponse,
            measuresResponse = sonarMeasureResponse,
            componentTreeResponse = sonarComponentTreeResponse,
            qualityGateResponse = sonarQualityGateResponse
        )

        return repository.save(document)
    }

    override fun getLatestAnalysis(projectKey: String): SonarAnalysisDocument? {
        return repository.findLatestByProjectKey(projectKey)
    }

    override fun getAnalysisHistory(serviceKey: String): List<SonarAnalysisDocument> {
        return repository.findByService(serviceKey)
    }

    override fun getAnalysisByVersion(
        serviceKey: String,
        version: Long
    ): SonarAnalysisDocument? {
        return repository.findByServiceAndVersion(serviceKey, version)
    }
}

