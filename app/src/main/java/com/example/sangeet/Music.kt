package com.example.sangeet

import java.util.concurrent.TimeUnit

data class Music(val id:String,val title:String,val album:String,val artist:String,val duration: Long = 0,val path: String,val artUri: String)

fun formatDuration(duration: Long):String{
    val minutes = TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS)-minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES))
    return String.format("%02d:%02d",minutes,seconds)
}

