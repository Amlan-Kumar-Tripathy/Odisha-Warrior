package com.example.odishawarrior.activities;

import static com.example.odishawarrior.classes.HomePageModel.BANNER;
import static com.example.odishawarrior.classes.HomePageModel.GRID;
import static com.example.odishawarrior.classes.HomePageModel.HORIZONTAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.HomePageAdapter;
import com.example.odishawarrior.classes.HomePageModel;
import com.example.odishawarrior.classes.HorizontalItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private Dialog catDialog;

    FirebaseFirestore firestore;

    private RecyclerView categoryRV;

    private List<HomePageModel> categoryList;

    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        catDialog = new Dialog(this);
        catDialog.setContentView(R.layout.loading_dialog);
        catDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        catDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        catDialog.setCancelable(false);
        catDialog.show();

        firestore = FirebaseFirestore.getInstance();

        categoryRV = findViewById(R.id.categoryRecyclerView);

        categoryList = new ArrayList<>();

        categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        categoryRV.setLayoutManager(manager);

        getCategoriesDataFromFirebase(categoryName);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCategoriesDataFromFirebase(String categoryName){

        firestore.collection("CATEGORIES").document(categoryName.toUpperCase()).collection("TODISPLAY").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){

                   for(DocumentSnapshot shot: task.getResult()){

                       long layoutType = (long) shot.get("layout_type");

                       if(layoutType == BANNER){

                           List<String> posters_url = (List<String>) shot.get("banner_images");
                           categoryList.add(new HomePageModel(BANNER,posters_url));

                       }
                       else if(layoutType == HORIZONTAL){

                           String layoutTitle = (String) shot.get("layout_title");
                           List<String> product_list = (List<String>) shot.get("product_list");

                           categoryList.add(new HomePageModel(HORIZONTAL,product_list,layoutTitle));

                       }
                       else if(layoutType == GRID){

                           String layoutTitle = (String) shot.get("layout_title");
                           List<String> product_list = (List<String>) shot.get("product_list");
                           String bgColor = (String) shot.get("bg_color");

                           categoryList.add(new HomePageModel(GRID,product_list,layoutTitle,bgColor));

                       }
                       else {

                       }

                   }

                   Handler handler = new Handler();
                   Runnable runnable = new Runnable() {
                       @Override
                       public void run() {
                           catDialog.dismiss();
                           HomePageAdapter adapter = new HomePageAdapter(categoryList);
                           categoryRV.setAdapter(adapter);
                           adapter.notifyDataSetChanged();
                       }
                   };
                   handler.postDelayed(runnable,5000);

//                   HomePageAdapter adapter = new HomePageAdapter(categoryList);
//                           categoryRV.setAdapter(adapter);
//                           adapter.notifyDataSetChanged();

               }
               else{
                   Toast.makeText(CategoriesActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        });

    }
}