package com.example.auditapi.domain.model

data class TextRange(
    val startLine: Int,
    val endLine: Int,
    val startOffset: Int,
    val endOffset: Int
)

