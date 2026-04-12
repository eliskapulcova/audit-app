package com.example.auditapi.model

data class SonarComponent(
    val key: String,
    val enabled: Boolean,
    val qualifier: String,
    val name: String,
    val longName: String,
    val path: String? = null
)

