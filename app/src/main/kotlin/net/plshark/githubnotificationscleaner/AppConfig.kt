package net.plshark.githubnotificationscleaner

import net.plshark.github.client.notifications.NotificationsClient
import net.plshark.github.client.notifications.impl.NotificationsClientImpl
import net.plshark.github.client.pullrequests.PullRequestsClient
import net.plshark.github.client.pullrequests.impl.PullRequestsClientImpl
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/** Main Spring configuration for the application. */
@Configuration
@EnableConfigurationProperties(ApplicationProperties::class)
class AppConfig {
    /** Creates a [PullRequestsClient]. */
    @Bean
    fun pullRequestsClient(
        properties: ApplicationProperties,
        webClientBuilder: WebClient.Builder,
    ): PullRequestsClient =
        PullRequestsClientImpl(
            webClient = webClientBuilder.build(),
            baseUrl = properties.github.baseUrl,
            apiToken = properties.github.apiToken,
        )

    /** Creates a [NotificationsClient]. */
    @Bean
    fun notificationsClient(
        properties: ApplicationProperties,
        webClientBuilder: WebClient.Builder,
    ): NotificationsClient =
        NotificationsClientImpl(
            webClient = webClientBuilder.build(),
            baseUrl = properties.github.baseUrl,
            apiToken = properties.github.apiToken,
        )
}
