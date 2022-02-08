package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.UserDetailsVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserDetailsActivity extends AppCompatActivity {

    private TextView fullNameTv, mobileTv, oldPasswordTv, newPasswordTv, confirmPasswordTv;
    private Button updateBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_details);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
         TextView dialogText = loadingDialog.findViewById(R.id.loadingDialogTv);
         dialogText.setText("Updating...");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Update Profile");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        fullNameTv = findViewById(R.id.chngAccFullName);
        mobileTv = findViewById(R.id.chngAccPhone);
        oldPasswordTv = findViewById(R.id.chngAccOldPass);
        newPasswordTv = findViewById(R.id.chngAccNewPass);
        confirmPasswordTv = findViewById(R.id.chngAccConfirmPass);
        updateBtn = findViewById(R.id.updateBtn);

        fullNameTv.setText(UserDetailsVariables.userName);
        mobileTv.setText(UserDetailsVariables.userMobile);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.show();

                if(newPasswordTv.getText().toString().equals("")){

                    String updatedName = fullNameTv.getText().toString();
                    String updatedPhone = mobileTv.getText().toString();


                    Map<String, Object> updatedUserDetails = new HashMap<>();
                    updatedUserDetails.put("full_name", updatedName);
                    updatedUserDetails.put("mobile_no", updatedPhone);

                    firestore.collection("USER").document(user.getUid()).update(updatedUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                firestore.collection("USER").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                            Toast.makeText(UpdateUserDetailsActivity.this, "7 "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                        loadingDialog.dismiss();
                                    }
                                });

                                Toast.makeText(UpdateUserDetailsActivity.this, "User profile updated successfully", Toast.LENGTH_LONG).show();
                                finish();

                            }
                            else{

                                Toast.makeText(UpdateUserDetailsActivity.this, "1 "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                            loadingDialog.dismiss();

                        }
                    });
                            //////////////////

                    //////////////////////////////

                }
                else{

                    if(!oldPasswordTv.getText().equals("")){

                        if(newPasswordTv.getText().toString().equals(confirmPasswordTv.getText().toString()) && newPasswordTv.getText().length() >= 6){

                            String email = user.getEmail();

                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, oldPasswordTv.getText().toString());

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        String updatedName = fullNameTv.getText().toString();
                                        String updatedPhone = mobileTv.getText().toString();

                                        Map<String, Object> updatedUserDetails = new HashMap<>();
                                        updatedUserDetails.put("full_name", updatedName);
                                        updatedUserDetails.put("mobile_no", updatedPhone);

                                        user.updatePassword(newPasswordTv.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){
                                                    firestore.collection("USER").document(user.getUid()).update(updatedUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){

                                                                firestore.collection("USER").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                                                                            Toast.makeText(UpdateUserDetailsActivity.this, "Your profile and password was updated successfully! ", Toast.LENGTH_LONG).show();
                                                                            finish();

                                                                        }
                                                                        else{
                                                                            Toast.makeText(UpdateUserDetailsActivity.this, "5 "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                        loadingDialog.dismiss();
                                                                    }
                                                                });
                                                            }
                                                            else{
                                                                Toast.makeText(UpdateUserDetailsActivity.this, "3 "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                loadingDialog.dismiss();
                                                            }
                                                        }
                                                    });
                                                }
                                                else{
                                                    Toast.makeText(UpdateUserDetailsActivity.this, "6 "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    loadingDialog.dismiss();
                                                }


                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(UpdateUserDetailsActivity.this, "4 "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        loadingDialog.dismiss();
                                    }

                                }
                            });

                        }
                        else{
                            Toast.makeText(UpdateUserDetailsActivity.this, "Confirm password does not match !", Toast.LENGTH_LONG).show();
                            loadingDialog.dismiss();
                        }


                    }
                    else{
                        Toast.makeText(UpdateUserDetailsActivity.this, "Enter your Old password ! ", Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                    }


                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        else{
            return false;
        }

        //return super.onOptionsItemSelected(item);
    }
}