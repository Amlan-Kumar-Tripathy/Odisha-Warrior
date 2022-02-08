package com.example.odishawarrior.classes;

import static com.example.odishawarrior.activities.TestSetsActivity.loadingDialog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.TestInstructionsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TestSetsAdapter extends RecyclerView.Adapter<TestSetsAdapter.ViewHolder> {

    private FirebaseFirestore firestore;

    private List<String> titleList;
    private String id, product_title;

    public TestSetsAdapter(List<String> titleList, String id, String product_title) {
        this.titleList = titleList;
        this.id = id;
        this.product_title = product_title;
    }

    @NonNull
    @Override
    public TestSetsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_sets_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestSetsAdapter.ViewHolder holder, int position) {
        holder.setData(titleList.get(position), position+1);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv, attemptsTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.testTitle);
            attemptsTv = itemView.findViewById(R.id.attemptsNo);
            firestore = FirebaseFirestore.getInstance();

        }

        private void setData(String title, int setNo){

            titleTv.setText(""+setNo + ") "+title);

            // TODO: 02-01-2022 attempts count


            firestore.collection("USER").document(FirebaseAuth.getInstance().getUid()).
                    collection("USER_DATA").document("MY_TESTS").collection("ATTEMPTED_TESTS")
                    .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){

                        DocumentSnapshot snapshot = task.getResult();
                        Long attemptCount;
                        Long attemptsLeft;

                        if(snapshot.exists()){
                            //User has attempted at least 1 test from the test series.
                            if(snapshot.contains("" + setNo + "_attempts_count")){
                                //User has attempted this particular test from the test series.

                                attemptCount = (Long) snapshot.get("" + setNo + "_attempts_count");
                                attemptsLeft = UserDetailsVariables.TOTAL_POSSIBLE_TEST_ATTEMPTS - attemptCount;
                                attemptsTv.setText(""+ attemptsLeft +" Attempts Left");

                                itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(itemView.getContext(), TestInstructionsActivity.class);
                                        intent.putExtra("product_id", id);
                                        intent.putExtra("set_no", setNo);
                                        intent.putExtra("product_title", product_title);
                                        intent.putExtra("test_title", title);
                                        intent.putExtra("no_of_attempts", attemptCount.intValue());

                                        itemView.getContext().startActivity(intent);
                                    }
                                });

                            }
                            else{
                                //User has NOT attempted this particular test from the test series.
                                attemptsTv.setText(""+ UserDetailsVariables.TOTAL_POSSIBLE_TEST_ATTEMPTS +" Attempts Left");

                                itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(itemView.getContext(), TestInstructionsActivity.class);
                                        intent.putExtra("product_id", id);
                                        intent.putExtra("set_no", setNo);
                                        intent.putExtra("product_title", product_title);
                                        intent.putExtra("test_title", title);
                                        intent.putExtra("no_of_attempts", 0);

                                        itemView.getContext().startActivity(intent);
                                    }
                                });

                            }


                        }
                        else {
                            //User has NOT attempted any test from the test series.

                            attemptsTv.setText(""+ UserDetailsVariables.TOTAL_POSSIBLE_TEST_ATTEMPTS +" Attempts Left");

                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(itemView.getContext(), TestInstructionsActivity.class);
                                    intent.putExtra("product_id", id);
                                    intent.putExtra("set_no", setNo);
                                    intent.putExtra("product_title", product_title);
                                    intent.putExtra("test_title", title);
                                    intent.putExtra("no_of_attempts", 0);

                                    itemView.getContext().startActivity(intent);
                                }
                            });

                        }

                        loadingDialog.dismiss();

                    }
                    else{

                        Toast.makeText(itemView.getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();

                    }
                }
            });


            //to be deleted
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(itemView.getContext(), TestInstructionsActivity.class);
//                    intent.putExtra("product_id", id);
//                    intent.putExtra("set_no", setNo);
//                    intent.putExtra("product_title", product_title);
//                    intent.putExtra("test_title", title);
//                    //intent.putExtra("no_of_attempts", attemptCount);
//
//                   itemView.getContext().startActivity(intent);
//                }
//            });

        }
    }
}
