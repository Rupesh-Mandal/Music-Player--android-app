package com.kali.music;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {
    private Context mContex;
    private ArrayList<MusicFile> mFiles;

    MusicAdapter(Context mContex, ArrayList<MusicFile> mFiles) {
        this.mContex = mContex;
        this.mFiles = mFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.music_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());
       byte[] image = getAlbumArt(mFiles.get(position).getPath());


        if (image != null) {
            Glide.with(mContex)
                    .load(image)
                    .into(holder.album_art);
        } else {
            Glide.with(mContex)
                    .load(R.drawable.ic_launcher_background)
                    .into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContex, PlayerActivity.class);
                intent.putExtra("position",position);
                mContex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_icon);
        }
    }

     private byte[] getAlbumArt(String uri){
    MediaMetadataRetriever retriever=new MediaMetadataRetriever();
    retriever.setDataSource(uri);
    byte[] art=retriever.getEmbeddedPicture();
         retriever.release();
         return art;

     }

}