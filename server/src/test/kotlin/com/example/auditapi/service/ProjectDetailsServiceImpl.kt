package com.example.auditapi.service

import com.example.auditapi.model.SonarAnalysisDocument
import com.example.auditapi.`interface`.SonarAnalysisRepository
import com.example.auditapi.mapper.SonarAnalysisDocumentMapper
import com.example.auditapi.model.IssueDto
import com.example.auditapi.model.ProjectSummaryDto
import com.example.auditapi.model.SonarQubeDataDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class ProjectDetailsServiceImplTest {

    @Mock
    lateinit var sonarRepository: SonarAnalysisRepository

    @Mock
    lateinit var sonarMapper: SonarAnalysisDocumentMapper

    @InjectMocks
    lateinit var service: ProjectDetailsServiceImpl

    @Test
    fun `should return project details when project exists`() {
        val projectKey = "proj-1"

        val doc = mock<SonarAnalysisDocument> {
            on { issues } doReturn listOf(
                mock { on { status } doReturn "OPEN" },
                mock { on { status } doReturn "CLOSED" }
            )
        }

        val sonarData = mock<SonarQubeDataDto>()
        val topIssues = listOf<IssueDto>()
        val summary = mock<ProjectSummaryDto>()

        whenever(sonarRepository.aggregateSonarData(projectKey)).thenReturn(sonarData)
        whenever(sonarRepository.findLatestByProjectKey(projectKey)).thenReturn(doc)
        whenever(sonarMapper.getMeasure(doc, "coverage")).thenReturn("80")
        whenever(sonarMapper.mapGrade(any())).thenReturn("A")
        whenever(sonarMapper.mapTopIssues(doc)).thenReturn(topIssues)
        whenever(sonarMapper.buildProjectSummary(any(), any())).thenReturn(summary)

        val result = service.getProjectDetails(projectKey)

        assertNotNull(result)
        assertEquals(sonarData, result.sonarQubeData)
        assertEquals(topIssues, result.topIssues)
        assertEquals(summary, result.projectSummary)
        assertEquals(2, result.kpis?.size)
    }

    @Test
    fun `should throw exception when project not found`() {
        val projectKey = "missing"

        whenever(sonarRepository.aggregateSonarData(projectKey)).thenReturn(null)

        assertThrows(IllegalArgumentException::class.java) {
            service.getProjectDetails(projectKey)
        }
    }

    @Test
    fun `should build KPIs correctly`() {
        val doc = mock<SonarAnalysisDocument> {
            on { issues } doReturn listOf(
                mock { on { status } doReturn "OPEN" },
                mock { on { status } doReturn "OPEN" },
                mock { on { status } doReturn "CLOSED" }
            )
        }

        whenever(sonarMapper.getMeasure(doc, "coverage")).thenReturn("75.5")

        val result = service.buildKpis(doc)

        assertEquals(2, result?.size)

        val openIssuesKpi = result!![0]
        assertEquals("open-issues", openIssuesKpi.id)
        assertEquals(2, openIssuesKpi.value)

        val coverageKpi = result[1]
        assertEquals("coverage", coverageKpi.id)
        assertEquals(75.5, coverageKpi.value)
    }

    @Test
    fun `should handle null document in KPIs`() {
        whenever(sonarMapper.getMeasure(null, "coverage")).thenReturn(null)

        val result = service.buildKpis(null)

        assertEquals(0, result?.get(0)?.value)
        assertEquals(0.0, result?.get(1)?.value)
    }

    @Test
    fun `should calculate health score correctly`() {
        val doc = mock<SonarAnalysisDocument> {
            on { issues } doReturn listOf(
                mock { on { status } doReturn "OPEN" },
                mock { on { status } doReturn "OPEN" }
            )
        }

        whenever(sonarMapper.getMeasure(doc, "coverage")).thenReturn("80")
        whenever(sonarMapper.mapGrade(any())).thenReturn("A")

        val result = service.calculateHealthScore(doc)

        assertNotNull(result)
        assertEquals(104.coerceIn(0, 100), result?.score) // 100 - 4 + 8
        assertEquals("A", result?.grade)
    }

    @Test
    fun `should return null health score when doc is null`() {
        val result = service.calculateHealthScore(null)
        assertNull(result)
    }

    @Test
    fun `should clamp health score between 0 and 100`() {
        val doc = mock<SonarAnalysisDocument> {
            on { issues } doReturn List(100) {
                mock { on { status } doReturn "OPEN" }
            }
        }

        whenever(sonarMapper.getMeasure(doc, "coverage")).thenReturn("0")
        whenever(sonarMapper.mapGrade(any())).thenReturn("F")

        val result = service.calculateHealthScore(doc)

        assertEquals(0, result?.score)
    }
}
