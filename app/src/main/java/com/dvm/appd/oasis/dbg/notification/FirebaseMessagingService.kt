package com.dvm.appd.oasis.dbg.notification

import android.annotation.SuppressLint
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.di.AppModule
import com.dvm.appd.oasis.dbg.shared.AppDatabase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class FirebaseMessagingService : FirebaseMessagingService() {

    lateinit var crashAnalytics: FirebaseAnalytics
    lateinit var roomDatabsae: AppDatabase

   override fun onCreate() {
        Log.d("Notification", "Service Started")
        crashAnalytics = FirebaseAnalytics.getInstance(this)
        roomDatabsae = AppModule(application).providesAppDatabase(application)
        super.onCreate()
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        var remoteMessage = p0
        Log.d("Notification", "onMessageRecived called")
        if (remoteMessage == null) {
            Log.d("Notification", "Null Data")
        }

        if (remoteMessage!!.data.size > 0) {
            Log.d("Notification", "Non null data")
            try {
                Log.e("Notification", remoteMessage.data.toString())
                var json = remoteMessage.data
                Log.d("Notification", "Recived json = ${remoteMessage.data.toString()}")
                handleDataMessage(json)
            }catch (e: Exception) {
                Log.e("Notification", "Failed to convert to json = $e")
                // TODO setup firebase analytic log here
            }
        }
        else {
            Log.d("Notification", "Entered Else ${remoteMessage.data.toString()}")
        }
        super.onMessageReceived(p0)
    }

    @SuppressLint("CheckResult")
    private fun handleDataMessage(json: MutableMap<String, String>) {
        Log.d("Notification", "Handling data")
        var id: String = ""
        try {
            id = json["order_id"].toString().toLong().toString()
        } catch (e: Exception) {
            Log.d("Notification", "Entered Catch")
            id = "-1"
        }
        Log.d("Notifidcation", "Recived id = $id")
        if (id == "-1") {
            Log.d("Notifidcation", "Recived id = $id")
            id = try {
                json["round_id"].toString().toLong().toString()
            } catch (e: Exception) {
                "0"
            }
        }
        Log.d("Notifidcation", "Recived id = $id")
        val title = json["title"]
        Log.d("Notification", "Title = ${title}")
        val body = json["body"]
        Log.d("Notification", "Body = ${body}")
        val channel = try {
            json["channel"]
        } catch (e: Exception) {
            resources.getString(R.string.chanel_id_general_notifications)
        }
        Log.d("Notification", "Channel = ${channel}")
        val sportsName = try {
            json["event_name"]!!
        } catch (e: Exception) {
            "Not Available"
        }

        val notificatoin = Notification(id = id!!, title = title!!, body = body!!, channel = channel!!)

        val otp = try {
            json["otp"]!!
        } catch (e: Exception) {
            "0000"
        }
        Log.d("Notification", "Notification = ${notificatoin.toString()}")

        Log.d("Notification", "Room instance = ${roomDatabsae.toString()}")
        roomDatabsae.notificationDao().insertNotification(notificatoin).doOnSubscribe {
            Log.d("Notification", "Started adding data to table")
        }
            .subscribe({
                Log.d("Notification", "Succesfully added data to table")
                /*val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, notificatoin.id)
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, notificatoin.title)
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Book")*/
                // TODO setup firbase analytic log
            },
                {
                    Log.e("Notification", "Error adding notification to room \n${it}")
                    // TODO setup firbase analytic log
                })

        Log.d("Notification", "$sportsName\n$otp\n")

        if (sportsName != "Not Available" && sportsName != "null" && sportsName != null) {
            sendNotification(notificatoin, sportsName?:"sprotsName")
        } else if (otp != null && otp != "null" && otp != "0000") {
            sendNotification(notificatoin, notificatoin.id, otp?:"OTP")
        } else {
            sendNotification(notificatoin)
        }
    }

    private fun sendNotification(message: Notification) {
        Log.d("Notification", "Sending Notification")
        val pendingIntent = NavDeepLinkBuilder(this.baseContext)
            .setGraph(R.navigation.navigation_graph)
            .setComponentName(MainActivity::class.java)
            .setDestination(R.id.action_events)
            .createPendingIntent()
        // TODO: Change icon appropriately
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, message.channel)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.body))
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .build()
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(Integer.parseInt(message.id) , notificationBuilder)
        }
    }

    @SuppressLint("CheckResult")
    private fun sendNotification(message: Notification, sport: String) {
        val pendingIntent = NavDeepLinkBuilder(this.baseContext)
            .setGraph(R.navigation.navigation_graph)
            .setComponentName(MainActivity::class.java)
            .setDestination(R.id.action_events)
            .createPendingIntent()
        roomDatabsae.eventsDao().getAllFavourites().subscribeOn(Schedulers.io()).doOnSuccess {
            Log.d("Notification", "Data recived from room = ${it.toString()}")
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (sport != "Not Available") {
                for(s in it) {
                    if (sport == s.eventId.toString()) {
                        Log.d("Notification", "Found matching sport = $s")
                        val notificationBuilder = NotificationCompat.Builder(this, message.channel)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(message.title)
                            .setContentText(message.body)
                            .setStyle(NotificationCompat.BigTextStyle().bigText(message.body))
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .build()
                        with(NotificationManagerCompat.from(this)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(Integer.parseInt(message.id) , notificationBuilder)
                        }
                    }
                }
            }
        } .subscribe({},{
            Log.e("Notification", "Error in Displaying notification = ${it.toString()}")
        })
    }

    private fun sendNotification(message: Notification, orderId: String, otp: String) {
        val pendingIntent = NavDeepLinkBuilder(this.baseContext)
            .setGraph(R.navigation.navigation_graph)
            .setComponentName(MainActivity::class.java)
            .setDestination(R.id.action_order_history)
            .createPendingIntent()
        if (otp.equals("0000")) {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, message.channel)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(message.title)
                .setContentText(message.body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message.body))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build()
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(Integer.parseInt(orderId) , notificationBuilder)
            }
        } else {
            // TODO Correct the pending intent
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, message.channel)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(message.title)
                .setContentText(message.body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message.body))
                .setSound(defaultSoundUri)
                .addAction(NotificationCompat.Action(R.drawable.ic_action_event_24px, "View Otp", pendingIntent))
                .build()
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(Integer.parseInt(orderId) , notificationBuilder)
            }
        }
    }
}