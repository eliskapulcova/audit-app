package com.example.auditapi.controller

import com.example.auditapi.model.AnalysisReport
import com.example.auditapi.model.AuditIssueDto
import com.example.auditapi.model.AuditReport
import com.example.auditapi.model.IssueReport
import com.example.auditapi.service.AnalysisReportService
import com.example.auditapi.service.CsvParserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class CsvController(
    private val csvParserService: CsvParserService,
    private val analysisReportService: AnalysisReportService
) {

    @GetMapping("/report")
    fun parseCsv(): AuditReport {
        val filePath = "age-verification-issues-report.csv"
        return AuditReport(
            issueReport = IssueReport(csvParserService.parseCsv(filePath)),
            analysisReport = AnalysisReport(analysisReportService.loadAnalysisReport())
        )
    }
}
