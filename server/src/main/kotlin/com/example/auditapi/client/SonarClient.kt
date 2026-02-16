package com.example.auditapi.client

import com.example.auditapi.domain.model.SonarComponentTreeResponse
import com.example.auditapi.domain.model.SonarIssuesResponse
import com.example.auditapi.domain.model.SonarMeasuresResponse
import com.example.auditapi.domain.model.SonarQualityGateResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class SonarClient(
    private val restTemplate: RestTemplate
) {

    // + count by severity and type - Aggregate by Type and Severity
    /*
    val counts = mutableMapOf<String, MutableMap<String, Int>>()

for (issue in issues) {
    val type = issue.type
    val severity = issue.severity
    counts.computeIfAbsent(type) { mutableMapOf() }
          .merge(severity, 1, Int::plus)
}

    */

    fun fetchIssues(projectKey: String, branch: String): SonarIssuesResponse {

        val uri = UriComponentsBuilder
            .fromPath("/api/issues/search")
            .queryParam("componentKeys", projectKey)
            .queryParam("branch", branch)
            .queryParam("ps", 500)
            .build()
            .toUriString()

        return restTemplate.getForObject(uri, SonarIssuesResponse::class.java)
            ?: throw IllegalStateException("Empty issues response from SonarQube")
    }

    fun fetchMeasures(projectKey: String, branch: String): SonarMeasuresResponse {

        //http://localhost:9000/api/measures/component?component=age_verification&branch=main&metricKeys=reliability_rating,security_rating,security_review_rating,sqale_rating,coverage,duplicated_lines_density,comment_lines_density,ncloc,files,sqale_debt_ratio,reliability_remediation_effort,security_remediation_effort,sqale_index
        val uri = UriComponentsBuilder
            .fromPath("/api/measures/component")
            .queryParam("component", projectKey)
            .queryParam("branch", branch)
            .queryParam(
                "metricKeys",
                "ncloc,coverage,duplicated_lines_density,complexity,cognitive_complexity,comment_lines_density,"
            )
            .build()
            .toUriString()

        return restTemplate.getForObject(uri, SonarMeasuresResponse::class.java)
            ?: throw IllegalStateException("Empty measures response from SonarQube")
    }

    fun fetchComponentTree(projectKey: String, branch: String): SonarComponentTreeResponse {

        //http://localhost:9000/api/measures/component_tree?component=age_verification&branch=main&metricKeys=ncloc,coverage,duplicated_lines_density,complexity,cognitive_complexity&strategy=all&ps=500
        // - z toho vypočítat mix a max

        val uri = UriComponentsBuilder
            .fromPath("/api/measures/component_tree")
            .queryParam("component", projectKey)
            .queryParam("branch", branch)
            .queryParam(
                "metricKeys",
                "ncloc,coverage,duplicated_lines_density,complexity,cognitive_complexity"
            )
            .queryParam("strategy", "all") //all?
            .queryParam("ps", 500)
            .build()
            .toUriString()

        return restTemplate.getForObject(uri, SonarComponentTreeResponse::class.java)
            ?: throw IllegalStateException("Empty component tree response")
    }

    fun fetchQualityGate(projectKey: String): SonarQualityGateResponse {

        //http://localhost:9000/api/qualitygates/project_status?projectKey=age_verification&branch=main
        val uri = UriComponentsBuilder
            .fromPath("/api/qualitygates/project_status")
            .queryParam("projectKey", projectKey)
            .build()
            .toUriString()

        return restTemplate.getForObject(uri, SonarQualityGateResponse::class.java)
            ?: throw IllegalStateException("Empty quality gate response")
    }

    /*fun fetchQualityProfiles(projectKey: String): SonarQualityProfilesResponse {

        val uri = UriComponentsBuilder
            .fromPath("/api/qualityprofiles/search")
            .queryParam("projectKey", projectKey)
            .build()
            .toUriString()

        return restTemplate.getForObject(uri, SonarQualityProfilesResponse::class.java)
            ?: throw IllegalStateException("Empty quality profiles response")
    }*/
}

