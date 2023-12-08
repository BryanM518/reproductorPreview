package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import data.remote.AppleMusic;
import domain.model.Result;
import domain.model.Root;
import domain.model.TrackAdapter;

import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {

    private AppleMusic musicService;
    private EditText txtName;
    private Button btnSearch;
    private TextView lblArtistName;
    private ListView livTracks;
    private Root root;
    private MediaPlayer mediaPlayer = null;
    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicService = new AppleMusic();


        initViews();
        initEvents();
    }

    private void initViews() {
        txtName = findViewById(R.id.txtName);
        btnSearch = findViewById(R.id.btnSearch);
        lblArtistName = findViewById(R.id.lblArtitsName);
        livTracks = findViewById(R.id.livTracks);

        livTracks.setOnItemClickListener((adapterView, view, pos, l) -> {

            Result result = root.getResults().get(pos);
            String filename = result.getLocalPreviewFilename();
            String cacheDir = getApplicationContext().getCacheDir().getAbsolutePath();
            String fullFilename = cacheDir + "/" + filename;

            TrackAdapter trackAdapter = new TrackAdapter(getApplicationContext(), root.getResults());
            trackAdapter.viewHolder = new TrackAdapter.ViewHolder(view);
            trackAdapter.viewHolder.button.setImageResource(R.drawable.pause_button_svgrepo_com);

            trackAdapter.viewHolder.button.setOnClickListener(view1 -> {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    trackAdapter.viewHolder.button.setImageResource(R.drawable.play_button_svgrepo_com_1_);
                }else if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    trackAdapter.viewHolder.button.setImageResource(R.drawable.pause_button_svgrepo_com);
                }
            });

            stopTrack();
            if(new File(fullFilename).exists()) {
                playTrack(fullFilename, trackAdapter);
                return;
            }

            musicService.downloadPreviewTrack(result, cacheDir,  () -> {
                runOnUiThread(() -> {
                    playTrack(fullFilename, trackAdapter);
                });
            }, ((errorCode, text) -> {

            }));
        });

    }

    private void updateTrackList(List<Result> results){
        TrackAdapter trackAdapter = new TrackAdapter(getApplicationContext(), results);
        livTracks.setAdapter(trackAdapter);
        lblArtistName.setText(txtName.getText().toString().toUpperCase());
    }

    public void stopTrack(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playTrack(String fullFilename, TrackAdapter trackAdapter){
        stopTrack();
        trackAdapter.viewHolder.button.setImageResource(R.drawable.pause_button_svgrepo_com);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(fullFilename));
        System.out.println(mediaPlayer);
        mediaPlayer.start();
    }

    private void initEvents(){

        btnSearch.setOnClickListener(view -> {
            String term = txtName.getText().toString().trim();

            stopTrack();

            musicService.requestSongByTerm(term, 50, (root -> {
                String cacheDir = getApplicationContext().getCacheDir().getAbsolutePath();

                this.root = root;

                musicService.downloadArtworks(root, cacheDir);
                System.out.println(root.getResults());

                runOnUiThread(() -> {
                    updateTrackList(root.getResults());
                });
            }), ((errorCode, text) -> {
                runOnUiThread(() -> {

                });
                    })
            );
        });
    }
}