package com.dvm.appd.oasis.dbg.di

import com.dvm.appd.oasis.dbg.di.elas.ElasComponent
import com.dvm.appd.oasis.dbg.di.elas.ElasModule
import com.dvm.appd.oasis.dbg.di.auth.AuthComponent
import com.dvm.appd.oasis.dbg.di.auth.AuthModule
import com.dvm.appd.oasis.dbg.di.events.EventsComponent
import com.dvm.appd.oasis.dbg.di.events.EventsModule
import com.dvm.appd.oasis.dbg.di.notification.NotificationComponent
import com.dvm.appd.oasis.dbg.di.notification.NotificationModule
import com.dvm.appd.oasis.dbg.di.profile.ProfileComponent
import com.dvm.appd.oasis.dbg.di.profile.ProfileModule
import com.dvm.appd.oasis.dbg.di.splash.SplashComponent
import com.dvm.appd.oasis.dbg.di.splash.SplashModule
import com.dvm.appd.oasis.dbg.di.wallet.WalletComponent
import com.dvm.appd.oasis.dbg.di.wallet.WalletModule
import dagger.Component
import javax.inject.Singleton

@Singleton @Component(modules = [AppModule::class])
interface AppComponent {
    fun newWalletComponent(b:WalletModule):WalletComponent

    fun newEventsComponent(a:EventsModule): EventsComponent

    fun newElasComponent(a: ElasModule): ElasComponent

    fun newAuthComponent(c:AuthModule):AuthComponent

    fun newProfileComponent(d:ProfileModule):ProfileComponent

    fun newSplashComponent(e:SplashModule):SplashComponent

    fun newNotificationComponent(n: NotificationModule): NotificationComponent
}