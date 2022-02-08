package com.example.odishawarrior.classes;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.CourseMaterialActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private List<String> list;
    private String productId;

    public SubjectsAdapter(List<String> list, String productId) {
        this.list = list;
        this.productId = productId;
    }

    @NonNull
    @Override
    public SubjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subjects_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsAdapter.ViewHolder holder, int position) {

        List<String> bgColorList = new ArrayList<>();
        bgColorList.add("#fa795f");
        bgColorList.add("#fcf25d");
        bgColorList.add("#adf75e");
        bgColorList.add("#5ef2df");
        bgColorList.add("#5cbbf2");
        bgColorList.add("#db77f7");
        bgColorList.add("#fa84de");
        bgColorList.add("#f78699");

        Random r = new Random();
        String bgColor = bgColorList.get(r.nextInt(bgColorList.size()));

        holder.setData(list.get(position), bgColor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView subjectTitleTv;
        private ConstraintLayout subjectConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectConstraintLayout = itemView.findViewById(R.id.course_subjects_constraintLayout);
            subjectTitleTv = itemView.findViewById(R.id.course_subjects_title);

        }

        private void setData(String title, String bgColor){

            subjectTitleTv.setText(title);
            subjectConstraintLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(bgColor)));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), CourseMaterialActivity.class);
                    intent.putExtra("subject_title",title);
                    intent.putExtra("subject_id",productId);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
