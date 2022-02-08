package com.example.odishawarrior.classes;

import static com.example.odishawarrior.MainActivity.loadingDialog;
import static com.example.odishawarrior.activities.ViewAllActivity.viewAllActivityList;
import static com.example.odishawarrior.classes.HomePageModel.BANNER;
import static com.example.odishawarrior.classes.HomePageModel.GRID;
import static com.example.odishawarrior.classes.HomePageModel.HORIZONTAL;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.ProductDetailsActivity;
import com.example.odishawarrior.activities.ViewAllActivity;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> list;
    private FirebaseFirestore firestore;



    public HomePageAdapter(List<HomePageModel> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getType()){
            case BANNER:
                return 0;
            case HORIZONTAL:
                return 1;
            case GRID:
                return 2;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch(viewType){

            case BANNER:
                View view0 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.banner_view_products_layout,parent,false);
                return  new BannerProductView(view0);

            case HORIZONTAL:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.horizontal_slider_layout, parent, false);

                return new HorizontalProductView(view1);
            case GRID:
                View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_product_layout, parent, false);

                return new GridProductView(view2);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch(list.get(position).getType()) {
            case BANNER:
                ((BannerProductView) holder).setPoster(list.get(position).getPosterImagesList());
                break;
            case HORIZONTAL:
               // ((HorizontalProductView) holder).setHorizontalData(list.get(position).getLayoutTitle(), list.get(position).getHorizontalList(),list.get(position).getViewAllList());
                ((HorizontalProductView) holder).setHorizontalData(list.get(position).getLayoutTitle(), list.get(position).getProductList());

                break;
            case GRID:
                //((GridProductView)holder).setGridData(list.get(position).getBgColor(),list.get(position).getLayoutTitle(),list.get(position).getHorizontalList(),list.get(position).getViewAllList());
                ((GridProductView)holder).setGridData(list.get(position).getBgColor(),list.get(position).getLayoutTitle(),list.get(position).getProductList());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HorizontalProductView extends RecyclerView.ViewHolder{

        private TextView titleTv;
        private RecyclerView horizontalViewRV;
        private Button viewAllBtn;
        private List<HorizontalItemModel> horizontalList;
        private List<ViewAllModel> viewAllList;

        public HorizontalProductView(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.layoutTitle);
            horizontalViewRV = itemView.findViewById(R.id.horizontalScrollRecycleView);
            viewAllBtn = itemView.findViewById(R.id.viewAllButton);
            firestore = FirebaseFirestore.getInstance();

            horizontalList = new ArrayList<HorizontalItemModel>();
            viewAllList = new ArrayList<ViewAllModel>();

        }

        public void setHorizontalData(String t, List<String> productList){

            titleTv.setText(t);

            LinearLayoutManager m = new LinearLayoutManager(itemView.getContext());
            m.setOrientation(RecyclerView.HORIZONTAL);
            horizontalViewRV.setLayoutManager(m);


            //Fetching details from the Firestore

            for(int i=0;i< productList.size();i++){

                int a = i;

                firestore.collection("PRODUCTS").document(productList.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot shot = task.getResult();

                            String title = (String) shot.get("product_title");
                            String subtitle = (String) shot.get("product_subtitle");
                            String price = (String) shot.get("sell_price");
                            String normal_price = (String) shot.get("normal_price");
                            String image = (String) shot.get("product_representative_image");

                            // TODO: 12-01-2022 restrict the size of horizontalList to 4, so that only 4 items get displayed in the Home Page RecyclerView
                            horizontalList.add(new HorizontalItemModel(productList.get(a),image,title,subtitle,price));
                            viewAllList.add(new ViewAllModel(productList.get(a),image,title,subtitle,price,normal_price));

//                            if(a == productList.size()-1){
//                                Handler handler = new Handler();
//                               Runnable runnable = new Runnable() {
//                                   @Override
//                                   public void run() {
//                                       loadingDialog.dismiss();
//                                       HorizontalProductAdapter a = new HorizontalProductAdapter(horizontalList);
//                                       horizontalViewRV.setAdapter(a);
//                                       a.notifyDataSetChanged();
//                                   }
//                               };
//                               handler.postDelayed(runnable,3000);
//
//                            }

                        }
                        else{
                            Toast.makeText(itemView.getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        loadingDialog.dismiss();
                        HorizontalProductAdapter a = new HorizontalProductAdapter(horizontalList);
                        horizontalViewRV.setAdapter(a);
                        a.notifyDataSetChanged();
                    }
                });

            }

            //End of Fetching


            viewAllBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewAllActivityList = viewAllList;
                    Intent viewAllIntent = new Intent(itemView.getContext(),ViewAllActivity.class);
                    viewAllIntent.putExtra("layout_title", t);
                    itemView.getContext().startActivity(viewAllIntent);
                }
            });



        }
    }

    public class GridProductView extends  RecyclerView.ViewHolder{

        private ConstraintLayout rootLayout;
        private TextView layoutTitle;
        private GridLayout gridLayout;
        private Button viewAllGridButton;
        private List<HorizontalItemModel> gridHorizontalList;
        private List<ViewAllModel> gridViewAllList;

        public GridProductView(@NonNull View itemView) {
            super(itemView);

            rootLayout = itemView.findViewById(R.id.gridParentLayout);
            layoutTitle = itemView.findViewById(R.id.layoutTitle);
            gridLayout = itemView.findViewById(R.id.gridProdLayout);
            viewAllGridButton = itemView.findViewById(R.id.viewAllButton);

            firestore = FirebaseFirestore.getInstance();

            gridHorizontalList = new ArrayList<>();
            //HorizontalItemModel placeholderList = new HorizontalItemModel("");
            gridViewAllList = new ArrayList<>();

        }

        public void setGridData(String bgColor, String t, List<String> productList){
            //rootLayout.setBackgroundColor(Color.parseColor(bgColor));
            rootLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(bgColor)));
            layoutTitle.setText(t);

            for(int x=0;x< productList.size();x++){

                int a = x;
                gridHorizontalList.add(new HorizontalItemModel(productList.get(a), "","", "",""));

                firestore.collection("PRODUCTS").document(productList.get(x).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot shot = task.getResult();

                            String productTitle = (String) shot.get("product_title");
                            String productSubtitle = (String) shot.get("product_subtitle");
                            String productPrice = (String) shot.get("sell_price");
                            String productNormal_price = (String) shot.get("normal_price");
                            String productimage = (String) shot.get("product_representative_image");


                            gridHorizontalList.set(a ,new HorizontalItemModel(productList.get(a),productimage,productTitle,productSubtitle,productPrice));
                            gridViewAllList.add(new ViewAllModel(productList.get(a),productimage,productTitle,productSubtitle,productPrice,productNormal_price));

//                            if(a == productList.size()-1){
//
//                                Handler handler = new Handler();
//                                Runnable runnable = new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        for(int x=0; x< 4; x++) {
//
//
//                                            int b = x;
//
//                                            TextView title = gridLayout.getChildAt(x).findViewById(R.id.titleTv);
//                                            TextView subtitle = gridLayout.getChildAt(x).findViewById(R.id.subtitleTv);
//                                            TextView price = gridLayout.getChildAt(x).findViewById(R.id.price);
//                                            ImageView img = gridLayout.getChildAt(x).findViewById(R.id.productImageView);
//
//
//                                            //Fetching data from Firebase
//
//
//
//
//                                            //End of Fetching
//
//                                            title.setText(gridHorizontalList.get(x).getTitle());
//                                            subtitle.setText(gridHorizontalList.get(x).getSubtitle());
//                                            price.setText("Rs. "+ gridHorizontalList.get(x).getPrice() + "/-");
//                                            //img.setImageResource(gridHorizontalList.get(x).getImageResource());
//                                            Glide.with(itemView.getContext()).load(gridHorizontalList.get(x).getImageResource()).placeholder(R.drawable.placeholder_image).into(img);
//
//                                            gridLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
//                                                    intent.putExtra("product_id", productList.get(b));
//                                                    intent.putExtra("product_title", gridHorizontalList.get(b).getTitle());
//                                                    itemView.getContext().startActivity(intent);
//                                                }
//                                            });
//                                        }
//
//                                        loadingDialog.dismiss();
//
//                                    }
//                                };
//                                handler.postDelayed(runnable,3000);
//
//                            }
                        }
                        else{
                            Toast.makeText(itemView.getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(int x=0; x< 4; x++) {


                            int b = x;

                            TextView title = gridLayout.getChildAt(x).findViewById(R.id.titleTv);
                            TextView subtitle = gridLayout.getChildAt(x).findViewById(R.id.subtitleTv);
                            TextView price = gridLayout.getChildAt(x).findViewById(R.id.price);
                            ImageView img = gridLayout.getChildAt(x).findViewById(R.id.productImageView);


                            //Fetching data from Firebase




                            //End of Fetching

                            title.setText(gridHorizontalList.get(x).getTitle());
                            subtitle.setText(gridHorizontalList.get(x).getSubtitle());
                            price.setText("Rs. "+ gridHorizontalList.get(x).getPrice() + "/-");
                            //img.setImageResource(gridHorizontalList.get(x).getImageResource());
                            Glide.with(itemView.getContext()).load(gridHorizontalList.get(x).getImageResource()).placeholder(R.drawable.placeholder_image).into(img);

                            gridLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                                    intent.putExtra("product_id", productList.get(b));
                                    intent.putExtra("product_title", gridHorizontalList.get(b).getTitle());
                                    itemView.getContext().startActivity(intent);
                                }
                            });
                        }

                        loadingDialog.dismiss();
                    }
                });

            }



            viewAllGridButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewAllActivityList = gridViewAllList;
                    Intent viewAllIntent = new Intent(itemView.getContext(),ViewAllActivity.class);
                    viewAllIntent.putExtra("layout_title", t);
                    itemView.getContext().startActivity(viewAllIntent);
                }
            });

        }
    }



    public class BannerProductView extends RecyclerView.ViewHolder{

        private ViewPager bannerViewPager;
        private List<String> modifiedList;
        private int currentItem;
        private int delay;
        private int period;
        private Timer timer;

        public BannerProductView(@NonNull View itemView) {
            super(itemView);
            bannerViewPager = itemView.findViewById(R.id.banner_view_view_pager);
            modifiedList = new ArrayList<>();
            delay = 3000;
            period = 3000;
        }

        private void setPoster(List<String> posterImagesList){

            if(timer != null){
                timer.cancel();
            }

            modifiedList.add(posterImagesList.get(posterImagesList.size()-1));

            for(int i=0;i<posterImagesList.size();i++){
                modifiedList.add(posterImagesList.get(i));
            }

            modifiedList.add(posterImagesList.get(0));

            currentItem = 1;
            BannerProductAdapter adapter = new BannerProductAdapter(modifiedList);
            bannerViewPager.setAdapter(adapter);
            bannerViewPager.setCurrentItem(currentItem);
            bannerViewPager.setPageMargin(16);

            automaticSliding(modifiedList);

            bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                        currentItem = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                        if(state == bannerViewPager.SCROLL_STATE_IDLE){
                            createInfiniteLoop(modifiedList);

                        }
                }
            });

            bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    cancelTimer();
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        automaticSliding(modifiedList);
                    }
                    return false;
                }
            });

        }

        private void createInfiniteLoop(List<String> modifiedList){
            if(currentItem >= modifiedList.size()-1){
                currentItem = 1;
                //bannerViewPager.setCurrentItem(currentItem,false);
            }
            else if(currentItem == 0){
                currentItem = modifiedList.size()-2;
                bannerViewPager.setCurrentItem(currentItem,false);
            }
        }

        private void automaticSliding(List<String> list){

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                   /* if(currentItem == list.size()){
                        currentItem = 1;
                    }*/
                    if(currentItem==1){
                        bannerViewPager.setCurrentItem(currentItem,false);
                    }

                    else {

                        bannerViewPager.setCurrentItem(currentItem, true);
                    }
                        currentItem++;

                }
            };

            timer = new Timer();
         timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            },delay,period);

        }

        private void cancelTimer(){
            timer.cancel();
        }


    }


}
