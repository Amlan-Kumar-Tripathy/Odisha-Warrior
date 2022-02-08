package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.PurchasedNotesPDFAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchasedNotesDisplayActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private RecyclerView recyclerView;

    private String productId,productTitle;
    private int productType;
    private List<String> topicsList;
    private List<String> topicsUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_notes_display);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.notesPDFrv);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        if(getIntent() != null) {
            productId = getIntent().getStringExtra("product_id");
            productTitle = getIntent().getStringExtra("product_title");
            productType = getIntent().getIntExtra("product_type", 0);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(productTitle);


            firestore.collection("PURCHASEDPRODUCTS").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){


                        topicsList = new ArrayList<>();
                        topicsUrlList = new ArrayList<>();

                        topicsList = (List<String>) task.getResult().get("subjects_list");
                        topicsUrlList = (List<String>) task.getResult().get("notes_url_list");

                    }
                    else{
                        Toast.makeText(PurchasedNotesDisplayActivity.this, ""+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    PurchasedNotesPDFAdapter adapter = new PurchasedNotesPDFAdapter(topicsList, topicsUrlList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}