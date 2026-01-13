package com.example.auditapi.controller

import com.example.auditapi.model.AuditIssueDto
import com.example.auditapi.service.CsvParserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/csv")
class CsvController(private val csvParserService: CsvParserService) {

    @GetMapping("/parse")
    fun parseCsv(): List<AuditIssueDto> {
        val filePath = "age-verification-issues-report.csv"
        return csvParserService.parseCsv(filePath)
    }
}
