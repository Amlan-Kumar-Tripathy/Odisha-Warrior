package com.example.odishawarrior.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.example.odishawarrior.R;
import com.example.odishawarrior.fragments.LoginFragment;
import com.example.odishawarrior.fragments.SignUpFragment;

public class AuthenticationActivity extends AppCompatActivity {

    private FrameLayout framelayout;
    public static boolean isLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        framelayout = findViewById(R.id.frameLayoutAuth);

        changeFragment(new LoginFragment());
        isLoginFragment = true;

    }

    /*private void setDefaultFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(framelayout.getId(), fragment);
        fragmentTransaction.commit();
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isLoginFragment){
                finish();
            }else{
                isLoginFragment = true;
                changeFragment(new LoginFragment());
            }
        }

        return false;
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_anim, R.anim.slide_out_anim);
        fragmentTransaction.replace(framelayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}