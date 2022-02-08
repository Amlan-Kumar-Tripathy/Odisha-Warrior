package com.example.odishawarrior.classes;

import java.util.List;

public class HomePageModel {

    public static final int BANNER = 0;
    public static final int HORIZONTAL = 1;
    public static final int GRID = 2;

    private int type;
    private List<HorizontalItemModel> horizontalList;
    private String layoutTitle;
    private String bgColor;
    private List<ViewAllModel> viewAllList;
    private List<String> posterImagesList;
    private List<String> productList;

    public HomePageModel(int type, List<String> productList, String layoutTitle) {
        this.type = type;
        //this.horizontalList = horizontalList;
        this.layoutTitle = layoutTitle;
        //this.viewAllList = viewAllList;
        this.productList = productList;
    }

    public HomePageModel(int type, List<String> productList, String layoutTitle, String bgColor) {
        this.type = type;
        //this.horizontalList = horizontalList;
        this.layoutTitle = layoutTitle;
        this.bgColor = bgColor;
       // this.viewAllList = viewAllList;
        this.productList = productList;
    }

    public HomePageModel(int type, List<String> posterImagesList) {
        this.type = type;
        this.posterImagesList = posterImagesList;
    }

    public List<String> getPosterImagesList() {
        return posterImagesList;
    }

    public void setPosterImagesList(List<String> posterImagesList) {
        this.posterImagesList = posterImagesList;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<HorizontalItemModel> getHorizontalList() {
        return horizontalList;
    }

    public void setHorizontalList(List<HorizontalItemModel> horizontalList) {
        this.horizontalList = horizontalList;
    }

    public String getLayoutTitle() {
        return layoutTitle;
    }

    public void setLayoutTitle(String layoutTitle) {
        this.layoutTitle = layoutTitle;
    }

    public List<ViewAllModel> getViewAllList() {
        return viewAllList;
    }

    public void setViewAllList(List<ViewAllModel> viewAllList) {
        this.viewAllList = viewAllList;
    }

    //newly added

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    //
}
