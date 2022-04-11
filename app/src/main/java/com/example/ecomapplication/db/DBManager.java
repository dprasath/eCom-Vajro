package com.example.ecomapplication.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DBManager {

    private CartDao cartDao;
    private LiveData<List<Product>> cartItems;

    //our app database object
    private AppDatabase appDatabase;

    public DBManager(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cartDao = database.cartDao();
        cartItems = cartDao.getAll();
    }

    public void insert(Product product) {
        new InsertAsyncTask(cartDao).execute(product);
    }

    public void update(Product product) {
        new UpdateAsyncTask(cartDao).execute(product);
    }

    public void delete(Product product) {
        new DeleteAsyncTask(cartDao).execute(product);
    }

    public LiveData<List<Product>> getCartItems() {
        return cartItems;
    }

    private static class InsertAsyncTask extends AsyncTask<Product, Void, Void> {
        private CartDao cartDao;

        private InsertAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            cartDao.insert(products[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Product, Void, Void> {
        private CartDao cartDao;

        private UpdateAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            cartDao.update(products[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Product, Void, Void> {
        private CartDao cartDao;

        private DeleteAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            cartDao.delete(products[0]);
            return null;
        }
    }
}
