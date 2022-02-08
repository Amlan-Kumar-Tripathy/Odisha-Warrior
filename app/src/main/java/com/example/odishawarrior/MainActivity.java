package com.example.odishawarrior;


import static com.example.odishawarrior.classes.UserDetailsVariables.TYPE_ACCOUNT;
import static com.example.odishawarrior.classes.UserDetailsVariables.TYPE_NOTIFICATION;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odishawarrior.activities.AuthenticationActivity;
import com.example.odishawarrior.activities.DeliveryActivity;
import com.example.odishawarrior.activities.ProductDetailsActivity;
import com.example.odishawarrior.classes.UserDetailsVariables;
import com.example.odishawarrior.fragments.HomeFragment;
import com.example.odishawarrior.fragments.MyAccountFragment;
import com.example.odishawarrior.fragments.MyCoursesFragment;
import com.example.odishawarrior.fragments.MyNotesFragment;
import com.example.odishawarrior.fragments.MyTestsFragment;
import com.example.odishawarrior.fragments.NotificationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.security.Key;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int currently_present_on_fragment = -1;

    public static Dialog loadingDialog;
    private Dialog loginDialog;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;

    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    public static DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout parentFrameLayout;
    private TextView titleTv;
    private ImageView notificationIconTop;

    private long pressedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        loginDialog = new Dialog(MainActivity.this);
        loginDialog.setContentView(R.layout.login_dialog_layout);
        loginDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        loginDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loginDialog.setCancelable(true);

        Button loginBtnFromDialog = loginDialog.findViewById(R.id.login_btn_from_dialog);
        loginBtnFromDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent headToLoginPageIntent = new Intent(MainActivity.this, AuthenticationActivity.class);
                loginDialog.dismiss();
                startActivity(headToLoginPageIntent);
                finish();
            }
        });

        //Dialog starts here

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        //Dialog ends here

        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.activity_main_toolbar);
        drawerLayout = findViewById(R.id.mainDrawerLayout);
        parentFrameLayout = findViewById(R.id.activity_main_framelayout);
        titleTv = findViewById(R.id.activity_main_title);
        notificationIconTop = findViewById(R.id.notificationIconTop);

        if(currently_present_on_fragment == -1) {
            changeFragmentAndTitle(new HomeFragment(), "Odisha Warrior");

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_COURSES){

            changeFragmentAndTitle(new MyCoursesFragment(), "My Courses");
            loadingDialog.dismiss();

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_NOTES){
            changeFragmentAndTitle(new MyNotesFragment(), "My Notes");
            loadingDialog.dismiss();
        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_TESTS){
            changeFragmentAndTitle(new MyTestsFragment(), "My Tests");
            loadingDialog.dismiss();
        }
        else if(currently_present_on_fragment == TYPE_ACCOUNT){
            changeFragmentAndTitle(new MyAccountFragment(), "My Account");
            loadingDialog.dismiss();
        }
        else if(currently_present_on_fragment == TYPE_NOTIFICATION){
            changeFragmentAndTitle(new NotificationsFragment(), "My Notifications");
            loadingDialog.dismiss();
        }



        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            int menuItem;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menuItem = item.getItemId();

                drawerLayout.closeDrawer(GravityCompat.START);

                if (currentUser == null) {

                    //Toast.makeText(MainActivity.this, "Please login to use this feature", Toast.LENGTH_LONG).show();



                    loginDialog.show();


                } else {

                    drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);

                       /* if(menuItem == R.id.nav_share){
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            //whatsappIntent.setPackage("com.whatsapp");
                            whatsappIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey, I have come across this wonderful app!");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "\n\nhttps://play.google.com/store/apps/details?id=com.example.odishawarrior\n");
                            try {
                                startActivity(whatsappIntent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(MainActivity.this, "WhatsApp is not installed!", Toast.LENGTH_SHORT).show();

                            }
                        }else if(menuItem == R.id.nav_helpline){
                            Intent phoneintent = new Intent(Intent.ACTION_DIAL);
                            phoneintent.setData(Uri.parse("tel:0123456789"));
                            startActivity(phoneintent);
                        }else if(menuItem == R.id.nav_ytchannel){
                            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "UvUyL48sRZ4"));
                            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.youtube.com/watch?v=UvUyL48sRZ4"));
                            try {
                                startActivity(appIntent);
                            } catch (ActivityNotFoundException ex) {
                                startActivity(webIntent);
                            }
                        }
                        else if(menuItem == R.id.nav_telegram){
                            try{
                                PackageManager pm = getApplicationContext().getPackageManager();
                                pm.getPackageInfo("org.telegram.messenger", 0);
                                Intent telegramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/XU8qbbnqJ_g1ZDM9"));
                                startActivity(telegramIntent);
                            }catch(Exception e){
                                Toast.makeText(MainActivity.this, "Telegram App is not installed!", Toast.LENGTH_LONG).show();
                            }
                        }else if(menuItem == R.id.nav_aboutUs){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                            startActivity(browserIntent);
                        }*/

                        switch (menuItem) {

                            case R.id.nav_home:
                                changeFragmentAndTitle(new HomeFragment(), "Odisha Warrior");
                                break;
                            case R.id.nav_myCourses:
                                changeFragmentAndTitle(new MyCoursesFragment(), "My Courses");
                                break;
                            case R.id.nav_mynotes:
                                changeFragmentAndTitle(new MyNotesFragment(), "My Notes");
                                break;
                            case R.id.nav_mytests:
                                changeFragmentAndTitle(new MyTestsFragment(), "My Tests");
                                break;
                            case R.id.nav_myaccount:
                                changeFragmentAndTitle(new MyAccountFragment(), "My Account");
                                break;
                            case R.id.nav_notifications:
                                changeFragmentAndTitle(new NotificationsFragment(), "My Notifications");
                                break;

                            case R.id.nav_share:
                                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                whatsappIntent.setType("text/plain");
                                //whatsappIntent.setPackage("com.whatsapp");
                                whatsappIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey, I have come across this wonderful app!");
                                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "\n\nhttps://play.google.com/store/apps/details?id=com.example.odishawarrior\n");
                                try {
                                    startActivity(whatsappIntent);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(MainActivity.this, "WhatsApp is not installed!", Toast.LENGTH_SHORT).show();

                                }
                                break;

                            case R.id.nav_helpline:
                                Intent phoneintent = new Intent(Intent.ACTION_DIAL);
                                phoneintent.setData(Uri.parse("tel:0123456789"));
                                startActivity(phoneintent);

                                break;

                            case R.id.nav_ytchannel:
                                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "UvUyL48sRZ4"));
                                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://www.youtube.com/watch?v=UvUyL48sRZ4"));
                                try {
                                    startActivity(appIntent);
                                } catch (ActivityNotFoundException ex) {
                                    startActivity(webIntent);
                                }
                                break;

                            case R.id.nav_telegram:

                                try {
                                    PackageManager pm = getApplicationContext().getPackageManager();
                                    pm.getPackageInfo("org.telegram.messenger", 0);
                                    Intent telegramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/XU8qbbnqJ_g1ZDM9"));
                                    startActivity(telegramIntent);
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, "Telegram App is not installed!", Toast.LENGTH_LONG).show();
                                }

                                break;

                            case R.id.nav_aboutUs:
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                                startActivity(browserIntent);
                                break;

                            case R.id.nav_logout:

                                FirebaseAuth.getInstance().signOut();

                                //todo: clear all data of the user

                                Intent signOutIntent = new Intent(MainActivity.this, AuthenticationActivity.class);
                                startActivity(signOutIntent);
                                finish();

                                break;

                        }

                        menuItem = 0;
                    }
                });

            }

                return true;
            }
        });


        notificationIconTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                   // Toast.makeText(MainActivity.this, "Please login to use this feature", Toast.LENGTH_LONG).show();
                    loginDialog.show();
                }
                else{
                    triggerNotification();
                    changeFragmentAndTitle(new NotificationsFragment(), "My Notifications");
                }

            }
        });

        SharedPreferences sh = getSharedPreferences("UserBasics", Context.MODE_PRIVATE);

        if(sh.contains("user_name") && sh.contains("user_email") && sh.contains("user_mobile")){

            UserDetailsVariables.userName = sh.getString("user_name", "");
            UserDetailsVariables.userEmail = sh.getString("user_email", "");
            UserDetailsVariables.userMobile = sh.getString("user_mobile", "");
            //UserDetailsVariables.userProfileImage = sh.getString("user_profile_pic", "");

        }
        else {

            firestore.collection("USER").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String fullName = (String) task.getResult().get("full_name");
                        String emailId = (String) task.getResult().get("email_ID");
                        String mobileNo = (String) task.getResult().get("mobile_no");

                        SharedPreferences sharedPreferences = getSharedPreferences("UserBasics", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_name", fullName);
                        editor.putString("user_email", emailId);
                        editor.putString("user_mobile", mobileNo);
                        editor.commit();

                        UserDetailsVariables.userName = sharedPreferences.getString("user_name", "");
                        UserDetailsVariables.userEmail = sharedPreferences.getString("user_email", "");
                        UserDetailsVariables.userMobile = sharedPreferences.getString("user_mobile", "");

                    }
                    else{

                    }
                }
            });

        }



    }

    private void triggerNotification(){

        Random random = new Random();
        int id = random.nextInt(9999);

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product_id","ODWR001");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTI001")
                .setSmallIcon(R.drawable.home_icon_image)
                .setColor(243)
                .setContentTitle("The wait is over.....")
                .setContentText("The much awaited product is out... ")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("The much awaited product is out...more dynamic and vibrant than before. Much longer text that cannot fit one line...Check it out"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "getString(R.string.channel_name)";
            String description = "getString(R.string.channel_description)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NOTI001", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void changeFragmentAndTitle(Fragment fragment, String title){
        titleTv.setText(title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        transaction.replace(parentFrameLayout.getId(),fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(currently_present_on_fragment == -1){
                return super.onKeyDown(keyCode, event);
            }
            else{
                changeFragmentAndTitle(new HomeFragment(), "Odisha Warrior");
            }
        }

        return false;

    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(currently_present_on_fragment == -1) {
            changeFragmentAndTitle(new HomeFragment(), "Odisha Warrior");

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_COURSES){

            changeFragmentAndTitle(new MyCoursesFragment(), "My Courses");
            loadingDialog.dismiss();

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_NOTES){
            changeFragmentAndTitle(new MyNotesFragment(), "My Notes");
            loadingDialog.dismiss();
        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_TESTS){
            changeFragmentAndTitle(new MyTestsFragment(), "My Tests");
            loadingDialog.dismiss();
        }else if(currently_present_on_fragment == TYPE_ACCOUNT){
            changeFragmentAndTitle(new MyAccountFragment(), "My Account");
            loadingDialog.dismiss();
        }else if(currently_present_on_fragment == TYPE_NOTIFICATION){
            changeFragmentAndTitle(new NotificationsFragment(), "My Notifications");
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currently_present_on_fragment == -1) {
            changeFragmentAndTitle(new HomeFragment(), "Odisha Warrior");

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_COURSES){

            changeFragmentAndTitle(new MyCoursesFragment(), "My Courses");
            loadingDialog.dismiss();

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_NOTES){
            changeFragmentAndTitle(new MyNotesFragment(), "My Notes");
            loadingDialog.dismiss();
        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_TESTS){
            changeFragmentAndTitle(new MyTestsFragment(), "My Tests");
            loadingDialog.dismiss();
        }else if(currently_present_on_fragment == TYPE_ACCOUNT){
            changeFragmentAndTitle(new MyAccountFragment(), "My Account");
            loadingDialog.dismiss();
        }else if(currently_present_on_fragment == TYPE_NOTIFICATION){
            changeFragmentAndTitle(new NotificationsFragment(), "My Notifications");
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(currently_present_on_fragment == -1) {
            changeFragmentAndTitle(new HomeFragment(), "Odisha Warrior");

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_COURSES){

            changeFragmentAndTitle(new MyCoursesFragment(), "My Courses");
            loadingDialog.dismiss();

        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_NOTES){
            changeFragmentAndTitle(new MyNotesFragment(), "My Notes");
            loadingDialog.dismiss();
        }
        else if(currently_present_on_fragment == UserDetailsVariables.TYPE_TESTS){
            changeFragmentAndTitle(new MyTestsFragment(), "My Tests");
            loadingDialog.dismiss();
        }else if(currently_present_on_fragment == TYPE_ACCOUNT){
            changeFragmentAndTitle(new MyAccountFragment(), "My Account");
            loadingDialog.dismiss();
        }else if(currently_present_on_fragment == TYPE_NOTIFICATION){
            changeFragmentAndTitle(new NotificationsFragment(), "My Notifications");
            loadingDialog.dismiss();
        }
    }

}