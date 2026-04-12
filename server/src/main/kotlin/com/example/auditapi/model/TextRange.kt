package com.example.auditapi.model

data class TextRange(
    val startLine: Int,
    val endLine: Int,
    val startOffset: Int,
    val endOffset: Int
)

