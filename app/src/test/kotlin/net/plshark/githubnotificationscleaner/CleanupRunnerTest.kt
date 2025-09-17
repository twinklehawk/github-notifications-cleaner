package net.plshark.githubnotificationscleaner

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import net.plshark.github.client.notifications.Notification
import net.plshark.github.client.notifications.NotificationsClient
import net.plshark.github.client.notifications.Subject
import net.plshark.github.client.pullrequests.PullRequest
import net.plshark.github.client.pullrequests.PullRequestsClient
import org.junit.jupiter.api.Test

class CleanupRunnerTest {
    private val notificationsClient: NotificationsClient = mockk()
    private val pullRequestsClient: PullRequestsClient = mockk()
    private val runner = CleanupRunner(notificationsClient, pullRequestsClient)

    @Test
    fun `run marks closed pull request notifications as done`() {
        every { notificationsClient.getNotifications() } returns
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
        every { notificationsClient.getNotifications() } returns
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
        every { notificationsClient.getNotifications() } returns
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
}
