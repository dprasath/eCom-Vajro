package com.example.ecomapplication.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Product implements Serializable {

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("id")
    @PrimaryKey()
    private int id;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String image;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    @ColumnInfo(name = "price")
    private String price;

    @SerializedName("special")
    private String specialPrice;

    @ColumnInfo(name = "count")
    private int count;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getProductId() {
        return productId;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
