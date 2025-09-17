package net.plshark.github.client.pullrequests

/** A client interface for interacting with GitHub pull requests. */
interface PullRequestsClient {
    /**
     * Retrieves a pull request.
     *
     * @param id the pull request ID
     * @param owner the ID of owner of the repository containing the pull request
     * @param repository the name of the repository containing the pull request
     */
    suspend fun getPullRequest(
        id: Long,
        owner: String,
        repository: String,
    ): PullRequest

    /**
     * Retrieves a pull request by an already-known URL.
     *
     * @param url the URL of the pull request
     */
    suspend fun getPullRequest(url: String): PullRequest
}
