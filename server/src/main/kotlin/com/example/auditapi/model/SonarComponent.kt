package com.example.auditapi.domain.model

data class SonarComponent(
    val key: String,
    val enabled: Boolean,
    val qualifier: String,
    val name: String,
    val longName: String,
    val path: String? = null
)

