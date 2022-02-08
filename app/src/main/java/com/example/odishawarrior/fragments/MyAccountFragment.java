package com.example.odishawarrior.fragments;

import static android.app.Activity.RESULT_OK;
import static com.example.odishawarrior.classes.UserDetailsVariables.TYPE_ACCOUNT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.widget.TooltipCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapImageDecoderResourceDecoder;
import com.example.odishawarrior.MainActivity;
import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.AuthenticationActivity;
import com.example.odishawarrior.activities.UpdateUserDetailsActivity;
import com.example.odishawarrior.classes.UserDetailsVariables;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class MyAccountFragment extends Fragment {

    private CardView cardView;

    private TextView fullNameTv, emailTv, mobileNoTv;
    private Button logOutBtn;
    private FloatingActionButton settingBtn;
    private ImageView cameraBtn;
    private ImageView userProfileImage;

    private static final int pic_id = 101;
    private ActivityResultLauncher<String> launchGalleryImageChooserActivity;
    private ActivityResultLauncher<Intent> launchCameraActivity;
//    public static Uri profile_image;
//    public static Bitmap bitmap_profile_image;
    public static Object profile_image;
    private BottomSheetDialog imageChooserDialog;

    private SharedPreferences sh;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_account, container, false);

        MainActivity.currently_present_on_fragment = TYPE_ACCOUNT;

        sh = getContext().getSharedPreferences("UserBasics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();

        fullNameTv = view.findViewById(R.id.accountFullName);
        emailTv = view.findViewById(R.id.accountEmailId);
        mobileNoTv = view.findViewById(R.id.accountPhoneNo);
        logOutBtn = view.findViewById(R.id.accountLogOut);
        settingBtn = view.findViewById(R.id.floatingActionButton);
        cardView = view.findViewById(R.id.userProfileImageCardView);

        cameraBtn = view.findViewById(R.id.cameraBtn);
        userProfileImage = view.findViewById(R.id.userProfileImage);

        launchGalleryImageChooserActivity = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null){

                    profile_image = (Uri) result;

                    Glide.with(getContext()).load(profile_image).into(userProfileImage);

                    editor.putString("profileImage", result.toString());
                    editor.commit();

                    Toast.makeText(getContext(), " " +result, Toast.LENGTH_SHORT).show();

                }

            }
        });

         launchCameraActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                            profile_image = (Bitmap) result.getData().getExtras().get("data");

                            Glide.with(getContext()).load(profile_image).into(userProfileImage);

                            editor.putString("profileImage", profile_image.toString());
                            editor.commit();

                        }
                    }
                });


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startUserProfileImageSettingFunctionality();

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView.setTooltipText("Put your nice pic!");

        createImageChooserDialog();

        if(sh.contains("profileImage")){
            Toast.makeText(getContext(), "" + sh.getString("profileImage", ""), Toast.LENGTH_SHORT).show();

            Glide.with(getContext()).load(Uri.parse(sh.getString("profileImage", ""))).placeholder(R.drawable.account_icon).into(userProfileImage);

        }else{
            Toast.makeText(getContext(), "no shared preferences", Toast.LENGTH_SHORT).show();

        }
        fullNameTv.setText(UserDetailsVariables.userName);
        emailTv.setText(UserDetailsVariables.userEmail);
        mobileNoTv.setText(UserDetailsVariables.userMobile);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateUserDetailsActivity.class);
                startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                //todo: clear all data of the user

                Intent signOutIntent = new Intent(getContext(), AuthenticationActivity.class);
            }
        });





    }

    private void createImageChooserDialog(){
        imageChooserDialog = new BottomSheetDialog(getContext());
        imageChooserDialog.setContentView(R.layout.image_chooser_layout_bottom_sheet_dialog);
        imageChooserDialog.setDismissWithAnimation(true);

        imageChooserDialog.findViewById(R.id.cameraActivityLauncherIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserProfileImageSettingFunctionalityFromCamera();
            }
        });

        imageChooserDialog.findViewById(R.id.galleryActivityLauncherIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserProfileImageSettingFunctionalityFromGallery();
            }
        });
    }

    private void startUserProfileImageSettingFunctionality(){

        imageChooserDialog.show();
    }

    private void startUserProfileImageSettingFunctionalityFromGallery(){
        imageChooserDialog.dismiss();
        launchGalleryImageChooserActivity.launch("image/*");
    }

    private void startUserProfileImageSettingFunctionalityFromCamera(){
        imageChooserDialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launchCameraActivity.launch(intent);
    }



}