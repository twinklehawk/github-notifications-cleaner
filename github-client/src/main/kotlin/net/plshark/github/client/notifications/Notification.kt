package net.plshark.github.client.notifications

import com.fasterxml.jackson.annotation.JsonInclude

/** Data for a GitHub notification. */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Notification(
    val id: String,
    val unread: Boolean,
    val subject: Subject,
)
