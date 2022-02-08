package com.example.odishawarrior.activities;

import static com.example.odishawarrior.MainActivity.currently_present_on_fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CrossProcessCursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.odishawarrior.MainActivity;
import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.UserDetailsVariables;
import com.example.odishawarrior.fragments.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeliveryActivity extends AppCompatActivity {



    private FirebaseUser user;
    private FirebaseFirestore firestore;

    private Dialog myDialog;

    private ConstraintLayout orderSuccessLayout;
    private Button continueToLearnBtn;

    private ImageView productImageIv;
    private TextView productTitleTv, productNormalPriceTv, productSellPriceTv1 , productSellPriceTv2, totalAmountTv1,totalAmountTv2, savedPriceTv;
    private Button continueBtn;

    String productId,productTitle,productSellPrice,productNormalPrice,productRepImage;
    long productType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        orderSuccessLayout = findViewById(R.id.order_success_constraint_layout);
        continueToLearnBtn = findViewById(R.id.order_success_redirect_button);

        productImageIv = findViewById(R.id.product_image);
        productTitleTv = findViewById(R.id.product_title);
        productNormalPriceTv = findViewById(R.id.cutted_price);
        productSellPriceTv1 = findViewById(R.id.product_price);
        productSellPriceTv2 = findViewById(R.id.total_items_price);
        totalAmountTv1 = findViewById(R.id.total_price);
        totalAmountTv2 = findViewById(R.id.delivery_total_amount);
        savedPriceTv = findViewById(R.id.saved_amount);
        continueBtn = findViewById(R.id.cart_continue_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Purchase");

        if(getIntent()!=null){

             productId = getIntent().getStringExtra("product_id_for_dlvry");
            String productTitle = getIntent().getStringExtra("product_title_for_dlvry");
            String productSellPrice = getIntent().getStringExtra("product_sell_price_for_dlvry");
            String productNormalPrice = getIntent().getStringExtra("product_normal_price_for_dlvry");
            String productRepImage = getIntent().getStringExtra("product_image_for_dlvry");
            //productType = getIntent().getStringExtra("product_type_for_dlvry");
            productType = getIntent().getLongExtra("product_type_for_dlvry", -1);
            //Toast.makeText(this, ""+m, Toast.LENGTH_SHORT).show();

            Glide.with(DeliveryActivity.this).load(productRepImage).into(productImageIv);
            productTitleTv.setText(productTitle);
            productNormalPriceTv.setText("Rs. "+convertToCurrencyFormat(productNormalPrice)+"/-");
            productSellPriceTv1.setText("Rs. "+convertToCurrencyFormat(productSellPrice)+"/-");
            productSellPriceTv2.setText("Rs. "+convertToCurrencyFormat(productSellPrice)+"/-");
            totalAmountTv1.setText("Rs. "+convertToCurrencyFormat(productSellPrice)+"/-");
            totalAmountTv2.setText("Rs. "+convertToCurrencyFormat(productSellPrice)+"/-");

            int normal_price = Integer.parseInt(productNormalPrice);
            int sell_price = Integer.parseInt(productSellPrice);
            String saved_price = String.valueOf(normal_price-sell_price);
            savedPriceTv.setText("You saved Rs. "+convertToCurrencyFormat(saved_price)+"/-");



        }
        else{
            finish();
            Toast.makeText(this, "Something went wrong! Please contact our support team.", Toast.LENGTH_SHORT).show();
        }



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //if the payment is successful
                if(user != null) {
                    addTheProductInTheUsersAccount();
                    getSupportActionBar().hide();
                    orderSuccessLayout.setVisibility(View.VISIBLE);

                }
                else{
                    // Dialog directing the user to sign in

                    myDialog = new Dialog(DeliveryActivity.this);
                    myDialog.setContentView(R.layout.login_dialog_layout);
                    myDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
                    myDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    myDialog.setCancelable(true);

                    Button loginBtnFromDialog = myDialog.findViewById(R.id.login_btn_from_dialog);

                    loginBtnFromDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent headToLoginPageIntent = new Intent(DeliveryActivity.this, AuthenticationActivity.class);
                            myDialog.dismiss();
                            startActivity(headToLoginPageIntent);
                            finish();
                        }
                    });

                    myDialog.show();

                }
            }
        });

        continueToLearnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirectToLearnIntent = new Intent(DeliveryActivity.this, MainActivity.class);
                currently_present_on_fragment = (int) productType;
                startActivity(redirectToLearnIntent);
                finish();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==android.R.id.home){
            //finish();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setIcon(R.drawable.youtube_icon_image);
            builder.setTitle("Dialog");
            builder.setMessage("This is a dialog message");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNeutralButton("Thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public static String convertToCurrencyFormat(String s){

        String result = "";
        int len = s.length();
        if(len%2==0){
            s = " " + s;
        }

        int iterator = s.length();

        if(len >= 4){
            result = s.substring(iterator-3);
            int count = (iterator-3)/2;
            iterator = iterator-3;

            while(count>0){
                result = s.substring(iterator-2,iterator) + "," + result;
                iterator = iterator-2;
                count = count -1;
            }

        }
        else{
            result = s;
        }

        return result.trim();
    }

    private void addTheProductInTheUsersAccount(){

        if(productType == UserDetailsVariables.TYPE_COURSES){

            firestore.collection("USER").document(user.getUid().toString()).collection("USER_DATA").document("MY_COURSES")
                    .update("my_purchases", FieldValue.arrayUnion(productId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(DeliveryActivity.this, "Success !", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(DeliveryActivity.this, "Please contact our Customer Support Team. Error is"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else if(productType == UserDetailsVariables.TYPE_NOTES){

            firestore.collection("USER").document(user.getUid().toString()).collection("USER_DATA").document("MY_NOTES")
                    .update("my_purchases", FieldValue.arrayUnion(productId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(DeliveryActivity.this, "Success !", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(DeliveryActivity.this, "Please contact our Customer Support Team. Error is"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else  if(productType == UserDetailsVariables.TYPE_TESTS){
            firestore.collection("USER").document(user.getUid().toString()).collection("USER_DATA").document("MY_TESTS")
                    .update("my_purchases", FieldValue.arrayUnion(productId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(DeliveryActivity.this, "Success !", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(DeliveryActivity.this, "Please contact our Customer Support Team. Error is"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(DeliveryActivity.this, "Could not recognise product category", Toast.LENGTH_LONG).show();
        }




    }



}


