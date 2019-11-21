package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class wishlist extends AppCompatActivity {

    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    RecyclerView rvWishlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        rvWishlist = findViewById(R.id.rvWishlist);
        kumpulanbarang.add(new barang("Treadmill",25000,"bagus untuk kesehatan","05:45:21","Flash Sale",12,R.drawable.treadmill,"cosmas"));
        kumpulanbarang.add(new barang("electronic Treadmill",55000,"bagus untuk kesehatan dan otot kaki","07:45:21","Pre Order",14,R.drawable.electrictreadmill,"Alfon"));
        kumpulanbarang.add(new barang("Bike",75000,"bagus untuk kesehatan dan mudah di pakai tanpa keluar rumah","08:45:21","Flash Sale",6,R.drawable.bike,"Edwin"));

        rvWishlist.setLayoutManager(new GridLayoutManager(this,2));
        WishlistAdapter adap=new WishlistAdapter(getApplicationContext(),kumpulanbarang);
        rvWishlist.setAdapter(adap);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            finish();
        }
        return true;
    }
}
