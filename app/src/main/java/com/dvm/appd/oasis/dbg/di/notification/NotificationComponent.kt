package com.dvm.appd.oasis.dbg.di.notification

import com.dvm.appd.oasis.dbg.notification.NotificationViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [NotificationModule::class])
interface NotificationComponent {

    fun injectNotifications(factory: NotificationViewModelFactory)

}