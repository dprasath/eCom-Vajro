package com.example.ecomapplication.api;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.ecomapplication.db.Product;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductResponse {

    @SerializedName("products")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    /*@Entity
    public class Product implements Serializable {

        @SerializedName("name")
        @ColumnInfo(name = "name")
        private String name;

        @SerializedName("id")
        @PrimaryKey()
        private String id;

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

        @ColumnInfo(name = "quantity")
        private boolean quantity;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getProductId() {
            return productId;
        }

        public String getImage() {
            return image;
        }

        public String getDescription() {
            return description;
        }

        public String getPrice() {
            return price;
        }

        public String getSpecialPrice() {
            return specialPrice;
        }
    }*/

}
