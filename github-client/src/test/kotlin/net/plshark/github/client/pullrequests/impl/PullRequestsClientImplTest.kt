package net.plshark.github.client.pullrequests.impl

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import net.plshark.test.TestUtils.doBlocking
import okhttp3.Headers.Companion.headersOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient

class PullRequestsClientImplTest {
    private val server = MockWebServer()
    private lateinit var client: PullRequestsClientImpl

    @BeforeEach
    fun setup() {
        server.start()
        client =
            PullRequestsClientImpl(
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
    fun `getPullRequest by ID sends the correct request`() =
        doBlocking {
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body = """{"id": 1, "state": "closed"}""",
                ),
            )

            client.getPullRequest(id = 1, owner = "test-owner", repository = "test-repo")

            assertThat(server.requestCount).isEqualTo(1)
            val request = server.takeRequest()
            assertThat(request.url.encodedPath).isEqualTo("/repos/test-owner/test-repo/pulls/1")
            assertThat(request.headers["Authorization"]).isEqualTo("Bearer test-token")
            assertThat(request.headers["Accept"]).isEqualTo("application/vnd.github.raw+json")
            assertThat(request.headers["X-GitHub-Api-Version"]).isEqualTo("2022-11-28")
        }

    @Test
    fun `getPullRequest by URL sends the correct request`() =
        doBlocking {
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body = """{"id": 1, "state": "closed"}""",
                ),
            )

            val host = server.url("").toString().substringBeforeLast("/")
            client.getPullRequest("$host/repos/test-owner-1/test-repo-1/pulls/2")

            assertThat(server.requestCount).isEqualTo(1)
            val request = server.takeRequest()
            assertThat(request.url.encodedPath).isEqualTo("/repos/test-owner-1/test-repo-1/pulls/2")
            assertThat(request.headers["Authorization"]).isEqualTo("Bearer test-token")
            assertThat(request.headers["Accept"]).isEqualTo("application/vnd.github.raw+json")
            assertThat(request.headers["X-GitHub-Api-Version"]).isEqualTo("2022-11-28")
        }

    @Test
    fun `getPullRequest parses and returns the pull request`() =
        doBlocking {
            server.enqueue(
                MockResponse(
                    headers = headersOf("Content-Type", "application/json"),
                    body = """{"id": 1, "state": "closed"}""",
                ),
            )

            val result = client.getPullRequest(id = 1, owner = "test-owner", repository = "test-repo")

            assertThat(result.id).isEqualTo(1)
            assertThat(result.state).isEqualTo("closed")
        }
}
