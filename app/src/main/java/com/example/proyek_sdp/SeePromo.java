package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SeePromo extends AppCompatActivity {

    ArrayList<voucher> list_promo;
    RecyclerView rv_promo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_promo);

        list_promo=new ArrayList<>();
        rv_promo = findViewById(R.id.rv_promo_see);

        //cetak promo
        FirebaseDatabase.getInstance().getReference().child("Voucher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("status_promo").getValue().toString().equals("1")){
                        //check yang belum kedaluawarsa
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d");
                        Boolean terlambat = true;
                        try {
                            Date tanggal_mulai = simpleDateFormat.parse(ds.child("mulai_promo").getValue().toString());
                            Date tanggal_selesai= simpleDateFormat.parse(ds.child("selesai_promo").getValue().toString());
                            Date dateSek= new Date();
                            Log.d("tanggal", simpleDateFormat.format(tanggal_selesai)+"---"+simpleDateFormat.format(dateSek));
                            if(tanggal_selesai.after(new Date())){
                                Log.d("tanggal bisa", simpleDateFormat.format(tanggal_selesai));
                                terlambat= false;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(!terlambat){
                            voucher x=new voucher();
                            x.setNama_promo(ds.child("nama_promo").getValue().toString());
                            x.setDeskripsi_promo(ds.child("deskripsi_promo").getValue().toString());
                            x.setDiskon_promo(ds.child("diskon_promo").getValue().toString());
                            x.setMulai_promo(ds.child("mulai_promo").getValue().toString());
                            x.setSelesai_promo(ds.child("selesai_promo").getValue().toString());
                            x.setStatus_promo(ds.child("status_promo").getValue().toString());
                            list_promo.add(x);}
                    }
                }
                rv_promo.setHasFixedSize(true);
                rv_promo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                ListPromoAdapter adapter = new ListPromoAdapter(getApplicationContext(), list_promo);
                rv_promo.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Set title bar
        getSupportActionBar().setTitle("Lihat Promo");
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
