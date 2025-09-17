package net.plshark.github.client.notifications

import com.fasterxml.jackson.annotation.JsonInclude

/** Data for a GitHub notification subject. */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Subject(
    val title: String,
    val url: String,
    val type: String,
) {
    companion object {
        const val TYPE_PULL_REQUEST = "PullRequest"
    }
}
