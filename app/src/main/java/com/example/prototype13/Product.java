package com.example.prototype13;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    //private int thumbnail;
    private int price;
    private Double rating;
    private String brand;
    private int isFulfilled;
    //private String seller;
    //private String description;
    //private String specs;
    private String url;
    private String thumbnailURL;
    private int reviews;
    private String originalPrice;
    private String domain;
    private String productID;
    private String json;
    private int value;

    /*public Product(String name, int price, int thumbnail, String brand, double rating, String url) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.price = price;
        this.brand = brand;
        this.rating = rating;
        this.url = url;
    }*/

    public Product(String name, int price, String thumbnailURL, String brand, Double rating, String url) {
        this.name = name;
        this.thumbnailURL = thumbnailURL;
        this.price = price;
        this.brand = brand;
        this.rating = rating;
        this.url = url;
    }

    public Product(String name, int price, String thumbnailURL, String brand, Double rating, int reviews, String url, int isFulfilled, String originalPrice) {
        this.name = name;
        this.thumbnailURL = thumbnailURL;
        this.price = price;
        this.brand = brand;
        this.rating = rating;
        this.url = url;
        this.reviews = reviews;
        this.isFulfilled = isFulfilled;
        this.originalPrice = originalPrice;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
    /*
        public void setName(String name) {
            this.name = name;
        }
    */
    public String getProductID(){return productID;}

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getDomain() { return domain; }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getOriginalPrice(){return originalPrice;}


    public int getReviews() {
        return reviews;
    }
/*
    public void setReviews(int reviews) {
        this.reviews = reviews;
    }
*/

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }


    /*public int getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }*/

    public int getPrice() {
        return price;
    }
/*
    public void setPrice(int price) {
        this.price = price;
    }
*/

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    /*public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }*/

    public Double getRating() {
        return rating;
    }
/*
    public void setRating(Double rating) {
        this.rating = rating;
    }
*/

    public int getFulfillment() {
        return isFulfilled;
    }
/*
    public void setFulfilled(byte isFulfilled) {
        this.isFulfilled = isFulfilled;
    }
*/

    public String getUrl(){
        return url;
    }
    /*public void setUrl(String url){
        this.url = url;
    }*/


    @Override
    public int describeContents() {
        return 0;
    }


    public Product(Parcel in) {
        name = in.readString();
        brand = in.readString();
        //seller = in.readString();
        //description = in.readString();
        //specs = in.readString();
        url = in.readString();
        price = in.readInt();
        rating = in.readDouble();
        isFulfilled = in.readInt();
        thumbnailURL = in.readString();
        domain = in.readString();
        json = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(brand);
        //dest.writeString(seller);
        //dest.writeString(description);
        //dest.writeString(specs);
        dest.writeString(url);
        dest.writeInt(price);
        dest.writeDouble(rating);
        dest.writeInt(isFulfilled);
        dest.writeString(thumbnailURL);
        dest.writeString(domain);
        dest.writeString(json);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}