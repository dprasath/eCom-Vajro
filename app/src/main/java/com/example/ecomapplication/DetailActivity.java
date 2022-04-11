package com.example.ecomapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.db.Product;

public class DetailActivity extends AppCompatActivity {

    ProductViewModel viewModel;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        product = (Product) getIntent().getSerializableExtra("product");
        setTitle(product.getName());
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        TextView name = findViewById(R.id.name);
        name.setText(product.getName());
        TextView price = findViewById(R.id.price);
        price.setText(product.getPrice());
        TextView detail = findViewById(R.id.detail);
        detail.setText(product.getDescription());
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(DetailActivity.this)
                .load(product.getImage())
//                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
        findViewById(R.id.addButton).setOnClickListener(v -> {
            viewModel.addToCart(product);
        });
        findViewById(R.id.inc).setOnClickListener(v -> {
            viewModel.updateCart(product, product.getCount() + 1);
        });
        findViewById(R.id.dec).setOnClickListener(v -> {
            viewModel.updateCart(product, product.getCount() - 1);
        });
        viewModel.getCartItems().observe(this, products -> {
            if (products.contains(product)) {
                findViewById(R.id.addButton).setVisibility(View.GONE);
                findViewById(R.id.countLayout).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.count)).setText("" + products.get(products.indexOf(product)).getCount());
            } else {
                findViewById(R.id.addButton).setVisibility(View.VISIBLE);
                findViewById(R.id.countLayout).setVisibility(View.GONE);
            }
        });
    }
}