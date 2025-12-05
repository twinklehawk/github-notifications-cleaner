package net.plshark.githubnotificationscleaner

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import net.plshark.github.client.notifications.GetNotificationsRequest
import net.plshark.github.client.notifications.Notification
import net.plshark.github.client.notifications.NotificationsClient
import net.plshark.github.client.notifications.Subject
import net.plshark.github.client.pullrequests.PullRequest
import net.plshark.github.client.pullrequests.PullRequestsClient
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.InstantSource
import java.time.OffsetDateTime
import java.time.ZoneId

/**
 * A [CommandLineRunner] that iterates over incomplete notifications and marks
 * any notifications with corresponding closed pull requests as done.
 */
@Component
class CleanupRunner(
    private val notificationsClient: NotificationsClient,
    private val pullRequestsClient: PullRequestsClient,
    private val instantSource: InstantSource,
    private val applicationProperties: ApplicationProperties,
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(Application::class.java)

    override fun run(vararg args: String) {
        log.info("Cleaning GitHub notifications")
        runBlocking {
            getNotifications()
                .filter { it.subject.type == Subject.TYPE_PULL_REQUEST }
                .filter { pullRequestsClient.getPullRequest(it.subject.url).state == PullRequest.STATE_CLOSED }
                .onEach { log.info("Marking notification [{}] as done", it.subject.title) }
                .collect { notificationsClient.markThreadDone(it.id.toLong()) }
        }
        log.info("Done cleaning GitHub notifications")
    }

    private fun getNotifications(): Flow<Notification> {
        val request =
            GetNotificationsRequest(
                all = applicationProperties.notifications.read,
                since =
                    applicationProperties.notifications.sinceOffset?.let {
                        OffsetDateTime.ofInstant(instantSource.instant().minus(it), ZoneId.of("Z"))
                    },
            )
        return notificationsClient.getNotifications(request)
    }
}
