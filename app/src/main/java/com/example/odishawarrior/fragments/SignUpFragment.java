package com.example.odishawarrior.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odishawarrior.MainActivity;
import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.AuthenticationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {

    private TextView loginBtn;
    private ImageView crossBtn;
    private FrameLayout parentFrameLayout;
    private EditText emailEt, passwordEt, cnfPassEt, fullNameEt, mobileEt;
    private Button signUpBtn;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        loginBtn = view.findViewById(R.id.signUp_loginTv);
        crossBtn = view.findViewById(R.id.signup_close_btn);
        emailEt = view.findViewById(R.id.signup_emailEt);
        passwordEt = view.findViewById(R.id.signup_passEt);
        signUpBtn = view.findViewById(R.id.signup_signUpBtn);
        cnfPassEt = view.findViewById(R.id.signup_confirm_passEt);
        fullNameEt = view.findViewById(R.id.fullName);
        mobileEt = view.findViewById(R.id.mobileNoEt);
        progressBar = view.findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentFrameLayout = getActivity().findViewById(R.id.frameLayoutAuth);


        fullNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cnfPassEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(emailEt.getText().toString().matches(emailPattern)){
                    if(cnfPassEt.getText().toString().equals(passwordEt.getText().toString())){

                        signUpBtn.setEnabled(false);
                        signUpBtn.setText("");
                        progressBar.setVisibility(View.VISIBLE);

                        String email = emailEt.getText().toString();
                        String password = passwordEt.getText().toString();

                        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Your account has been created successfully!", Toast.LENGTH_SHORT).show();


                                    //Automatically login the user, after successful sign up
                                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){

                                                //Store the details of the user and create desired folders

                                                Map<String,Object> user_details = new HashMap<>();
                                                user_details.put("full_name", fullNameEt.getText().toString());
                                                user_details.put("email_ID", emailEt.getText().toString());
                                                user_details.put("mobile_no", mobileEt.getText().toString());



                                                firestore.collection("USER").document(firebaseAuth.getUid()).set(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){

                                                            CollectionReference user_data_collections = firestore.collection("USER").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                            List<String> documentNames = new ArrayList<>();
                                                            documentNames.add("MY_RATINGS");
                                                            documentNames.add("MY_COURSES");
                                                            documentNames.add("MY_TESTS");
                                                            documentNames.add("MY_NOTES");

                                                            Map<String,Long> user_data_params = new HashMap<>();
                                                            user_data_params.put("total", 0L);

                                                            for(int i=0;i<documentNames.size();i++){
                                                                user_data_collections.document(documentNames.get(i)).set(user_data_params).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){

                                                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserBasics", Context.MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                            editor.putString("user_name", fullNameEt.getText().toString());
                                                                            editor.putString("user_email", emailEt.getText().toString());
                                                                            editor.putString("user_mobile", mobileEt.getText().toString());
                                                                            editor.commit();

                                                                            Intent intent = new Intent(getContext(),MainActivity.class);
                                                                            startActivity(intent);
                                                                            getActivity().finish();
                                                                        }
                                                                        else{
                                                                            signUpBtn.setText("SIGN UP");
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            firebaseAuth.signOut();
                                                                            Toast.makeText(getContext(), "An error occurred! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                        else{
                                                            signUpBtn.setText("SIGN UP");
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            firebaseAuth.signOut();
                                                            Toast.makeText(getContext(), "An error occurred! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                            }
                                            else{
                                                signUpBtn.setText("SIGN UP");
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getContext(), "An error occurred. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                                else{
                                    signUpBtn.setEnabled(true);
                                    Toast.makeText(getContext(), "An error occurred. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                signUpBtn.setText("SIGN UP");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                            ///


                    }
                    else{
                        cnfPassEt.setError("Password does not match");
                    }

                }else{
                    emailEt.setError("Enter a valid E mail address");
                }



            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationActivity.isLoginFragment = true;
                changeFragment(new LoginFragment());
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_anim,R.anim.slide_out_anim);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){

        if(!fullNameEt.getText().toString().equals("")){
            if(!emailEt.getText().toString().equals("")){
                if(mobileEt.getText().toString().length() == 10){
                    if(passwordEt.getText().toString().length() >= 6){
                       if(!cnfPassEt.getText().toString().equals("")){
                           signUpBtn.setEnabled(true);
                       }
                       else{
                           signUpBtn.setEnabled(false);
                           cnfPassEt.setError("Please enter a password");
                       }
                    }
                    else{
                        signUpBtn.setEnabled(false);
                        passwordEt.setError("Password should consists of a minimum of 6 character");
                    }
                }
                else{
                    signUpBtn.setEnabled(false);
                    mobileEt.setError("Please enter a 10 digit Mobile Number");
                }
            }
            else{
                signUpBtn.setEnabled(false);
                emailEt.setError("Please enter a valid Email Address");
            }
        }
        else{
            signUpBtn.setEnabled(false);
            fullNameEt.setError("Please enter your Full Name");
        }

    }
}