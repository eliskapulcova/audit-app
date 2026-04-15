package com.example.auditapi.service

import com.example.auditapi.client.SonarClient
import com.example.auditapi.`interface`.SonarAnalysisRepository
import com.example.auditapi.mapper.SonarAnalysisDocumentMapper
import com.example.auditapi.util.Helper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class SonarAnalysisServiceImplTest {

    @Mock
    lateinit var repository: SonarAnalysisRepository

    @Mock
    lateinit var sonarClient: SonarClient

    @Mock
    lateinit var mapper: SonarAnalysisDocumentMapper

    @InjectMocks
    lateinit var service: SonarAnalysisServiceImpl

    @Test
    fun `analyzeAndCreate should fetch data and persist analysis`() {
        val repositoryKey = "repo"
        val serviceKey = "service"
        val projectKey = "project"
        val branch = "main"

        val issues = Helper.issuesResponse()
        val measures = Helper.measuresResponse()
        val tree = Helper.componentTreeResponse()
        val gate = Helper.qualityGateResponse()

        val version = 1L
        val document = Helper.doc(projectKey = projectKey)

        whenever(sonarClient.fetchIssues(projectKey, branch)).thenReturn(issues)
        whenever(sonarClient.fetchMeasures(projectKey, branch)).thenReturn(measures)
        whenever(sonarClient.fetchComponentTree(projectKey, branch)).thenReturn(tree)
        whenever(sonarClient.fetchQualityGate(projectKey)).thenReturn(gate)

        whenever(repository.getNextVersion(serviceKey)).thenReturn(version)
        whenever(
            mapper.responseToDocument(
                repositoryKey,
                serviceKey,
                projectKey,
                version,
                issues,
                measures,
                tree,
                gate
            )
        ).thenReturn(document)

        whenever(repository.save(document)).thenReturn(document)

        val result = service.analyzeAndCreate(
            repositoryKey,
            serviceKey,
            projectKey,
            branch
        )

        assertEquals(document, result)

        verify(sonarClient).fetchIssues(projectKey, branch)
        verify(sonarClient).fetchMeasures(projectKey, branch)
        verify(sonarClient).fetchComponentTree(projectKey, branch)
        verify(sonarClient).fetchQualityGate(projectKey)

        verify(repository).getNextVersion(serviceKey)
        verify(mapper).responseToDocument(
            repositoryKey,
            serviceKey,
            projectKey,
            version,
            issues,
            measures,
            tree,
            gate
        )
        verify(repository).save(document)
    }

    @Test
    fun `createAnalysis should map and save`() {
        val repositoryKey = "repo"
        val serviceKey = "service"
        val projectKey = "project"

        val issues = Helper.issuesResponse()
        val measures = Helper.measuresResponse()
        val tree = Helper.componentTreeResponse()
        val gate = Helper.qualityGateResponse()

        val version = 2L
        val document = Helper.doc(projectKey = projectKey)

        whenever(repository.getNextVersion(serviceKey)).thenReturn(version)

        whenever(
            mapper.responseToDocument(
                repositoryKey,
                serviceKey,
                projectKey,
                version,
                issues,
                measures,
                tree,
                gate
            )
        ).thenReturn(document)

        whenever(repository.save(document)).thenReturn(document)

        val result = service.createAnalysis(
            repositoryKey,
            serviceKey,
            projectKey,
            issues,
            measures,
            tree,
            gate
        )

        assertEquals(document, result)

        verify(repository).getNextVersion(serviceKey)
        verify(mapper).responseToDocument(
            repositoryKey,
            serviceKey,
            projectKey,
            version,
            issues,
            measures,
            tree,
            gate
        )
        verify(repository).save(document)
    }

    @Test
    fun `getLatestAnalysis should return latest document`() {
        val projectKey = "project"
        val document = Helper.doc(projectKey = projectKey)

        whenever(repository.findLatestByProjectKey(projectKey)).thenReturn(document)

        val result = service.getLatestAnalysis(projectKey)

        assertEquals(document, result)
        verify(repository).findLatestByProjectKey(projectKey)
    }

    @Test
    fun `getLatestAnalysis should return null when not found`() {
        val projectKey = "project"

        whenever(repository.findLatestByProjectKey(projectKey)).thenReturn(null)

        val result = service.getLatestAnalysis(projectKey)

        assertNull(result)
    }

    @Test
    fun `getAnalysisHistory should return list of documents`() {
        val serviceKey = "service"
        val documents = listOf(
            Helper.doc(projectKey = "proj1"),
            Helper.doc(projectKey = "proj2")
        )

        whenever(repository.findByService(serviceKey)).thenReturn(documents)

        val result = service.getAnalysisHistory(serviceKey)

        assertEquals(2, result.size)
        assertEquals(documents, result)

        verify(repository).findByService(serviceKey)
    }

    @Test
    fun `getAnalysisByVersion should return document`() {
        val serviceKey = "service"
        val version = 1L
        val document = Helper.doc()

        whenever(repository.findByServiceAndVersion(serviceKey, version)).thenReturn(document)

        val result = service.getAnalysisByVersion(serviceKey, version)

        assertEquals(document, result)
        verify(repository).findByServiceAndVersion(serviceKey, version)
    }

    @Test
    fun `getAnalysisByVersion should return null when not found`() {
        val serviceKey = "service"
        val version = 1L

        whenever(repository.findByServiceAndVersion(serviceKey, version)).thenReturn(null)

        val result = service.getAnalysisByVersion(serviceKey, version)

        assertNull(result)
    }
}
