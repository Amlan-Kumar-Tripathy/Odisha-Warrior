package com.example.odishawarrior.fragments;

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

import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.AuthenticationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordFragment extends Fragment {

    private TextView goBackBtn;
    private FrameLayout parentFrameLayout;
    private EditText emailEt;
    private Button resetPasswordBtn;
    private ProgressBar progressBar;
    private FirebaseAuth forgetPassAuth;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forgot_password, container, false);

        goBackBtn = view.findViewById(R.id.goBack_btn);
        emailEt = view.findViewById(R.id.forgotpass_emailEt);
        resetPasswordBtn = view.findViewById(R.id.resetPass_btn);
        progressBar = view.findViewById(R.id.progressBar);
        forgetPassAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentFrameLayout = getActivity().findViewById(R.id.frameLayoutAuth);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailForReset = emailEt.getText().toString().trim();
                if(!emailForReset.equals("")){

                    resetPasswordBtn.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    resetPasswordBtn.setText("");

                    forgetPassAuth.sendPasswordResetEmail(emailForReset).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "A password reset link has been sent successfully to your registered email ID", Toast.LENGTH_LONG).show();
                            }
                            else{

                                Toast.makeText(getContext(), "An error occurred! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                resetPasswordBtn.setEnabled(true);

                            }

                            progressBar.setVisibility(View.INVISIBLE);
                            resetPasswordBtn.setText("RESET PASSWORD");

                        }
                    });

                }
                else{
                    emailEt.setError("Please enter your Registered email ID!");
                }
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationActivity.isLoginFragment = true;
                changeFragment(new LoginFragment());
            }
        });
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_anim,R.anim.slide_out_anim);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}