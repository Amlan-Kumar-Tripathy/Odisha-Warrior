package com.example.odishawarrior.fragments;

import static com.example.odishawarrior.activities.CourseMaterialActivity.firestore;
import static com.example.odishawarrior.activities.CourseMaterialActivity.subjectTitle;
import static com.example.odishawarrior.classes.UserDetailsVariables.TYPE_TESTS;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.odishawarrior.MainActivity;
import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.CourseMaterialActivity;
import com.example.odishawarrior.classes.CommonPurchasedItemAdapter;
import com.example.odishawarrior.classes.CommonPurchasedItemModel;
import com.example.odishawarrior.classes.PurchasedMaterialsAdapter;
import com.example.odishawarrior.classes.UserDetailsVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyTestsFragment extends Fragment {

    private Dialog loadingDialog;
    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private List<String> purchasedProductList;
    private List<CommonPurchasedItemModel> purchasedItemModelList;

    public MyTestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_tests, container, false);

        MainActivity.currently_present_on_fragment = TYPE_TESTS;

        recyclerView = view.findViewById(R.id.testsFragRv);
        firestore = FirebaseFirestore.getInstance();
        purchasedProductList = new ArrayList<>();
        purchasedItemModelList = new ArrayList<>();

        loadingDialog = new Dialog(getContext());

        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore.collection("USER").document(FirebaseAuth.getInstance().getUid()).
                collection("USER_DATA").document("MY_TESTS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.contains("my_purchases")) {

                        purchasedProductList = (List<String>) documentSnapshot.get("my_purchases");

                        for (int i = 0; i < purchasedProductList.size(); i++) {

                            int a = i;

                            firestore.collection("PRODUCTS").document(purchasedProductList.get(i))
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){

                                        String title = (String) task.getResult().get("product_title");
                                        String subtitle = (String) task.getResult().get("product_subtitle");
                                        String image = (String) task.getResult().get("product_representative_image");
                                        String productId = purchasedProductList.get(a);
                                        long productType = task.getResult().getLong("product_type");

                                        purchasedItemModelList.add(new CommonPurchasedItemModel(image,title,subtitle,(int)productType,productId));

                                    }
                                    else{
                                        Toast.makeText(getContext(), "1 "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    }
                    else{
                        Toast.makeText(getContext(), "2 "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    loadingDialog.dismiss();

                }
                else{
                    Toast.makeText(getContext(), "3 "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                CommonPurchasedItemAdapter adapter = new CommonPurchasedItemAdapter(purchasedItemModelList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        handler.postDelayed(runnable,1000);

    }

    //    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(RecyclerView.VERTICAL);
//        recyclerView.setLayoutManager(manager);
//
//        firestore.collection("PURCHASEDPRODUCTS").document(CourseMaterialActivity.productId)
//                .collection(subjectTitle.toUpperCase()).document("tests").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    List<String> titleList = List<String> task.getResult().get("title_list");
//                    List<String> idList = List<String> task.getResult().get("id_list");
//
//                    PurchasedMaterialsAdapter adapter = new PurchasedMaterialsAdapter(titleList, idList, UserDetailsVariables.LECTURES);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                } else {
//
//                    Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//    }
}