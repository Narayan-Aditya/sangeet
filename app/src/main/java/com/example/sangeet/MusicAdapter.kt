package com.example.sangeet

import android.app.DownloadManager.Request
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sangeet.databinding.MusicViewBinding


class MusicAdapter(private val context: Context, private val musiclist: ArrayList<Music>) : RecyclerView.Adapter<MusicAdapter.Myholder>(){
    class Myholder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root){
        val title = binding.songName
        val album = binding.songAlbum
        val image = binding.songIcon
        val duration = binding.songDuration
        val root= binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
        return Myholder(MusicViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MusicAdapter.Myholder, position: Int) {
        holder.title.text = musiclist[position].title
        holder.album.text = musiclist[position].album
        holder.duration.text = formatDuration(musiclist[position].duration)

        Glide.with(context)
            .load(musiclist[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(holder.image)
            holder.root.setOnClickListener{
                val intent = Intent(context,player::class.java)
                intent.putExtra("index",position)
                intent.putExtra("class",    "MusicAdapter")
                ContextCompat.startActivity(context, intent ,null)
            }
    }

    override fun getItemCount(): Int {
        return musiclist.size
    }
}



