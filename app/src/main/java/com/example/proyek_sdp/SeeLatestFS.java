package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SeeLatestFS extends AppCompatActivity {

    RecyclerView rv_latestfs;
    ArrayList<barang> list_barang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_latest_fs);
        rv_latestfs=findViewById(R.id.rv_see_latestfs);
        list_barang=new ArrayList<>();

        //preorder latest
        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot row:dataSnapshot.getChildren()) {
                    if(row.child("jenis").getValue().toString().equalsIgnoreCase("Flash Sale")&&row.child("status").getValue().toString().equals("1")&&!row.child("idpenjual").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        barang data=new barang();
                        data.setId(row.child("id").getValue().toString());
                        data.setDeskripsi(row.child("deskripsi").getValue().toString());
                        data.setIdpenjual(row.child("idpenjual").getValue().toString());
                        data.setJenis(row.child("jenis").getValue().toString());
                        data.setNama(row.child("nama").getValue().toString());
                        data.setLokasi(row.child("lokasi").getValue().toString());
                        data.setVarian(row.child("varian").getValue().toString());
                        data.setMaksimal(Integer.parseInt(row.child("maksimal").getValue().toString()));
                        data.setWaktu_selesai(row.child("waktu_selesai").getValue().toString());
                        data.setWaktu_mulai(row.child("waktu_mulai").getValue().toString());
                        data.setWaktu_upload(row.child("waktu_upload").getValue().toString());
                        data.setHarga(Integer.parseInt(row.child("harga").getValue().toString()));
                        data.setKategori(row.child("kategori").getValue().toString());
                        data.setBerat(Integer.parseInt(row.child("berat").getValue().toString()));
                        data.setStatus(Integer.parseInt(row.child("status").getValue().toString()));
                        list_barang.add(data);
                    }
                }

                Collections.sort(list_barang,barang.sortdescwaktu);
                //cetak barang flashsale
                rv_latestfs.setHasFixedSize(true);
                rv_latestfs.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                TopFlashSaleSeeAllAdapter adapterflashsale = new TopFlashSaleSeeAllAdapter(getApplicationContext(), list_barang);
                adapterflashsale.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        barang x = list_barang.get(position);
                        Bundle b = new Bundle();
                        b.putSerializable("barang", x);
                        Intent intent = new Intent(getApplicationContext(), detail_feed.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                rv_latestfs.setAdapter(adapterflashsale);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setTitle("Latest Flash Sales");
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
