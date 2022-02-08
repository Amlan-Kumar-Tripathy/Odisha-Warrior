package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.SubjectsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CourseSubjectsActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private Dialog loadingDialog;
    private RecyclerView recyclerView;

    private String productTitle, productId;
    private int productType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_subjects);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        TextView loadingDialogMsg = loadingDialog.findViewById(R.id.loadingDialogTv);
        loadingDialogMsg.setText("");
        loadingDialog.show();

        if(getIntent() != null){
            productTitle = getIntent().getStringExtra("product_title");
            productId = getIntent().getStringExtra("product_id");
            productType = getIntent().getIntExtra("product_type", 0);
        }

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(productTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.courses_recycler_view);

        int noOfColumns = 2;
        GridLayoutManager manager = new GridLayoutManager(this,noOfColumns);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        firestore.collection("PURCHASEDPRODUCTS").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    List<String> subjectsList = (List<String>) task.getResult().get("subjects_list");

                    SubjectsAdapter adapter = new SubjectsAdapter(subjectsList, productId);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }
                else {

                    Toast.makeText(CourseSubjectsActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();

                }
                loadingDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

            return super.onOptionsItemSelected(item);
    }
}