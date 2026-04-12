package com.example.auditapi.client

import com.example.auditapi.model.SonarComponentTreeResponse
import com.example.auditapi.model.SonarIssuesResponse
import com.example.auditapi.model.SonarMeasuresResponse
import com.example.auditapi.model.SonarQualityGateResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class SonarClient(
    private val restTemplate: RestTemplate
) {
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
