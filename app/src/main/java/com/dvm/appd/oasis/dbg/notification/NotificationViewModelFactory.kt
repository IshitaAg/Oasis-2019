package com.dvm.appd.oasis.dbg.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dvm.appd.oasis.dbg.OASISApp
import com.dvm.appd.oasis.dbg.di.notification.NotificationModule
import javax.inject.Inject

class NotificationViewModelFactory() : ViewModelProvider.Factory {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        OASISApp.appComponent.newNotificationComponent(NotificationModule()).injectNotifications(this)
        return NotificationViewModel(notificationRepository) as T
    }

}