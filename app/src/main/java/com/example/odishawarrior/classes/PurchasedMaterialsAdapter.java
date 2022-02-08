package com.example.odishawarrior.classes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.LectureDisplayActivity;

import java.util.List;

public class PurchasedMaterialsAdapter extends RecyclerView.Adapter<PurchasedMaterialsAdapter.ViewHolder> {

    private List<String> titleList, idUrlList;
    private int materialType;

    public PurchasedMaterialsAdapter(List<String> titleList, List<String> idUrlList, int materialType) {
        this.titleList = titleList;
        this.idUrlList = idUrlList;
        this.materialType = materialType;
    }

    @NonNull
    @Override
    public PurchasedMaterialsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchased_course_material_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedMaterialsAdapter.ViewHolder holder, int position) {
        holder.setData(titleList.get(position), idUrlList.get(position) );
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconIv;
        private TextView titleTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.icon);
            titleTv = itemView.findViewById(R.id.lessonsTitle);
        }

        private void setData(String title, String idUrl){

            titleTv.setText(title);

            if(materialType == UserDetailsVariables.LECTURES){

                iconIv.setImageResource(R.drawable.lecture_icon_blue);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            //VIDEO
                        Intent intent = new Intent(itemView.getContext(), LectureDisplayActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("idUrl", idUrl);
                        itemView.getContext().startActivity(intent);
                    }
                });

            }else  if(materialType == UserDetailsVariables.NOTES){

                iconIv.setImageResource(R.drawable.notes_icon_blue);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //PDF VIEWER
//                        Intent intent = new Intent(itemView.getContext(), aaa.class);
//                        intent.putExtra("title", title);
//                        intent.putExtra("idUrl", idUrl);
//                        itemView.getContext().startActivity(intent);
                    }
                });

            }else  if(materialType == UserDetailsVariables.TESTS){

                iconIv.setImageResource(R.drawable.tests_icon_blue);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TESTS INSTRUCTION WINDOW
//                        Intent intent = new Intent(itemView.getContext(), aaa.class);
//                        intent.putExtra("title", title);
//                        intent.putExtra("idUrl", idUrl);
//                        itemView.getContext().startActivity(intent);
                    }
                });


            }

        }
    }
}
