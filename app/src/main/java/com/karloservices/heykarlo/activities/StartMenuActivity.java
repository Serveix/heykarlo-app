package com.karloservices.heykarlo.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.karloservices.heykarlo.R;

public class StartMenuActivity extends AppCompatActivity {
    private VideoView bgVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        bgVideoView = (VideoView) findViewById(R.id.bgVideoView);

        Uri uriBgVideo = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cityatnightvideo);

        bgVideoView.setVideoURI(uriBgVideo);
        bgVideoView.start();
        bgVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }

    public void sendLoginForm(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void sendRegisterForm(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
