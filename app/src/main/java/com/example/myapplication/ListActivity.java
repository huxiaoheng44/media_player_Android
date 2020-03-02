package com.example.myapplication;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private List<Song> songList = new ArrayList<>();
    //动态注册广播接收器和intentfilter
    private IntentFilter intentFilter;
    private HeadsetPlugReceiver headsetPlugReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        TextView welcome_top = (TextView)findViewById(R.id.welcome_top);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        welcome_top.setText("欢迎您,"+username+"!");
//        //初始化歌曲列表，不能写在这里，要在获取权限之后 不然就会秒退
//        initSonglist();
        SongAdapter adapter = new SongAdapter(ListActivity.this,R.layout.song_item,songList);
        final ListView songListView = (ListView)findViewById(R.id.song_list);
        songListView.setAdapter(adapter);
        //设置点击动作
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songList.get(position);
                Intent intent = new Intent(ListActivity.this,MusicPlayActivity.class);
//                intent.putExtra("songPath",song.getSongPath());
//                intent.putExtra("songName",song.getName());
                intent.putExtra("songList",(Serializable)songList);
                intent.putExtra("songPosition",position);
                startActivity(intent);

                Toast.makeText(ListActivity.this,song.getSongPath(),Toast.LENGTH_SHORT).show();
            }
        });

        //动态注册耳机广播监听
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        headsetPlugReceiver = new HeadsetPlugReceiver();
        registerReceiver(headsetPlugReceiver, intentFilter);


        TextView welcomeTxt=(TextView)findViewById(R.id.welcome_top);
        Button backBtn=(Button)findViewById(R.id.back_to_main_button);
        String uname=this.getIntent().getStringExtra("username");
        welcomeTxt.setText("欢迎您，"+uname+"!");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //读取文件路径获取权限Manifest.permission.READ_EXTERNAL_STORAGE，才能获得列表
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
            initSonglist();
        } else {
            initSonglist();
        }


        //返回主界面
        Button back = (Button)findViewById(R.id.back_to_main_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
    private void initSonglist(){

//        songList.add(new Song("成都","赵雷",R.drawable.chengdu));
//        songList.add(new Song("莉莉安","宋东野",R.drawable.lilian));
//        songList.add(new Song("南山南","马頔",R.drawable.nanshannan));
//        songList.add(new Song("奇妙能力歌","陈粒",R.drawable.qimiaonenglige));

        Cursor cursor = null;
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // 获取专辑图片
                // 获取歌手信息
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //获取歌曲名称
                String disName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //获取文件路径
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Song song = new Song( disName, artist, url,R.drawable.default_cover);
                songList.add(song);
            }
        }

    }

    //运行权限申请
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initSonglist();
                } else {
                    Toast.makeText(this, "未授权，功能无法实现", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //创建监听耳机广播的类
    class HeadsetPlugReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("android.intent.action.HEADSET_PLUG")){
                if(intent.hasExtra("state")){
                    if (intent.getIntExtra("state", 0) == 1){
                        Toast.makeText(context, "耳机已连接", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "耳机已断开", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        //注销广播监听器
        unregisterReceiver(headsetPlugReceiver);
    }

}
