package net.plshark.github.client.notifications.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEmpty
import net.plshark.github.client.exceptions.GithubClientException
import net.plshark.github.client.notifications.Notification
import net.plshark.github.client.notifications.NotificationsClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.bodyToFlow
import java.util.concurrent.atomic.AtomicBoolean

/** A default [NotificationsClient] implementation. */
class NotificationsClientImpl(
    private val webClient: WebClient,
    private val baseUrl: String = "https://api.github.com",
    private val apiToken: String,
) : NotificationsClient {
    override fun getNotifications(): Flow<Notification> = getNotifications(includeRead = true)

    override fun getUnreadNotifications(): Flow<Notification> = getNotifications(includeRead = false)

    private fun getNotifications(includeRead: Boolean = true) =
        flow {
            var page = 1
            val done = AtomicBoolean()
            while (!done.get()) {
                val result =
                    getNotificationsPage(includeRead = includeRead, page = page)
                        .onEmpty { done.set(true) }
                emitAll(result)
                ++page
            }
        }

    private fun getNotificationsPage(
        includeRead: Boolean = true,
        page: Int = 1,
    ) = webClient
        .get()
        .uri("$baseUrl/notifications?all=$includeRead&page=$page")
        .header("Authorization", "Bearer $apiToken")
        .header("Accept", "application/vnd.github+json")
        .header("X-GitHub-Api-Version", "2022-11-28")
        .retrieve()
        .bodyToFlow<Notification>()
        .catch {
            if (it !is WebClientResponseException) {
                throw it
            }
            throw GithubClientException(it.statusCode.value(), it.responseBodyAsString, it)
        }

    override suspend fun markThreadDone(threadId: Int) {
        try {
            webClient
                .delete()
                .uri("$baseUrl/notifications/threads/$threadId")
                .header("Authorization", "Bearer $apiToken")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .retrieve()
                .awaitBodilessEntity()
        } catch (e: WebClientResponseException) {
            throw GithubClientException(e.statusCode.value(), e.responseBodyAsString, e)
        }
    }
}
