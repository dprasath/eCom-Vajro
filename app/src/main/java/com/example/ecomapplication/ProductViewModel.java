package com.example.ecomapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ecomapplication.db.DBManager;
import com.example.ecomapplication.db.Product;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private DBManager dbManager;
    private LiveData<List<Product>> productLiveData;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        dbManager = new DBManager(application);
        productLiveData = dbManager.getCartItems();
    }

    public void addToCart(Product product) {
        product.setCount(1);
        dbManager.insert(product);
    }

    public void updateCart(Product product, int count) {
        if (count <= 0) {
            dbManager.delete(product);
        } else {
            product.setCount(count);
            dbManager.update(product);
        }
    }

    public void removeFromCart(Product product) {
        dbManager.delete(product);
    }


    public LiveData<List<Product>> getCartItems() {
        return productLiveData;
    }
}
