package com.example.ecomapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.db.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    CartAdapter adapter;
    ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CartAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        viewModel.getCartItems().observe(this, products -> {
            Log.e("CartCount", "" + products.size());
            adapter.updateList(products);
            calculateTotal(products);
        });
    }

    private void calculateTotal(List<Product> products) {
        int total = 0;
        for (Product product : products) {
            total += Integer.parseInt(product.getPrice().replace("₹", "").replace(",", "")) * product.getCount();
        }
        ((TextView) findViewById(R.id.total)).setText("₹" + total);
    }

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        private List<Product> products;

        public CartAdapter(List<Product> data) {
            this.products = data;
        }


        public void updateList(final List<Product> streamList) {
            this.products.clear();
            this.products = streamList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
            return new CartAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
            Product product = products.get(position);
            holder.name.setText(product.getName());
            holder.price.setText(product.getPrice());
            holder.count.setText("" + product.getCount());
            int total = Integer.parseInt(product.getPrice().replace("₹", "").replace(",", "")) * product.getCount();
            holder.itemTotal.setText("₹" + total);
            Glide.with(CartActivity.this)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
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
            public TextView name, price, count, itemTotal;
            public ImageView imageView;
            public View add, inc, dec, root;

            public ViewHolder(View v) {
                super(v);
                root = v;
                name = v.findViewById(R.id.name);
                price = v.findViewById(R.id.price);
                count = v.findViewById(R.id.count);
                itemTotal = v.findViewById(R.id.itemTotal);
                add = v.findViewById(R.id.addButton);
                inc = v.findViewById(R.id.inc);
                dec = v.findViewById(R.id.dec);
                imageView = v.findViewById(R.id.imageView);
            }
        }
    }
}