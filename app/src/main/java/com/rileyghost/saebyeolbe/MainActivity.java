package com.rileyghost.saebyeolbe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    private  VideoView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mVideoView = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.gg);

        mVideoView.setVideoURI(uri);
        mVideoView.start();
        mVideoView.requestFocus(); // test

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mVideoView.setMediaController(null);
                mVideoView.animate().alpha(1);
                mVideoView.seekTo(0);
                mVideoView.start();

            }
        });

        mVideoView.setMediaController(new MediaController(this){
            public boolean dispatchKeyEvent(KeyEvent event)
            {
                mVideoView.setMediaController(null);
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    ((Activity) getContext()).finish();
                return super.dispatchKeyEvent(event);
            }
        });

    }

    public void btnLoginMain_Click (View v) {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void btnRegisterMain_Click (View v){
        Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        // clean up or send result here
        finish();
    }
}
