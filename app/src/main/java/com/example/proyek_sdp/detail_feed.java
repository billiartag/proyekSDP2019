package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class detail_feed extends AppCompatActivity {
    ImageView img,imghati;
    TextView tipe;
    TextView nama;
    TextView harga;
    TextView deskripsi;
    TextView pemilik;
    TextView durasi;
    TextView max;
    Button isi_wishlist;
    Button beli;
    Button nego;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
        //isi_wishlist=findViewById(R.id.isi_wishlist);
        beli=findViewById(R.id.beli);
        nego = findViewById(R.id.button_nego);
        img=findViewById(R.id.img);
        imghati=findViewById(R.id.imghatikosong);
        tipe=findViewById(R.id.tipe);
        nama=findViewById(R.id.username);
        harga=findViewById(R.id.harga);
        deskripsi=findViewById(R.id.deskripsi);
        pemilik=findViewById(R.id.pemilik);
        durasi=findViewById(R.id.durasi);
        max=findViewById(R.id.max);
        barang x= (barang)getIntent().getExtras().getSerializable("barang");
        FirebaseStorage.getInstance().getReference().child("img_barang").child(x.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(img);
            }
        });
        tipe.setText(x.getJenis().toString());
        nama.setText(x.getNama());
        harga.setText("Harga Barang : Rp. "+x.getHarga());
        deskripsi.setText("Deskripsi : "+x.getDeskripsi());
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(x.getIdpenjual())){
                        pemilik.setText("Pemilik : "+ds.child("nama").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        durasi.setText("Durasi : "+x.getWaktu_selesai());
        max.setText("Max barang yang dapat dipesan : "+x.getMaksimal());
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
       /* isi_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Tambah ke WishList Berhasil ", Toast.LENGTH_SHORT).show();
            }
        });*/
        imghati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sudahfavorite = 1;
                boolean sudahada = false;

                if(sudahfavorite == 1) {
                //    sudahada = true;
                    imghati.setImageResource(R.drawable.ic_hatimerah_black_24dp);
                    Toast.makeText(getApplicationContext(), "Tambah ke WishList Berhasil ", Toast.LENGTH_SHORT).show();
                }/*else if(sudahada == true) {
                    Toast.makeText(getApplicationContext(), "Removed From WishList", Toast.LENGTH_SHORT).show();
                    imghati.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }*/
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
        nego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (detail_feed.this,nego_user.class);
                //isi param lempar disini
                startActivity(i);
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
