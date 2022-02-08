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

public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.ViewHolder>{

    private List<HorizontalItemModel> list;

    public HorizontalProductAdapter(List<HorizontalItemModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_product_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //GET THE PRODUCT ID FROM HERE

       holder.setData(list.get(position).getProductId(),list.get(position).getImageResource(),list.get(position).getTitle()
       , list.get(position).getSubtitle(),list.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView titleTv,subtitleTv,priceTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.productImageView);
            titleTv = itemView.findViewById(R.id.titleTv);
            subtitleTv = itemView.findViewById(R.id.subtitleTv);
            priceTv = itemView.findViewById(R.id.price);



        }

        public void setData(String productId,String img,String t,String subt, String prTv){
            //
            //image.setImageResource(img);
            Glide.with(itemView.getContext()).load(img).placeholder(R.drawable.placeholder_image).into(image);
            titleTv.setText(t);
            subtitleTv.setText(subt);
            priceTv.setText("Rs. "+prTv+"/-");

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
