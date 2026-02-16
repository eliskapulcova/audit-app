package com.example.auditapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class SonarClientConfig {

    @Bean
    fun sonarRestTemplate(
        builder: RestTemplateBuilder,
        @Value("\${sonar.base-url}") baseUrl: String,
        @Value("\${sonar.token}") token: String
    ): RestTemplate =
        builder
            .rootUri(baseUrl)
            .basicAuthentication(token, "")
            .build()
}

