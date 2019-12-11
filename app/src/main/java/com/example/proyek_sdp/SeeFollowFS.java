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

public class SeeFollowFS extends AppCompatActivity {

    ArrayList<barang> list_fs;
    RecyclerView rv_fs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_follow_fs);
        list_fs=new ArrayList<>();
        rv_fs = findViewById(R.id.rv_see_fsfollow);


        //flash sale
        FirebaseDatabase.getInstance().getReference().child("FollowDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_user").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                    if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds2.child("idpenjual").getValue().toString())){
                                        if(ds2.child("jenis").getValue().toString().equals("Flash Sale") && ds2.child("idpenjual").getValue().toString().equals(ds.child("following").getValue().toString()) && ds2.child("status").getValue().toString().equals("1")){
                                            barang data=new barang();
                                            data.setId(ds2.child("id").getValue().toString());
                                            data.setDeskripsi(ds2.child("deskripsi").getValue().toString());
                                            data.setIdpenjual(ds2.child("idpenjual").getValue().toString());
                                            data.setJenis(ds2.child("jenis").getValue().toString());
                                            data.setNama(ds2.child("nama").getValue().toString());
                                            data.setLokasi(ds2.child("lokasi").getValue().toString());
                                            data.setVarian(ds2.child("varian").getValue().toString());
                                            data.setMaksimal(Integer.parseInt(ds2.child("maksimal").getValue().toString()));
                                            data.setWaktu_selesai(ds2.child("waktu_selesai").getValue().toString());
                                            data.setWaktu_mulai(ds2.child("waktu_mulai").getValue().toString());
                                            data.setWaktu_upload(ds2.child("waktu_upload").getValue().toString());
                                            data.setHarga(Integer.parseInt(ds2.child("harga").getValue().toString()));
                                            data.setKategori(ds2.child("kategori").getValue().toString());
                                            data.setBerat(Integer.parseInt(ds2.child("berat").getValue().toString()));
                                            data.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                                            list_fs.add(data);
                                        }
                                    }
                                }
                                //cetak barang flashsale
                                rv_fs.setHasFixedSize(true);
                                rv_fs.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                                TopFlashSaleSeeAllAdapter adapterflashsale = new TopFlashSaleSeeAllAdapter(getApplicationContext(), list_fs);
                                adapterflashsale.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        barang x = list_fs.get(position);
                                        Bundle b = new Bundle();
                                        b.putSerializable("barang", x);
                                        Intent intent = new Intent(getApplicationContext(), detail_feed.class);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }
                                });
                                rv_fs.setAdapter(adapterflashsale);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setTitle("Followed Flash Sales");
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
