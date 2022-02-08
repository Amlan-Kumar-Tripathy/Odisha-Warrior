package com.example.odishawarrior.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.odishawarrior.MainActivity;
import com.example.odishawarrior.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private Animation topAnim, bottomAnim;
    private ImageView logoIv;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoIv = findViewById(R.id.logoIv);
        titleTv = findViewById(R.id.logoTv);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.logo_image_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.logo_text_animation);

        logoIv.setAnimation(topAnim);
        titleTv.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                if(auth.getCurrentUser()==null){
                    Intent intent = new Intent(SplashActivity.this, AuthenticationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }



            }
        },3000);

    }
}