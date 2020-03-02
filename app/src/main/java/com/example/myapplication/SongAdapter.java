package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    private int resourseId;

    public SongAdapter(Context context, int textViewResourceId, List<Song> objects) {
        super(context,textViewResourceId, objects);
        resourseId =textViewResourceId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        Song song = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourseId,parent,false);
        ImageView songImage = (ImageView)view.findViewById(R.id.song_item_image);
        TextView songName = (TextView)view.findViewById(R.id.song_item_name);
        TextView songAuthor = (TextView)view.findViewById(R.id.song_item_author);
        songImage.setImageResource(song.getImageId());
        songName.setText(song.getName());
        songAuthor.setText(song.getAuthor());

        return view;

    }
}
