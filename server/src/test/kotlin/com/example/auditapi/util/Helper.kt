package com.example.auditapi.util

import com.example.auditapi.enumeration.IssueStatus
import com.example.auditapi.enumeration.IssueType
import com.example.auditapi.enumeration.Severity
import com.example.auditapi.model.SonarAnalysisDocument
import com.example.auditapi.model.SonarIssue

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
        projectKey: String = "proj",
        issues: List<SonarIssue> = emptyList(),
        totalIssues: Int = issues.size,
        coverage: String? = "80"
    ) = SonarAnalysisDocument(
        id = "doc-1",
        serviceKey = "service",
        repositoryKey = "repo",
        analysisVersion = 1L,
        projectKey = projectKey,
        totalIssues = totalIssues,
        effortTotal = null,
        issues = issues,
        components = emptyList()
    )
}
