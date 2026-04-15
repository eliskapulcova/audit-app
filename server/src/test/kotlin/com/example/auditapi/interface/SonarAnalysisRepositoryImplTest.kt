package com.example.auditapi.`interface`

import com.example.auditapi.enumeration.IssueType
import com.example.auditapi.enumeration.Severity
import com.example.auditapi.model.ProjectStatus
import com.example.auditapi.model.SonarAnalysisDocument
import com.example.auditapi.util.Helper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoTemplate

@DataMongoTest(properties = [
    "spring.data.mongodb.uri=mongodb://localhost:27017/test"
])
@Import(SonarAnalysisRepositoryImpl::class)
class SonarAnalysisRepositoryImplTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var repository: SonarAnalysisRepository

    @BeforeEach
    fun setup() {
        mongoTemplate.dropCollection(SonarAnalysisDocument::class.java)
    }

    @Test
    fun `save should persist document`() {
        val doc = Helper.doc(projectKey = "proj")

        val saved = repository.save(doc)

        assertNotNull(saved)
        assertEquals("proj", saved.projectKey)
    }

    @Test
    fun `findLatestByProjectKey should return latest version`() {
        val doc1 = Helper.doc(projectKey = "proj").copy(analysisVersion = 1)
        val doc2 = Helper.doc(projectKey = "proj").copy(analysisVersion = 2)

        mongoTemplate.save(doc1)
        mongoTemplate.save(doc2)

        val result = repository.findLatestByProjectKey("proj")

        assertEquals(2, result?.analysisVersion)
    }

    @Test
    fun `findByService should return sorted results`() {
        val doc1 = Helper.doc().copy(id = "1", serviceKey = "svc", analysisVersion = 1)
        val doc2 = Helper.doc().copy(id = "2", serviceKey = "svc", analysisVersion = 3)
        val doc3 = Helper.doc().copy(id = "3", serviceKey = "svc", analysisVersion = 2)

        mongoTemplate.save(doc1)
        mongoTemplate.save(doc2)
        mongoTemplate.save(doc3)

        val result = repository.findByService("svc")

        assertEquals(listOf(3L, 2L, 1L), result.map { it.analysisVersion })
    }

    @Test
    fun `findByServiceAndVersion should return correct document`() {
        val doc = Helper.doc(projectKey = "proj")
            .copy(serviceKey = "svc", analysisVersion = 5)

        mongoTemplate.save(doc)

        val result = repository.findByServiceAndVersion("svc", 5)

        assertNotNull(result)
        assertEquals(5, result?.analysisVersion)
    }

    @Test
    fun `getNextVersion should increment version`() {
        val doc = Helper.doc(projectKey = "proj").copy(analysisVersion = 3)
        mongoTemplate.save(doc)

        val next = repository.getNextVersion("proj")

        assertEquals(4, next)
    }

    @Test
    fun `getNextVersion should return 1 when no data`() {
        val next = repository.getNextVersion("unknown")

        assertEquals(1, next)
    }

    @Test
    fun `aggregateSonarData should compute metrics correctly`() {
        val issues = listOf(
            Helper.issue(status = "OPEN", severity = Severity.MAJOR).copy(type = IssueType.BUG),
            Helper.issue(status = "OPEN", severity = Severity.CRITICAL).copy(type = IssueType.VULNERABILITY),
            Helper.issue(status = "OPEN", severity = Severity.MINOR).copy(type = IssueType.CODE_SMELL)
        )

        val doc = Helper.doc(
            projectKey = "proj",
            issues = issues
        ).copy(
            analysisVersion = 1,
            qualityGateStatus = ProjectStatus(
                status = "OK",
                conditions = emptyList(),
                ignoredConditions = false,
                period = null,
                caycStatus = null
            ),
            measures = listOf(
                Helper.measure("coverage", "85"),
                Helper.measure("duplicated_lines_density", "5")
            )
        )

        mongoTemplate.save(doc)

        val result = repository.aggregateSonarData("proj")

        assertNotNull(result)
        assertEquals(1, result!!.bugs)
        assertEquals(1, result.vulnerabilities)
        assertEquals(1, result.codeSmells)
        assertEquals(85.0, result.coverage)
        assertEquals(5.0, result.duplications)
        assertEquals("Passed", result.qualityGate)
        assertEquals("B", result.reliability)
    }
}
