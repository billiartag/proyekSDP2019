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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class wishlist_activity extends AppCompatActivity {

    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    RecyclerView rvWishlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        rvWishlist = findViewById(R.id.rvWishlist);
        //start program
        FirebaseDatabase.getInstance().getReference().child("WishListDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                long count=dataSnapshot2.getChildrenCount();
                                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                    if(ds2.child("id").getValue().equals(ds.child("id_barang").getValue().toString())){
                                        barang wish_user=new barang();
                                        wish_user.setId(ds2.child("id").getValue().toString());
                                        wish_user.setWaktu_upload(ds2.child("waktu_upload").getValue().toString());
                                        wish_user.setWaktu_mulai(ds2.child("waktu_mulai").getValue().toString());
                                        wish_user.setWaktu_selesai(ds2.child("waktu_selesai").getValue().toString());
                                        wish_user.setMaksimal(Integer.parseInt(ds2.child("maksimal").getValue().toString()));
                                        wish_user.setVarian(ds2.child("varian").getValue().toString());
                                        wish_user.setLokasi(ds2.child("lokasi").getValue().toString());
                                        wish_user.setNama(ds2.child("nama").getValue().toString());
                                        wish_user.setIdpenjual(ds2.child("idpenjual").getValue().toString());
                                        wish_user.setDeskripsi(ds2.child("deskripsi").getValue().toString());
                                        wish_user.setKategori(ds2.child("kategori").getValue().toString());
                                        wish_user.setHarga(Integer.parseInt(ds2.child("harga").getValue().toString()));
                                        wish_user.setJenis(ds2.child("jenis").getValue().toString());
                                        kumpulanbarang.add(wish_user);
                                    }
                                }
                                rvWishlist.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                                WishlistAdapter adap=new WishlistAdapter(getApplicationContext(),kumpulanbarang);
                                rvWishlist.setAdapter(adap);
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
