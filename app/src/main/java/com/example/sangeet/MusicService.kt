package com.example.sangeet

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.handleCoroutineException

class MusicService: Service() {
    private val myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession : MediaSessionCompat


    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }
    inner class MyBinder: Binder(){
        fun currentServices(): MusicService {
            return this@MusicService
        }
    }
     fun showNotification(){
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)

            .setContentTitle(player.musicListPA[player.songposition].title)
            .setContentText(player.musicListPA[player.songposition].artist)
            .setSmallIcon(R.drawable.music_icon)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.pervious_icon,"Previous",null)
            .addAction(R.drawable.play_icon,"play",null)
            .addAction(R.drawable.next_icon,"next",null)
            .build()
        startForeground(13,notification)
     }
}