package com.example.odishawarrior.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class LoginFragment extends Fragment {

    private TextView forgetPasswordTv, signUpBtn;
    private FrameLayout parentFrameLayout;
    private EditText emailEt, passEt;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        forgetPasswordTv = view.findViewById(R.id.forgotPassword);
        signUpBtn = view.findViewById(R.id.signUp_loginTv);
        emailEt = view.findViewById(R.id.emailEt);
        passEt = view.findViewById(R.id.passEt);
        loginBtn = view.findViewById(R.id.signup_signUpBtn);
        progressBar = view.findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentFrameLayout = getActivity().findViewById(R.id.frameLayoutAuth);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passEt.getText().toString().trim();

                if(!email.equals("")){
                    if(!password.equals("")){
                        //Authenticate the user

                        loginBtn.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        loginBtn.setText("");

                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                                }
                                else{
                                    loginBtn.setEnabled(true);
                                    Toast.makeText(getContext(), "An error occurred. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                                loginBtn.setText("LOG IN");

                            }
                        });

                    }
                    else{
                        passEt.setError("Please enter your password");
                    }
                }
                else{
                    emailEt.setError("Please enter your Email address");
                }
            }
        });

        forgetPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationActivity.isLoginFragment = false;
                changeFragment(new ForgotPasswordFragment());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationActivity.isLoginFragment = false;
                changeFragment(new SignUpFragment());
            }
        });


    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_anim, R.anim.slide_out_anim);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}