package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayActivity<e> extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener{
    //设置为全局的Player可以在退出的时候停止播放
    MediaPlayer Player = new MediaPlayer();

    private List<Song> songList = new ArrayList<>();
    private int songPosition = -1;
    private TextView name_text = null;
    private int palyMode = 0; //音乐播放模式 0：顺序播放 1：单曲循环 2：随机播放

    private ImageView bpause = null;
    private ImageView bset = null;
    @Override
    protected void onDestroy() {
        if(Player.isPlaying())
            Player.release();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        name_text = (TextView) findViewById(R.id.music_play_name);
        //System.out.println("播放");

        Intent intent = getIntent();
        songList = (List<Song>)intent.getSerializableExtra("songList");
        songPosition = intent.getIntExtra("songPosition",-1);

        ImageView bnext = (ImageView) findViewById(R.id.next_song);
        ImageView bprevious = (ImageView) findViewById(R.id.previous_song);
        bset = (ImageView) findViewById(R.id.music_play_set);
        ImageView bback = (ImageView) findViewById(R.id.music_play_back);
        bpause = (ImageView)findViewById(R.id.pause_button);

        bnext.setOnClickListener(MusicPlayActivity.this);
        bprevious.setOnClickListener(MusicPlayActivity.this);
        bset.setOnClickListener(MusicPlayActivity.this);
        bback.setOnClickListener(MusicPlayActivity.this);
        bpause.setOnClickListener(MusicPlayActivity.this);
        Player.setOnCompletionListener(this);
//        Bpause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pause();
//            }
//        });
//
//        bnext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                songPosition++;
//                play();
//            }
//        });
//
//        bprevious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                songPosition--;
//                play();
//            }
//        });
        play();

        //获取歌曲名和歌曲路径
//        String songName = intent.getStringExtra("songName");
//        String songPath = intent.getStringExtra("songPath");
//        Song song = songList.get(songPosition);
//        String songName = song.getName();
//        String songPath = song.getSongPath();
//
//
//        TextView name_text = (TextView) findViewById(R.id.music_play_name);
//        name_text.setText(songName);
//
//        //创建播放时间格式化工具
//        SimpleDateFormat mFormat = new SimpleDateFormat("mm:ss");
//        //创建MediaPlayer和设置监听
//        try {
//            Player.setDataSource(songPath);
//            Player.prepare();
//            Player.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    //播放歌曲
    private void play(){
        //获取歌曲名和歌曲路径
        if(palyMode == 2) {
            songPosition = getRandom();
        }
        Song song = songList.get(songPosition);
        String songName = song.getName();
        String songPath = song.getSongPath();

        //设置名字
        name_text.setText(songName);

        //创建MediaPlayer
        try {
            //如果已经在播放一首歌
            if(Player.isPlaying())
                Player.pause();
            Player.release();
            Player = new MediaPlayer();
            Player.setDataSource(songPath);
            Player.setOnCompletionListener(this);
            Player.prepare();
            Player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pause(){
        if(Player.isPlaying()){
            Player.pause();
            bpause.setImageResource(R.drawable.play_button);
        }else{
            Player.start();
            bpause.setImageResource(R.drawable.pause_button);
        }
    }

    private int getRandom(){
        Random r = new Random();
        return r.nextInt(songList.size()-1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case R.id.pause_button: {
               pause();
               break;
           }
            case R.id.next_song:{
                songPosition++;
                play();
                break;
            }
            case R.id.previous_song:{
                songPosition--;
                play();
                break;
            }
            case R.id.music_play_back:{
                finish();
                break;
            }
            case R.id.music_play_set:{
                palyMode = (palyMode+1)%3;
                if(palyMode == 0){
                    bset.setImageResource(R.drawable.play_sequence);
                    Toast.makeText(MusicPlayActivity.this,"顺序播放",Toast.LENGTH_SHORT).show();
                }else if(palyMode == 1){
                    bset.setImageResource(R.drawable.play_single);
                    Toast.makeText(MusicPlayActivity.this,"单曲循环",Toast.LENGTH_SHORT).show();
                }else{
                    bset.setImageResource(R.drawable.play_random);
                    Toast.makeText(MusicPlayActivity.this,"随机播放",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        System.out.println("播放完毕");
        if(palyMode == 0){
            songPosition++;
            play();
        }else if(palyMode ==1){
            play();
        }else{
            songPosition=getRandom();
            play();
        }

    }
}
