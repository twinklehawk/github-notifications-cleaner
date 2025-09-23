package net.plshark.githubnotificationscleaner

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

/** Configuration properties for the application. */
@ConfigurationProperties("application")
data class ApplicationProperties(
    val github: GitHubClientProperties,
    val notifications: NotificationProperties = NotificationProperties(),
)

/** Configuration properties for the GitHub client. */
data class GitHubClientProperties(
    val baseUrl: String = "https://api.github.com",
    val apiToken: String,
)

/** Configuration properties for notifications to process. */
data class NotificationProperties(
    val read: Boolean = true,
    val sinceOffset: Duration? = null,
)
