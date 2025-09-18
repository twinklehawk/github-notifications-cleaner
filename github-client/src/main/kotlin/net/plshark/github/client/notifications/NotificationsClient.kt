package net.plshark.github.client.notifications

import kotlinx.coroutines.flow.Flow

/** A client interface for interacting with GitHub notifications. */
interface NotificationsClient {
    /** Lists all notifications for the current user, sorted by most recently updated. */
    fun getNotifications(): Flow<Notification>

    /** Lists all unread notifications for the current user, sorted by most recently updated. */
    fun getUnreadNotifications(): Flow<Notification>

    /** Marks a thread as done. */
    suspend fun markThreadDone(threadId: Long)
}
