package net.plshark.github.client.pullrequests

import com.fasterxml.jackson.annotation.JsonInclude

/** Data for a GitHub pull request. */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class PullRequest(
    val id: Long,
    val state: String,
) {
    companion object {
        const val STATE_CLOSED = "closed"
    }
}
