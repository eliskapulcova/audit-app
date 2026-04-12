package com.example.auditapi.model

import com.example.auditapi.model.SonarMeasure

data class SonarComponentTreeResponse(
    val baseComponent: Component,
    val components: List<Component>
)

data class Component(
    val key: String,
    val name: String,
    val qualifier: String,
    val path: String? = null,
    val language: String? = null,
    val measures: List<SonarMeasure>
)

