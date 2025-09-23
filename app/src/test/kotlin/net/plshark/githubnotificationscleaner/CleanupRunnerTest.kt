package net.plshark.githubnotificationscleaner

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import net.plshark.github.client.notifications.Notification
import net.plshark.github.client.notifications.NotificationsClient
import net.plshark.github.client.notifications.Subject
import net.plshark.github.client.pullrequests.PullRequest
import net.plshark.github.client.pullrequests.PullRequestsClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.time.InstantSource
import java.util.concurrent.atomic.AtomicBoolean

class CleanupRunnerTest {
    private val notificationsClient: NotificationsClient = mockk()
    private val pullRequestsClient: PullRequestsClient = mockk()
    private val instantSource = InstantSource.fixed(Instant.ofEpochMilli(500_000_000))
    private val properties =
        ApplicationProperties(github = GitHubClientProperties(apiToken = "test"))
    private val runner = CleanupRunner(notificationsClient, pullRequestsClient, instantSource, properties)

    @Test
    fun `run marks closed pull request notifications as done`() {
        every { notificationsClient.getNotifications(any()) } returns
            flowOf(
                Notification(
                    id = "1",
                    unread = true,
                    subject = Subject(title = "title-1", url = "url-1", type = Subject.TYPE_PULL_REQUEST),
                ),
                Notification(
                    id = "2",
                    unread = true,
                    subject = Subject(title = "title-2", url = "url-2", type = Subject.TYPE_PULL_REQUEST),
                ),
            )
        coEvery { pullRequestsClient.getPullRequest("url-1") } returns
            PullRequest(id = 1, state = PullRequest.STATE_CLOSED)
        coEvery { pullRequestsClient.getPullRequest("url-2") } returns
            PullRequest(id = 1, state = PullRequest.STATE_CLOSED)
        coEvery { notificationsClient.markThreadDone(any()) } just runs

        runner.run()

        coVerify { notificationsClient.markThreadDone(1) }
        coVerify { notificationsClient.markThreadDone(2) }
    }

    @Test
    fun `run ignores notifications for types other than pull request`() {
        every { notificationsClient.getNotifications(any()) } returns
            flowOf(
                Notification(
                    id = "1",
                    unread = true,
                    subject = Subject(title = "title-1", url = "url-1", type = "Issue"),
                ),
            )

        runner.run()

        coVerify(exactly = 0) { notificationsClient.markThreadDone(any()) }
    }

    @Test
    fun `run ignores notifications for non-closed pull requests`() {
        every { notificationsClient.getNotifications(any()) } returns
            flowOf(
                Notification(
                    id = "1",
                    unread = true,
                    subject = Subject(title = "title-1", url = "url-1", type = Subject.TYPE_PULL_REQUEST),
                ),
            )
        coEvery { pullRequestsClient.getPullRequest("url-1") } returns PullRequest(id = 1, state = "open")

        runner.run()

        coVerify(exactly = 0) { notificationsClient.markThreadDone(any()) }
    }

    @Test
    fun `run fetches all notifications when configured`() {
        val flowObserver = AtomicBoolean()
        every { notificationsClient.getNotifications(any()) } returns
            emptyFlow<Notification>().onCompletion { flowObserver.set(true) }

        runner.run()

        assertThat(flowObserver.get()).isTrue
    }

    @Test
    fun `run fetches only unread notifications when configured`() {
        val properties =
            ApplicationProperties(
                github = GitHubClientProperties(apiToken = "test"),
                notifications = NotificationProperties(read = false),
            )
        val readOnlyRunner = CleanupRunner(notificationsClient, pullRequestsClient, instantSource, properties)
        val flowObserver = AtomicBoolean()
        every { notificationsClient.getNotifications(any()) } returns
            emptyFlow<Notification>().onCompletion { flowObserver.set(true) }

        readOnlyRunner.run()

        assertThat(flowObserver.get()).isTrue
    }

    @Test
    fun `run fetches notifications updated since an offset when configured`() {
        val properties =
            ApplicationProperties(
                github = GitHubClientProperties(apiToken = "test"),
                notifications = NotificationProperties(sinceOffset = Duration.ofHours(10)),
            )
        val sinceRunner = CleanupRunner(notificationsClient, pullRequestsClient, instantSource, properties)
        val flowObserver = AtomicBoolean()
        every { notificationsClient.getNotifications(any()) } returns
            emptyFlow<Notification>().onCompletion { flowObserver.set(true) }

        sinceRunner.run()

        assertThat(flowObserver.get()).isTrue
    }
}
