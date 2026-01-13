package com.example.auditapi.service

import com.example.auditapi.model.AuditIssueDto
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.BufferedReader

@Service
class CsvParserService {

    fun parseCsv(filePath: String): List<AuditIssueDto> {
        val resource = ClassPathResource(filePath)

        val lines = resource.inputStream.bufferedReader().readLines()
        if (lines.isEmpty()) return emptyList()

        val header = lines.first().split(",")
        val dataLines = lines.drop(1)

        return dataLines.map { line ->
            val tokens = line.split(",")

            AuditIssueDto(
                updateDate = tokens[header.indexOf("updateDate")],
                line = tokens[header.indexOf("line")].toInt(),
                rule = tokens[header.indexOf("rule")],
                project = tokens[header.indexOf("project")],
                effort = tokens[header.indexOf("effort")],
                type = tokens[header.indexOf("type")],
                cleanCodeAttribute = tokens[header.indexOf("cleanCodeAttribute")],
                internalTags = tokens[header.indexOf("internalTags")],
                issueStatus = tokens[header.indexOf("issueStatus")],
                flows = tokens[header.indexOf("flows")],
                scope = tokens[header.indexOf("scope")],
                key = tokens[header.indexOf("key")],
                severity = tokens[header.indexOf("severity")],
                comments = tokens[header.indexOf("comments")],
                author = tokens[header.indexOf("author")],
                fromSonarQubeUpdate = tokens[header.indexOf("fromSonarQubeUpdate")].toBoolean(),
                cleanCodeAttributeCategory = tokens[header.indexOf("cleanCodeAttributeCategory")],
                linkedTicketStatus = tokens[header.indexOf("linkedTicketStatus")],
                messageFormattings = tokens[header.indexOf("messageFormattings")],
                impacts = tokens[header.indexOf("impacts")],
                message = tokens[header.indexOf("message")],
                creationDate = tokens[header.indexOf("creationDate")],
                quickFixAvailable = tokens[header.indexOf("quickFixAvailable")].toBoolean(),
                tags = tokens[header.indexOf("tags")],
                codeVariants = tokens[header.indexOf("codeVariants")],
                component = tokens[header.indexOf("component")],
                prioritizedRule = tokens[header.indexOf("prioritizedRule")].toBoolean(),
                textRange = tokens[header.indexOf("textRange")],
                debt = tokens[header.indexOf("debt")],
                hash = tokens[header.indexOf("hash")],
                status = tokens[header.indexOf("status")]
            )
        }
    }
}
