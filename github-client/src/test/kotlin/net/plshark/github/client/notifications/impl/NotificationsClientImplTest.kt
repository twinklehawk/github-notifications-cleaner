package net.plshark.github.client.notifications.impl

import kotlinx.coroutines.flow.toList
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import net.plshark.github.client.notifications.Notification
import net.plshark.github.client.notifications.Subject
import net.plshark.test.TestUtils.doBlocking
import okhttp3.Headers.Companion.headersOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient

class NotificationsClientImplTest {
    private val server = MockWebServer()
    private lateinit var client: NotificationsClientImpl

    @BeforeEach
    fun setup() {
        server.start()
        client =
            NotificationsClientImpl(
                WebClient.create(),
                server.url("").toString().substringBeforeLast("/"),
                "test-token",
            )
    }

    @AfterEach
    fun cleanup() {
        server.close()
    }

    @Test
    fun `getNotifications sends the correct request`() =
        doBlocking {
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body = "[]",
                ),
            )

            val result = client.getNotifications().toList()

            assertThat(result).isEmpty()
            assertThat(server.requestCount).isEqualTo(1)
            val request = server.takeRequest()
            assertThat(request.url.encodedPath).isEqualTo("/notifications")
            assertThat(request.url.query).isEqualTo("all=true&page=1")
            assertThat(request.headers["Authorization"]).isEqualTo("Bearer test-token")
            assertThat(request.headers["Accept"]).isEqualTo("application/vnd.github+json")
            assertThat(request.headers["X-GitHub-Api-Version"]).isEqualTo("2022-11-28")
        }

    @Test
    fun `getNotifications parses and returns notifications`() =
        doBlocking {
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body =
                        """
                        [
                          {"id": "1", "unread": true, "subject": {"title": "note-1", "url": "test-url-1", "type": "PullRequest"}}
                        ]
                        """.trimIndent(),
                ),
            )
            server.enqueue(
                MockResponse(headers = headersOf("Content-Type", "application/json"), body = "[]"),
            )

            val result = client.getNotifications().toList()

            assertThat(result).containsExactly(
                Notification(
                    id = "1",
                    unread = true,
                    subject =
                        Subject(
                            title = "note-1",
                            url = "test-url-1",
                            type = "PullRequest",
                        ),
                ),
            )
        }

    @Test
    fun `getNotifications fetches all results`() =
        doBlocking {
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body =
                        """
                        [
                          {"id": "1", "unread": true, "subject": {"title": "note-1", "url": "test-url-1", "type": "Issue"}},
                          {"id": "2", "unread": false, "subject": {"title": "note-2", "url": "test-url-2", "type": "PullRequest"}}
                        ]
                        """.trimIndent(),
                ),
            )
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body =
                        """
                        [
                          {"id": "3", "unread": true, "subject": {"title": "note-3", "url": "test-url-3", "type": "PullRequest"}}
                        ]
                        """.trimIndent(),
                ),
            )
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body = "[]",
                ),
            )

            val result = client.getNotifications().toList()

            assertThat(result).hasSize(3)
            assertThat(result[0].id).isEqualTo("1")
            assertThat(result[1].id).isEqualTo("2")
            assertThat(result[2].id).isEqualTo("3")
            assertThat(server.requestCount).isEqualTo(3)
            assertThat(server.takeRequest().url.queryParameter("page")).isEqualTo("1")
            assertThat(server.takeRequest().url.queryParameter("page")).isEqualTo("2")
            assertThat(server.takeRequest().url.queryParameter("page")).isEqualTo("3")
        }

    @Test
    fun `markThreadDone sends the correct request`() =
        doBlocking {
            server.enqueue(MockResponse())

            client.markThreadDone(1)

            assertThat(server.requestCount).isEqualTo(1)
            val request = server.takeRequest()
            assertThat(request.url.encodedPath).isEqualTo("/notifications/threads/1")
            assertThat(request.headers["Authorization"]).isEqualTo("Bearer test-token")
            assertThat(request.headers["X-GitHub-Api-Version"]).isEqualTo("2022-11-28")
        }
}
