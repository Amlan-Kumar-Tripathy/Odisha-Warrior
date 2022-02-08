package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.ViewPager2Adapter;
import com.example.odishawarrior.fragments.LecturesFragment;
import com.example.odishawarrior.fragments.NotesFragment;
import com.example.odishawarrior.fragments.TestsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourseMaterialActivity extends AppCompatActivity {

    public static String subjectTitle, productId;

    public static FirebaseFirestore firestore;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_material);

        firestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.course_material_toolbar);

        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        if (getIntent() != null) {
            subjectTitle = getIntent().getStringExtra("subject_title");
            productId = getIntent().getStringExtra("subject_id");
        }

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(subjectTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager2Adapter.fragmentList.clear();
        ViewPager2Adapter.titleList.clear();



        adapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new LecturesFragment(),"Lectures");
        adapter.addFragment(new NotesFragment(),"Notes");
        adapter.addFragment(new TestsFragment(),"Tests");

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(ViewPager2Adapter.titleList.get(position));
            }
        });
        mediator.attach();



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchDATA(){
        if (!subjectTitle.equals("") && !productId.equals("")) {

            firestore.collection("PURCHASEDPRODUCTS").document(productId)
                    .collection(subjectTitle.toUpperCase()).document("lectures").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        String titleList = (String) task.getResult().get("title_list");
                        String urlList = (String) task.getResult().get("url_list");

                    } else {
                        Toast.makeText(CourseMaterialActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

            firestore.collection("PURCHASEDPRODUCTS").document(productId)
                    .collection(subjectTitle.toUpperCase()).document("notes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        String titleList = (String) task.getResult().get("title_list");
                        String urlList = (String) task.getResult().get("url_list");

                    } else {
                        Toast.makeText(CourseMaterialActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

            firestore.collection("PURCHASEDPRODUCTS").document(productId)
                    .collection(subjectTitle.toUpperCase()).document("tests").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        String titleList = (String) task.getResult().get("title_list");
                        String idList = (String) task.getResult().get("id_list");

                    } else {
                        Toast.makeText(CourseMaterialActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }

}