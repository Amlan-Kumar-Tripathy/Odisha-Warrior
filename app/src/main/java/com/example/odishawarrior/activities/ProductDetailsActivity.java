package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.ProductImageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;



public class ProductDetailsActivity extends AppCompatActivity {

    //Views Variables declaration


    private TextView titleTv,subtitleTv, sellPriceTv, normalPriceTv, descriptionTv, avgRatingTvAbove, avgRatingTvBelow,
                        totalRatingsTvAbove, totalRatingsTvMiddle,totalRatingsTvBelow,
                        star5Tv,star4Tv,star3Tv,star2Tv,star1Tv;
    private Button buyNowBtn;




    //End of VVD

    //Some more variable declaration
    private String title,subtitle, sellPrice, normalPrice, description, avgRating,
            totalRatings;
    private String productId, productTitle;
    private String productRepImage;
    private List<String> productImagesList;
    private Long star5,star4,star3,star2,star1, productType;
    private List<Long> individual_rating = new ArrayList<Long>();

    //


    private ViewPager pager;
    private ProductImageAdapter adapter;
    private TabLayout viewPageTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //Views Variables initialization
        productImagesList = new ArrayList<>();


        titleTv = findViewById(R.id.productTitle);
        subtitleTv = findViewById(R.id.productSubTitle);
        avgRatingTvAbove = findViewById(R.id.totalAverageRatingTv);
        totalRatingsTvAbove = findViewById(R.id.totalRatingTv);
        sellPriceTv = findViewById(R.id.sellPriceTv);
        normalPriceTv = findViewById(R.id.normalPriceTv);
        descriptionTv = findViewById(R.id.elaboratedDetailsTv);
        avgRatingTvBelow = findViewById(R.id.totalAverageRatingsBottomTv);
        totalRatingsTvMiddle = findViewById(R.id.totalRatingBottomTv);
        star5Tv = findViewById(R.id.star5totalRatingTv);
        star4Tv = findViewById(R.id.star4totalRatingTv);
        star3Tv = findViewById(R.id.star3totalRatingTv);
        star2Tv = findViewById(R.id.star2totalRatingTv);
        star1Tv = findViewById(R.id.star1totalRatingTv);
        totalRatingsTvBelow = findViewById(R.id.totalRatingsBottomTv);

        buyNowBtn = findViewById(R.id.buyNowBtn);
        //

        if(getIntent()!=null) {
            productId = getIntent().getStringExtra("product_id");
            productTitle = getIntent().getStringExtra("product_title");
        }

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(productTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pager = findViewById(R.id.productImageViewPager);
        viewPageTabs = findViewById(R.id.viewPageTabs);

       /* productImagesList.add("https://img.jagranjosh.com/imported/images/E/Articles/WBJEE_Thermodynamics_2018_media.jpg");
        productImagesList.add("https://images.squarespace-cdn.com/content/v1/5269fbd3e4b0eb2b76ccc1db/1602545756734-56BYDW1SMLCM9WIEQO40/thermodynamics-mcat.png");
        productImagesList.add("https://1lw1tk46gr9aum2ilzzghvk5-wpengine.netdna-ssl.com/wp-content/uploads/2021/04/shutterstock_379063504-scaled.jpg");*/

        //Fetching data from Database

                                                                            // TODO: pass a product ID here

        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot shot = task.getResult();

                    //Variable declaration and initialisation




                     productImagesList = (List<String>) shot.get("product_images");
                     productRepImage = (String) shot.get("product_representative_image");
                     title = (String) shot.get("product_title");
                     subtitle = (String) shot.get("product_subtitle");
                     normalPrice = (String) shot.get("normal_price");
                     sellPrice = (String) shot.get("sell_price");
                     avgRating = (String) shot.get("average_rating");
                     totalRatings = (String) shot.get("total_ratings");
                     description = (String) shot.get("product_description");
                     productType = (Long) shot.get("product_type");

                    /**/

                     individual_rating = (List<Long>) shot.get("individual_ratings");
                     star1 = individual_rating.get(0);
                     star2 = individual_rating.get(1);
                     star3 = individual_rating.get(2);
                     star4 = individual_rating.get(3);
                     star5 = individual_rating.get(4);

                    //EO Variable declaration

                    //Assigning them to the views

                    titleTv.setText(title);
                    subtitleTv.setText(subtitle);
                    avgRatingTvAbove.setText(avgRating);
                    totalRatingsTvAbove.setText("("+totalRatings+")" + " Ratings");
                    sellPriceTv.setText("Rs. "+sellPrice+"/-");
                    normalPriceTv.setText("Rs. "+normalPrice+"/-");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        descriptionTv.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
                    } else
                        descriptionTv.setText(Html.fromHtml(description));

                    avgRatingTvBelow.setText(avgRating);
                    totalRatingsTvMiddle.setText("("+totalRatings+")" + " Ratings");

                   star5Tv.setText(""+star5);
                    star4Tv.setText(""+star4);
                    star3Tv.setText(""+star3);
                    star2Tv.setText(""+star2);
                    star1Tv.setText(""+star1);

                    totalRatingsTvBelow.setText(totalRatings);
                    //

                    //
                    adapter = new ProductImageAdapter(productImagesList);
                    pager.setAdapter(adapter);
                    viewPageTabs.setupWithViewPager(pager,true);
                    //

                }
                else{
                    Toast.makeText(ProductDetailsActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent deliveryIntent = new Intent(ProductDetailsActivity.this,DeliveryActivity.class);

                deliveryIntent.putExtra("product_id_for_dlvry",productId);
                deliveryIntent.putExtra("product_title_for_dlvry",title);
                deliveryIntent.putExtra("product_sell_price_for_dlvry",sellPrice);
                deliveryIntent.putExtra("product_normal_price_for_dlvry",normalPrice);
                deliveryIntent.putExtra("product_type_for_dlvry",productType);
                deliveryIntent.putExtra("product_image_for_dlvry",productRepImage);


                startActivity(deliveryIntent);

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}