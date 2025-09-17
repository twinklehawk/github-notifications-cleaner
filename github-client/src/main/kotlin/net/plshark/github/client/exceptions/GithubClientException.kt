package net.plshark.github.client.exceptions

/** A [RuntimeException] indicating an error occurred while making a GitHub API call. */
class GithubClientException(
    val statusCode: Int? = null,
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException("GitHub request failed with status $statusCode and message $message", cause)
