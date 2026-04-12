package com.example.auditapi.`interface`

import SonarAnalysisDocument
import com.example.auditapi.model.SonarQubeDataDto

interface SonarAnalysisRepository {
    fun save(analysis: SonarAnalysisDocument): SonarAnalysisDocument
    fun findLatestByProjectKey(projectKey: String): SonarAnalysisDocument?
    fun findByService(serviceKey: String): List<SonarAnalysisDocument>
    fun findByServiceAndVersion(serviceKey: String, version: Long): SonarAnalysisDocument?
    fun getNextVersion(projectKey: String): Long
    fun aggregateSonarData(projectKey: String): SonarQubeDataDto?
}
