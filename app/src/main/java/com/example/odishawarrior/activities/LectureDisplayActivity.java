package com.example.odishawarrior.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.odishawarrior.R;

public class LectureDisplayActivity extends Activity {

    private String lectureTitle, url;
    private VideoView videoView;
    MediaController mediaControls;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_display);

        videoView = findViewById(R.id.lecturevideoview);
        progressBar = findViewById(R.id.progressBar8);

        if(getIntent() != null){
            lectureTitle = getIntent().getStringExtra("title");
            url = getIntent().getStringExtra("idUrl");

            if(url.length() == 11){

            }
            else{

               // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.samplevideo);
                 // initiate a video view
                if (mediaControls == null) {
                    // create an object of media controller class
                    mediaControls = new MediaController(LectureDisplayActivity.this);
                    mediaControls.setAnchorView(videoView);
                }
               videoView.setMediaController(mediaControls);
                videoView.setVideoURI(Uri.parse(url));
                videoView.start();

                progressBar.setVisibility(View.VISIBLE);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                            @Override
                            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                                progressBar.setProgress(videoView.getBufferPercentage());
                            }
                        });

                        mp.start();
                        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                progressBar.setVisibility(View.GONE);
                                mp.start();
                            }
                        });
                    }
                });

                // implement on completion listener on video view
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        Toast.makeText(getApplicationContext(), "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
                    }
                });
                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                        return false;
                    }
                });


            }

        }
    }
}