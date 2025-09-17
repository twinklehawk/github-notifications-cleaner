package net.plshark.githubnotificationscleaner

import org.springframework.boot.context.properties.ConfigurationProperties

/** Configuration properties for the application. */
@ConfigurationProperties("application")
data class ApplicationProperties(
    val github: GitHubClientProperties,
)

/** Configuration properties for the GitHub client. */
data class GitHubClientProperties(
    val baseUrl: String = "https://api.github.com",
    val apiToken: String,
)
