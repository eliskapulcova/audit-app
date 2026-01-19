package com.example.auditapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuditApiApplication

fun main(args: Array<String>) {
    runApplication<AuditApiApplication>(*args)
}