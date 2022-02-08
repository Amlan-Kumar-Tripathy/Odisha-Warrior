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
import com.example.odishawarrior.activities.ProductDetailsActivity;

import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {

    private List<ViewAllModel> list;

    public ViewAllAdapter(List<ViewAllModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_all_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position).getProductId(),list.get(position).getProductImage(),list.get(position).getProductTitle(),list.get(position).getProductSubtitle(),list.get(position).getProductPrice(),
                list.get(position).getProductActualPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImageIv;
        private TextView titleTv,subtitleTv,priceTv,normalPriceTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageIv = itemView.findViewById(R.id.prodImage);
            titleTv = itemView.findViewById(R.id.prodTitle);
            subtitleTv = itemView.findViewById(R.id.prodSubtitle);
            priceTv = itemView.findViewById(R.id.prodPrice);
            normalPriceTv = itemView.findViewById(R.id.prodNormalPrice);

        }

        public void setData(String productId,String imgRes, String t, String st, String p, String np){

            //productImageIv.setImageResource(imgRes);
            Glide.with(itemView.getContext()).load(imgRes).into(productImageIv);
            titleTv.setText(t);
            subtitleTv.setText(st);
            priceTv.setText("Rs. "+ p+ "/-");
            normalPriceTv.setText("Rs. "+ np+ "/-");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    intent.putExtra("product_id", productId);
                    intent.putExtra("product_title", t);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
