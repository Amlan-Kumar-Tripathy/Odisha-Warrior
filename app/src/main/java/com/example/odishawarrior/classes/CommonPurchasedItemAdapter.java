package com.example.odishawarrior.classes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.CourseSubjectsActivity;
import com.example.odishawarrior.activities.NotesDisplayActivity;
import com.example.odishawarrior.activities.PurchasedNotesDisplayActivity;
import com.example.odishawarrior.activities.TestSetsActivity;

import java.util.List;

public class CommonPurchasedItemAdapter extends RecyclerView.Adapter<CommonPurchasedItemAdapter.ViewHolder> {

    List<CommonPurchasedItemModel> list;

    public CommonPurchasedItemAdapter(List<CommonPurchasedItemModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CommonPurchasedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchased_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonPurchasedItemAdapter.ViewHolder holder, int position) {
        holder.setData(list.get(position).getPurchasedProductImage(),list.get(position).getPurchasedProductTitle(),
                        list.get(position).getPurchasedProductSubtitle(),list.get(position).getPurchasedProductType(),
                list.get(position).getPurchasedProductId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle, productSubtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.purchasedProdIv);
            productTitle = itemView.findViewById(R.id.purchasedProdTitle);
           productSubtitle = itemView.findViewById(R.id.purchasedProdSubtitle);
        }

        private void setData(String img, String t, String subt, int type, String id){
            productTitle.setText(t);
            productSubtitle.setText(subt);
            Glide.with(itemView.getContext()).load(img).into(productImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(type == UserDetailsVariables.TYPE_COURSES){

                        Intent intent = new Intent(itemView.getContext(), CourseSubjectsActivity.class);
                        intent.putExtra("product_id",id);
                        intent.putExtra("product_type",type);
                        intent.putExtra("product_title",t);
                        itemView.getContext().startActivity(intent);

                    }
                    else if(type == UserDetailsVariables.TYPE_NOTES){

                        Intent intent = new Intent(itemView.getContext(), PurchasedNotesDisplayActivity.class);
                        intent.putExtra("product_id",id);
                        intent.putExtra("product_type",type);
                        intent.putExtra("product_title",t);
                        itemView.getContext().startActivity(intent);

                    }
                    else if(type == UserDetailsVariables.TYPE_TESTS){

                        Intent intent = new Intent(itemView.getContext(), TestSetsActivity.class);
                        intent.putExtra("product_id",id);
                        intent.putExtra("product_type",type);
                        intent.putExtra("product_title",t);
                        itemView.getContext().startActivity(intent);

                    }
                    else{
                        Toast.makeText(itemView.getContext(), "Please contact support team !", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
