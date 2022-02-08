package com.example.odishawarrior.classes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.CategoriesActivity;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    List<CategoriesModel> list;

    public CategoriesAdapter(List<CategoriesModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position).getCategoryTitle(),list.get(position).getImageAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageAdd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.categoryTitleTv);
            imageAdd = itemView.findViewById(R.id.categoryImageIcon);
        }

        private void setData(String t, String i){
            title.setText(t);
            Glide.with(itemView.getContext()).load(i).placeholder(R.drawable.placeholder_image_short).into(imageAdd);
            //imageAdd.setImageResource(i);

            if(!(getAdapterPosition() == 0)) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), CategoriesActivity.class);
                        intent.putExtra("category_name", t);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
