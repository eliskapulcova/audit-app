package com.example.auditapi.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class AuditIssueDto(
    val updateDate: String,
    val line: Int,
    val rule: String,
    val project: String,
    val effort: String,
    val type: String,
    val cleanCodeAttribute: String,
    val internalTags: String,
    val issueStatus: String,
    val flows: String,
    val scope: String,
    val key: String,
    val severity: String,
    val comments: String,
    val author: String,
    val fromSonarQubeUpdate: Boolean,
    val cleanCodeAttributeCategory: String,
    val linkedTicketStatus: String,
    val messageFormattings: String,
    val impacts: String,
    val message: String,
    val creationDate: String,
    val quickFixAvailable: Boolean,
    val tags: String,
    val codeVariants: String,
    val component: String,
    val prioritizedRule: Boolean,
    val textRange: String,
    val debt: String,
    val hash: String,
    val status: String
)

