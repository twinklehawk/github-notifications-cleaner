package net.plshark.github.client.notifications

import java.time.OffsetDateTime

/**
 * A request object for retrieving notifications.
 *
 * @param all If true, show notifications marked as read. Defaults to false.
 * @param participating If true, only shows notifications in which the user is directly participating or mentioned.
 * Defaults to false.
 * @param since Only show results that were last updated after the given time.
 * @param before Only show notifications updated before the given time.
 * @param page The page number of the results to fetch. Defaults to 1.
 * @param perPage The number of results per page (max 50). Defaults to 50.
 */
data class GetNotificationsRequest(
    val all: Boolean? = null,
    val participating: Boolean? = null,
    val since: OffsetDateTime? = null,
    val before: OffsetDateTime? = null,
    val page: Int = 1,
    val perPage: Int? = null,
)
