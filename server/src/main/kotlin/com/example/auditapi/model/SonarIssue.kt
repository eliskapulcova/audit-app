package com.example.auditapi.model

import com.example.auditapi.model.TextRange
import com.example.auditapi.enumeration.IssueStatus
import com.example.auditapi.enumeration.IssueType
import com.example.auditapi.enumeration.Severity

data class SonarIssue(
    val key: String,
    val rule: String,
    val severity: Severity,
    val component: String,
    val project: String,
    val hash: String?,
    val textRange: TextRange?,
    val flows: List<Any>,
    val resolution: String?,
    val status: String,
    val message: String,
    val effort: String?,
    val debt: String?,
    val author: String?,
    val tags: List<String>,
    val creationDate: String,
    val updateDate: String,
    val closeDate: String?,
    val type: IssueType,
    val scope: String,
    val quickFixAvailable: Boolean,
    val cleanCodeAttribute: String?,
    val cleanCodeAttributeCategory: String?,
    val impacts: List<Impact>,
    val issueStatus: IssueStatus
)


