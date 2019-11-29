package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class cart extends AppCompatActivity {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    Button bayar;
    TextView total_harga;
    RecyclerView rv_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bayar=findViewById(R.id.bayar_cart);
        total_harga=findViewById(R.id.harga_barang_cart);
        rv_cart=findViewById(R.id.rv_cart);
        rv_cart.setHasFixedSize(true);
        rv_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        CartAdapter adapter = new CartAdapter(getApplicationContext(), kumpulanbarang);
        rv_cart.setAdapter(adapter);

        int total=0;
        for (barang x:kumpulanbarang) {
           // total=total+x.getHarga();
        }
        total_harga.setText("Rp "+total);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_cart, menu);
        menu.getItem(0).setTitle("saldo : 10000");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close_cart){
            finish();
        }
        else if (item.getItemId()==R.id.top_up_cart){
            Intent move=new Intent(getApplicationContext(),topup_activity.class);
            startActivity(move);
        }
        return true;
    }
}
