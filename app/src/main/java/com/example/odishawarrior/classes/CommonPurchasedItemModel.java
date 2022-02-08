package com.example.odishawarrior.classes;

public class CommonPurchasedItemModel {

    String purchasedProductImage;
    String purchasedProductTitle;
    String purchasedProductSubtitle;
    int purchasedProductType;
    String purchasedProductId;


    public CommonPurchasedItemModel(String purchasedProductImage, String purchasedProductTitle, String purchasedProductSubtitle, int purchasedProductType, String purchasedProductId) {
        this.purchasedProductImage = purchasedProductImage;
        this.purchasedProductTitle = purchasedProductTitle;
        this.purchasedProductSubtitle = purchasedProductSubtitle;
        this.purchasedProductType = purchasedProductType;
        this.purchasedProductId = purchasedProductId;
    }

    public int getPurchasedProductType() {
        return purchasedProductType;
    }

    public void setPurchasedProductType(int purchasedProductType) {
        this.purchasedProductType = purchasedProductType;
    }

    public String getPurchasedProductId() {
        return purchasedProductId;
    }

    public void setPurchasedProductId(String purchasedProductId) {
        this.purchasedProductId = purchasedProductId;
    }

    public String getPurchasedProductImage() {
        return purchasedProductImage;
    }

    public void setPurchasedProductImage(String purchasedProductImage) {
        this.purchasedProductImage = purchasedProductImage;
    }

    public String getPurchasedProductTitle() {
        return purchasedProductTitle;
    }

    public void setPurchasedProductTitle(String purchasedProductTitle) {
        this.purchasedProductTitle = purchasedProductTitle;
    }

    public String getPurchasedProductSubtitle() {
        return purchasedProductSubtitle;
    }

    public void setPurchasedProductSubtitle(String purchasedProductSubtitle) {
        this.purchasedProductSubtitle = purchasedProductSubtitle;
    }
}
