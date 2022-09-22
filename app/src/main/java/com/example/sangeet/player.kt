package com.example.sangeet

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sangeet.player.Companion.musicListPA
import kotlinx.android.synthetic.main.activity_player.*

class player : AppCompatActivity(),ServiceConnection, MediaPlayer.OnCompletionListener{
    private lateinit var runnable: Runnable
    private var handler = Handler()

    companion object{
        lateinit var musicListPA:ArrayList<Music>
        var songposition: Int = 0
//        var mediaPlayer: MediaPlayer? = null
        var IsPlay: Boolean = false
        var musicService: MusicService? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
//for Starting SERVICES
        val intent = Intent(this,MusicService::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)
        initialize()
        back_button.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

//For Play Pause and Next Button Working
        previousbtnPA.setOnClickListener { prenext(incement = false) }
        nextbtnPA.setOnClickListener { prenext(incement = true) }
        playpause.setOnClickListener {
            if (IsPlay){
                pause()
            }
            else
                play()
        }

//For seek Bar working Function
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) musicService!!.mediaPlayer!!.seekTo(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })


    }
    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[songposition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(songimgPA)
        songnamePA.text= musicListPA[songposition].title
    }
    private fun createmediaplayer(){
        try {
            if(musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songposition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            IsPlay=true
            playpause.setIconResource(R.drawable.pause_icon)
            seekbarstart.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            seekbarend.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            seekbar.progress = 0
            seekbar.max = musicService!!.mediaPlayer!!.duration
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
        }
        catch (e: Exception){return }
    }
    private fun play(){
        playpause.setIconResource(R.drawable.pause_icon)
        IsPlay = true
        musicService!!.mediaPlayer!!.start()
    }
    private fun pause(){
        playpause.setIconResource(R.drawable.play_icon)
        IsPlay= false
        musicService!!.mediaPlayer!!.pause()
    }
    private fun prenext(incement: Boolean){
        if(incement){
            setposition(incement = true)
            setLayout()
            createmediaplayer()
        }
        else{
            setposition(incement = false)
            setLayout()
            createmediaplayer()
        }
    }
    private fun setposition(incement: Boolean){
        if(incement){
            if(musicListPA.size -1 == songposition){
                songposition = 0
                Toast.makeText(this,"First Song",Toast.LENGTH_SHORT).show()
            }
            else{
                ++songposition
            }
        }
        else{
            if(0== songposition){
                songposition = musicListPA.size-1
                Toast.makeText(this,"Last Song",Toast.LENGTH_SHORT).show()
            }
            else{
                --songposition
            }
        }
    }
    private fun initialize() {
        songposition = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "MusicAdapter" ->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
            }
            "MainActivity" ->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()
                setLayout()
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentServices()
        createmediaplayer()
        seekBarSetup()
//        musicService!!.showNotification()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        setposition(incement = true)
        createmediaplayer()
        try {setLayout() }
        catch (e:Exception){return}
    }
    private fun seekBarSetup(){
        runnable = Runnable {
            seekbarstart.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            seekbar.progress = musicService!!.mediaPlayer!!.currentPosition
            handler.postDelayed(runnable,200)
        }
        handler.postDelayed(runnable,0)
    }
}