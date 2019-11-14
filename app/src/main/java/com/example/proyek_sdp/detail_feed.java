package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class detail_feed extends AppCompatActivity {
    ImageView img;
    TextView tipe;
    TextView nama;
    TextView harga;
    TextView deskripsi;
    TextView pemilik;
    TextView durasi;
    TextView max;
    Button isi_wishlist;
    Button beli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
        isi_wishlist=findViewById(R.id.isi_wishlist);
        beli=findViewById(R.id.beli);
        img=findViewById(R.id.img);
        tipe=findViewById(R.id.tipe);
        nama=findViewById(R.id.username);
        harga=findViewById(R.id.harga);
        deskripsi=findViewById(R.id.deskripsi);
        pemilik=findViewById(R.id.pemilik);
        durasi=findViewById(R.id.durasi);
        max=findViewById(R.id.max);
        barang x= (barang)getIntent().getExtras().getSerializable("barang");
        img.setBackgroundResource(x.getGambar());
        tipe.setText(x.getTipe().toString());
        nama.setText(x.getNama());
        harga.setText("Harga Barang : Rp. "+x.getHarga());
        deskripsi.setText("Deskripsi : "+x.getDeskripsi());
        pemilik.setText("Pemilik : "+x.getPemilik());
        durasi.setText("Durasi : "+x.getDurasi());
        max.setText("Max barang yang dapat dipesan : "+x.getMax_barang());
        if (tipe.getText().toString().equals("Flash Sale")){
            tipe.setBackgroundColor(Color.YELLOW);
            tipe.setTextColor(Color.BLACK);
            beli.setText("Tambah Ke Keranjang");
        }
        else if (tipe.getText().toString().equals("Pre Order")){
            tipe.setBackgroundColor(Color.BLACK);
            tipe.setTextColor(Color.WHITE);
            beli.setText("Lakukan Pre Order");
        }
        isi_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Tambah ke WishList Berhasil ", Toast.LENGTH_SHORT).show();
            }
        });
        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipe.getText().toString().equals("Flash Sale")){
                Toast.makeText(getApplicationContext(), "Tambah ke Keranjang Berhasil!", Toast.LENGTH_SHORT).show();
            }
            else if (tipe.getText().toString().equals("Pre Order")){
                Toast.makeText(getApplicationContext(), "Pre Order Berhasil!", Toast.LENGTH_SHORT).show();
            }
            }
        });
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
