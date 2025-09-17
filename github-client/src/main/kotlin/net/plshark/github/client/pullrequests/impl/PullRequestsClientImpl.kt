package net.plshark.github.client.pullrequests.impl

import net.plshark.github.client.exceptions.GithubClientException
import net.plshark.github.client.pullrequests.PullRequest
import net.plshark.github.client.pullrequests.PullRequestsClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

/** A default [PullRequestsClient] implementation. */
class PullRequestsClientImpl(
    private val webClient: WebClient,
    private val baseUrl: String = "https://api.github.com",
    private val apiToken: String,
) : PullRequestsClient {
    override suspend fun getPullRequest(
        id: Long,
        owner: String,
        repository: String,
    ): PullRequest = getPullRequest("$baseUrl/repos/$owner/$repository/pulls/$id")

    override suspend fun getPullRequest(url: String): PullRequest {
        try {
            return webClient
                .get()
                .uri(url)
                .header("Authorization", "Bearer $apiToken")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("Accept", "application/vnd.github.raw+json")
                .retrieve()
                .awaitBody()
        } catch (e: WebClientResponseException) {
            throw GithubClientException(e.statusCode.value(), e.responseBodyAsString, e)
        }
    }
}
