package net.plshark.github.client.notifications

import kotlinx.coroutines.flow.Flow

/** A client interface for interacting with GitHub notifications. */
interface NotificationsClient {
    /**
     * Lists notifications for the current user, sorted by most recently updated.
     *
     * This method ignores the page value in the request and returns all results.
     */
    fun getNotifications(request: GetNotificationsRequest): Flow<Notification>

    /** Marks a thread as done. */
    suspend fun markThreadDone(threadId: Long)
}
