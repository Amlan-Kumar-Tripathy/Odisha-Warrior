package com.example.odishawarrior.classes;

public class ViewAllModel {

    String productImage;
    String productTitle,productSubtitle,productPrice,productActualPrice,productId;

    public ViewAllModel(String productId,String productImage, String productTitle, String productSubtitle, String productPrice, String productActualPrice) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productSubtitle = productSubtitle;
        this.productPrice = productPrice;
        this.productActualPrice = productActualPrice;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductActualPrice() {
        return productActualPrice;
    }

    public void setProductActualPrice(String productActualPrice) {
        this.productActualPrice = productActualPrice;
    }
}
