package com.example.auditapi.util

import com.example.auditapi.enumeration.IssueStatus
import com.example.auditapi.enumeration.IssueType
import com.example.auditapi.enumeration.Severity
import com.example.auditapi.model.*
import org.bson.types.ObjectId

object Helper {

    fun issue(
        key: String = "1",
        status: String = "OPEN",
        severity: Severity = Severity.MAJOR
    ) = SonarIssue(
        key = key,
        rule = "rule",
        severity = severity,
        component = "comp",
        project = "proj",
        hash = null,
        textRange = null,
        flows = emptyList(),
        resolution = null,
        status = status,
        message = "msg",
        effort = null,
        debt = null,
        author = null,
        tags = emptyList(),
        creationDate = "2024",
        updateDate = "2024",
        closeDate = null,
        type = IssueType.BUG,
        scope = "MAIN",
        quickFixAvailable = false,
        cleanCodeAttribute = null,
        cleanCodeAttributeCategory = null,
        impacts = emptyList(),
        issueStatus = IssueStatus.OPEN
    )

    fun doc(
        id: String = ObjectId().toString(),
        projectKey: String = "proj",
        issues: List<SonarIssue> = emptyList(),
        totalIssues: Int = issues.size,
        coverage: String? = "80"
    ) = SonarAnalysisDocument(
        id = id,
        serviceKey = "service",
        repositoryKey = "repo",
        analysisVersion = 1L,
        projectKey = projectKey,
        totalIssues = totalIssues,
        effortTotal = null,
        issues = issues,
        components = emptyList()
    )

    fun issuesResponse(
        total: Int = 1
    ) = SonarIssuesResponse(
        total = total,
        p = 1,
        ps = 100,
        paging = Paging(pageIndex = 1, pageSize = 100, total = total),
        effortTotal = 10,
        issues = listOf(issue()),
        components = listOf(componentSummary()),
        facets = emptyList()
    )

    fun componentSummary() = SonarComponent(
        key = "comp_1",
        enabled = true,
        qualifier = "FIL",
        name = "File1.kt",
        longName = "src/main/kotlin/File1.kt",
        path = "src/main/kotlin/File1.kt"
    )

    fun measuresResponse() = SonarMeasuresResponse(
        component = SonarComponentMeasures(
            measures = listOf(
                measure("coverage", "80"),
                measure("bugs", "0")
            )
        )
    )

    fun measure(
        metric: String,
        value: String? = "1",
        bestValue: Boolean? = false
    ) = SonarMeasure(metric, value, bestValue)

    fun componentTreeResponse() = SonarComponentTreeResponse(
        baseComponent = component(),
        components = listOf(component())
    )

    fun component() = Component(
        key = "file_1",
        name = "File1.kt",
        qualifier = "FIL",
        path = "src/File1.kt",
        language = "kotlin",
        measures = listOf(measure("ncloc", "100"))
    )

    fun qualityGateResponse(
        status: String = "OK"
    ) = SonarQualityGateResponse(
        projectStatus = ProjectStatus(
            status = status,
            conditions = listOf(
                condition("OK", "coverage")
            ),
            ignoredConditions = false,
            period = period(),
            caycStatus = "compliant"
        )
    )

    fun condition(
        status: String,
        metricKey: String
    ) = Condition(
        status = status,
        metricKey = metricKey,
        comparator = "GT",
        errorThreshold = "50",
        actualValue = "80"
    )

    fun period() = Period(
        mode = "date",
        date = "2024-01-01"
    )
}
