package com.example.odishawarrior.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.odishawarrior.R;

import java.util.List;

public class BannerProductAdapter extends PagerAdapter {

    private List<String> list;

    public BannerProductAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).
                inflate(R.layout.banners_layout,container,false);
        ImageView posterImage = view.findViewById(R.id.banner_poster_imageview);
        Glide.with(container.getContext()).load(list.get(position)).placeholder(R.drawable.placeholder_image_large).into(posterImage);
        container.addView(view,0);
        return view;

        //return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
