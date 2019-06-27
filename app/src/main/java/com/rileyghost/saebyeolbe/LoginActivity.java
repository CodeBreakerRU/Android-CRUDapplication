package com.rileyghost.saebyeolbe;

import android.app.Activity;
import android.content.Intent;

import android.media.MediaPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private  EditText txtEmailLogin;
    private  EditText txtPwd;
    private  FirebaseAuth firebaseAuth;

    private  VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txtEmailLogin = (EditText) findViewById(R.id.txtEmailLogin);
        txtPwd = (EditText) findViewById(R.id.txtPasswordLogin);
        firebaseAuth = FirebaseAuth.getInstance();


        mVideoView = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video1);

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



    public void btnUserLogin_Click (View v) {

        if (txtEmailLogin.length() == 0)
        {
            txtEmailLogin.setError("Enter your email address");
        }
        else if (txtPwd.length() == 0)
        {
            txtPwd.setError("Enter your password");
        }

        else {

            // ProgressDialog

            try {
                firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(), txtPwd.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // ProgressDialog dismiss

                                if (task.isSuccessful()) {
                                    toastMessage("Successfully logged in with: " + firebaseAuth.getCurrentUser().getEmail());
                                    //Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());

                                    startActivity(i);
                                } else {
                                    Log.e("Error", task.getException().toString());
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            } catch (Exception e) {

                //e.printStackTrace();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }


        }


    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();

    }
}

