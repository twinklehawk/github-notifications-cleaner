package net.plshark.githubnotificationscleaner

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

/** Configuration properties for the application. */
@ConfigurationProperties("application")
data class ApplicationProperties(
    val github: GitHubClientProperties,
    val notifications: NotificationProperties = NotificationProperties(),
)

/**
 * Configuration properties for the GitHub client.
 * @param baseUrl the base GitHub API URL
 * @param apiToken the API token necessary to authenticate to the GitHub API
 */
data class GitHubClientProperties(
    val baseUrl: String = "https://api.github.com",
    val apiToken: String,
)

/**
 * Configuration properties for notifications to process.
 * @param read if true, also process notifications that have been marked as read
 * @param sinceOffset if not null, only process notifications modified since this offset
 */
data class NotificationProperties(
    val read: Boolean = true,
    val sinceOffset: Duration? = null,
)
