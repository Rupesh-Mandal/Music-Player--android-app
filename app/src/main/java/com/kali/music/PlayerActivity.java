package com.kali.music;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.kali.music.MainActivity.musicFiles;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
TextView song_name,artist_name,duration_played,duration_total;
ImageView cover_art,nextBtn,prevBtn,backBtn,shuffleBtn,repeatBtn,playPauseBtn;
RelativeLayout mcountener;
SeekBar seekBar;
int position=-1;
static ArrayList<MusicFile> listSong=new ArrayList<>();
static Uri uri;
static MediaPlayer mediaPlayer;
private Handler handler=new Handler();
private Thread playThread,prevThread,nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
       mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b){
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    int mCurrentPodition=mediaPlayer.getCurrentPosition() /1000;
                    seekBar.setProgress(mCurrentPodition);
                    duration_played.setText(formattedTime(mCurrentPodition));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        metsData(uri);
        super.onResume();
    }

    private void playThreadBtn() {
        playThread=new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()){
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPodition=mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPodition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
        else {
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPodition=mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPodition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private void nextThreadBtn() {
        nextThread=new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position + 1) % listSong.size());
            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metsData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPodition=mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPodition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
           mediaPlayer.setOnCompletionListener(this);
           playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position + 1) % listSong.size());
            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metsData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPodition=mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPodition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
           mediaPlayer.setOnCompletionListener(this);
          // playPauseBtn.setImageResource(R.drawable.ic_play);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void prevThreadBtn() {
        prevThread=new Thread(){
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position - 1) < 0 ? (listSong.size() -1) : (position -1));
            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metsData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPodition=mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPodition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
           mediaPlayer.setOnCompletionListener(this);
         //   playPauseBtn.setImageResource(R.drawable.ic_pause);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position - 1) < 0 ? (listSong.size() -1) : (position -1));
            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metsData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPodition=mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPodition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
           mediaPlayer.setOnCompletionListener(this);
          //  playPauseBtn.setImageResource(R.drawable.ic_play);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
        }

    }

    private String formattedTime(int mCurrentPodition) {
        String totalOut="";
        String totalNew="";
        String seconds=String.valueOf(mCurrentPodition % 60);
        String minutes=String.valueOf(mCurrentPodition / 60);
        totalOut=minutes + ":" + seconds;
        totalNew=minutes + ":" + "0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        }
        else {
            return totalOut;
        }

    }

    private void getIntentMethod() {
        position=getIntent().getIntExtra("position",-1);
        listSong=musicFiles;
        if (listSong != null){
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            uri=Uri.parse(listSong.get(position).getPath());
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metsData(uri);


    }

    private void initView() {
        song_name=findViewById(R.id.song_name);
        artist_name=findViewById(R.id.song_artist);
        duration_played=findViewById(R.id.durationPlayed);
        duration_total=findViewById(R.id.durationTotal);
        cover_art=findViewById(R.id.cover_art);
        nextBtn=findViewById(R.id.id_next);
        prevBtn=findViewById(R.id.id_prev);
        backBtn=findViewById(R.id.back_btn);
        shuffleBtn=findViewById(R.id.id_shuffle_off);
        repeatBtn=findViewById(R.id.id_repeat);
        playPauseBtn=findViewById(R.id.play_pause);
        seekBar=findViewById(R.id.seekBar);
        mcountener=findViewById(R.id.mContainer);
    }

    private void metsData(Uri uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal=Integer.parseInt(listSong.get(position).getDuration()) /1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art=retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art!=null){
//            Glide.with(this)
//                    .asBitmap()
//                    .load(art)
//                    .into(cover_art);

          bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
          ImageAnimation(this,cover_art,bitmap);
          Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
              @SuppressLint("ResourceType")
              @Override
              public void onGenerated(@Nullable Palette palette) {
                  Palette.Swatch swatch=palette.getDominantSwatch();
                  if (swatch!=null){
                      ImageView grediant=findViewById(R.id.imaeViewGredient);
                      ImageView gradiant1=findViewById(R.id.imaeViewGredienttop);
                      RelativeLayout mcontainer=findViewById(R.id.mContainer);
                      RelativeLayout relativeLayoutForBottom=findViewById(R.id.relative_layout_for_bottom);
                      relativeLayoutForBottom.setBackgroundResource(R.drawable.gredient_bg);
                      grediant.setBackgroundResource(R.drawable.gredient_bg);
                      gradiant1.setBackgroundResource(R.drawable.gredient_bg_top);
                      mcontainer.setBackgroundResource(R.drawable.main_bg);
                      GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),0x0000000});
                     grediant.setBackground(gradientDrawable);
                      relativeLayoutForBottom.setBackground(gradientDrawable);
                      GradientDrawable gradientDrawable1=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{swatch.getRgb(),0x0000000});
                      gradiant1.setBackground(gradientDrawable1);

                      GradientDrawable gradientDrawablebg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),swatch.getRgb()});
                      mcontainer.setBackground(gradientDrawablebg);
                      //setting song_text color according cover_art
                      song_name.setTextColor(swatch.getTitleTextColor());
                      artist_name.setTextColor(Color.RED);
                  }
                  else {
                      ImageView grediant=findViewById(R.id.imaeViewGredient);
                      ImageView gradiant1=findViewById(R.id.imaeViewGredienttop);
                      RelativeLayout mcontainer=findViewById(R.id.mContainer);
                      RelativeLayout relativeLayoutForBottom=findViewById(R.id.relative_layout_for_bottom);
                      relativeLayoutForBottom.setBackgroundResource(R.drawable.gredient_bg);
                      grediant.setBackgroundResource(R.drawable.gredient_bg);
                      gradiant1.setBackgroundResource(R.drawable.gredient_bg_top);
                      mcontainer.setBackgroundResource(R.drawable.main_bg);
                      GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0x00000000});
                      grediant.setBackground(gradientDrawable);
                      relativeLayoutForBottom.setBackground(gradientDrawable);
                      GradientDrawable gradientDrawable1=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0xff000000,0x00000000});
                      gradiant1.setBackground(gradientDrawable1);

                      GradientDrawable gradientDrawablebg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0xff000000});
                      mcontainer.setBackground(gradientDrawablebg);
                      //setting song_text color according cover_art
                      song_name.setTextColor(Color.WHITE);
                      artist_name.setTextColor(Color.RED);
                  }

              }

          });
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.ic_launcher_background)
                    .into(cover_art);


        }
    }

    public void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){

        Animation animOut= AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
        final Animation animIn= AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);


    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if (mediaPlayer!=null){

            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}