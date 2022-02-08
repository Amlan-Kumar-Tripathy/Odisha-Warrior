package com.example.odishawarrior.fragments;

import static com.example.odishawarrior.activities.CourseMaterialActivity.firestore;
import static com.example.odishawarrior.activities.CourseMaterialActivity.subjectTitle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.CourseMaterialActivity;
import com.example.odishawarrior.classes.PurchasedMaterialsAdapter;
import com.example.odishawarrior.classes.UserDetailsVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.notesRecyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        firestore.collection("PURCHASEDPRODUCTS").document(CourseMaterialActivity.productId)
                .collection(subjectTitle.toUpperCase()).document("notes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    List<String> titleList = (List<String>) task.getResult().get("title_list");
                    List<String> urlList = (List<String>) task.getResult().get("url_list");

                    PurchasedMaterialsAdapter adapter = new PurchasedMaterialsAdapter(titleList, urlList, UserDetailsVariables.NOTES);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}