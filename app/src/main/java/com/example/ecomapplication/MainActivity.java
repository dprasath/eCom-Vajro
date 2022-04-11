package com.example.ecomapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.api.ApiClient;
import com.example.ecomapplication.api.ApiInterface;
import com.example.ecomapplication.api.ProductResponse;
import com.example.ecomapplication.db.Product;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ProductViewModel viewModel;
    ProgressDialog progressDialog;
    ProductAdapter adapter;
    List<Product> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Products");
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        progressDialog = new ProgressDialog(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        getProductList();

        viewModel.getCartItems().observe(this, products -> {
            Log.e("ProductCount", ""+products.size());
            cartItems = products;
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        startActivity(intent);
        return true;
    }

    private void getProductList() {
        showLoading("Please wait...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductResponse> call = apiInterface.getProductList("https://www.mocky.io/v2/5def7b172f000063008e0aa2");
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    Log.e("Res", "" + response.body().toString());
                    handleResponse(response.body());
                } else {
                    ResponseBody errorBody = response.errorBody();
                    Log.e("Response errorBody", String.valueOf(errorBody));
                    showError();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                hideLoading();
                Log.e("TAG", "Response = " + t.toString());
            }
        });
    }


    private void handleResponse(ProductResponse response) {
        if (response.getProducts() != null && response.getProducts().size() > 0) {
            List<Product> products = response.getProducts();
            adapter.updateList(products);
        } else {
            showError();
        }
    }

    private void showError() {
        Toast.makeText(this, "Error! Please try again later", Toast.LENGTH_SHORT).show();
    }

    public void showLoading(String text) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(text);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

        private List<Product> products;

        public ProductAdapter(List<Product> data) {
            this.products = data;
        }


        public void updateList(final List<Product> streamList) {
            this.products.clear();
            this.products = streamList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MainActivity.ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
            return new ProductAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.ProductAdapter.ViewHolder holder, int position) {
            Product product = products.get(position);
            holder.name.setText(product.getName());
            holder.price.setText(product.getPrice());
            Glide.with(MainActivity.this)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
            holder.countLayout.setVisibility(View.GONE);
            holder.add.setVisibility(View.VISIBLE);
            if (!cartItems.contains(product)) {
                holder.countLayout.setVisibility(View.GONE);
                holder.add.setVisibility(View.VISIBLE);
            } else {
                holder.countLayout.setVisibility(View.VISIBLE);
                holder.add.setVisibility(View.GONE);
                holder.count.setText(""+ cartItems.get(cartItems.indexOf(product)).getCount());
            }
            holder.root.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            });
            holder.add.setOnClickListener(v -> {
                viewModel.addToCart(product);
                notifyItemChanged(position);
            });
            holder.inc.setOnClickListener(v -> {
                viewModel.updateCart(product, product.getCount() + 1);
                notifyItemChanged(position);
            });
            holder.dec.setOnClickListener(v -> {
                viewModel.updateCart(product, product.getCount() - 1);
                notifyItemChanged(position);
            });
        }

        @Override
        public int getItemCount() {
            if (products != null) {
                return products.size();
            } else {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price, count;
            public ImageView imageView;
            public View add, inc, dec, root, countLayout;

            public ViewHolder(View v) {
                super(v);
                root = v;
                name = v.findViewById(R.id.name);
                price = v.findViewById(R.id.price);
                count = v.findViewById(R.id.count);
                add = v.findViewById(R.id.addButton);
                countLayout = v.findViewById(R.id.countLayout);
                inc = v.findViewById(R.id.inc);
                dec = v.findViewById(R.id.dec);
                imageView = v.findViewById(R.id.imageView);
            }
        }
    }
}