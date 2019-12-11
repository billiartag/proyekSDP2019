package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SeeSeller extends AppCompatActivity {

    ArrayList<user> kumpulanuser = new ArrayList<user>();
    RecyclerView rv_seller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_seller);
        rv_seller = findViewById(R.id.rv_seller_seeall);


        DatabaseReference databaseReference;
        //cetak icon top seller
        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds!=null){
                        if(!ds.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            //cari rating
                            FirebaseDatabase.getInstance().getReference().child("RatingAndReviewDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    long count=dataSnapshot2.getChildrenCount();
                                    int i=0;
                                    float hasil_rating=0;
                                    for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                        if(ds2.child("id_user").getValue().toString().equals(ds.child("email").getValue().toString())){
                                            i++;
                                            hasil_rating=hasil_rating+Float.parseFloat(ds2.child("rating").getValue().toString());
                                        }
                                    }
                                    //isi rating
                                    hasil_rating=hasil_rating/i;
                                    user baru=new user();
                                    baru.setId(ds.child("id").getValue().toString());
                                    baru.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                                    baru.setBirthdate(ds.child("birthdate").getValue().toString());
                                    baru.setEmail(ds.child("email").getValue().toString());
                                    baru.setPhone(ds.child("phone").getValue().toString());
                                    baru.setPassword(ds.child("password").getValue().toString());
                                    baru.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                                    baru.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString()));
                                    if(String.valueOf(hasil_rating).equals("NaN")){
                                        baru.setRating(0);
                                    }
                                    else {
                                        baru.setRating(hasil_rating);
                                    }
                                    baru.setNama(ds.child("nama").getValue().toString());
                                    baru.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                                    baru.setAlamat(ds.child("alamat").getValue().toString());
                                    baru.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                                    databaseReference.child(ds.getKey()).setValue(baru);
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                            long count=dataSnapshot.getChildrenCount();
                                            kumpulanuser.clear();
                                            for (DataSnapshot ds3 :dataSnapshot3.getChildren()) {
                                                if(ds3!=null){
                                                    if(!ds3.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                                        user baru=new user();
                                                        baru.setId(ds3.child("id").getValue().toString());
                                                        baru.setVerifikasi_ktp(Integer.parseInt(ds3.child("verifikasi_ktp").getValue().toString()));
                                                        baru.setBirthdate(ds3.child("birthdate").getValue().toString());
                                                        baru.setEmail(ds3.child("email").getValue().toString());
                                                        baru.setPhone(ds3.child("phone").getValue().toString());
                                                        baru.setRating(Float.parseFloat(ds3.child("rating").getValue().toString()));
                                                        baru.setNama(ds3.child("nama").getValue().toString());
                                                        baru.setProfil_picture(Integer.parseInt(ds3.child("profil_picture").getValue().toString()));
                                                        baru.setAlamat(ds.child("alamat").getValue().toString());
                                                        baru.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                                                        kumpulanuser.add(baru);
                                                    }
                                                }
                                            }
                                            Collections.sort(kumpulanuser,user.sortdescrating);
                                            rv_seller.setHasFixedSize(true);
                                            rv_seller.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                                            TopuserSeeallAdapter adaptertopuser = new TopuserSeeallAdapter(getApplicationContext(), kumpulanuser);
                                            adaptertopuser.setOnItemClickListener(new OnItemClickListener() {
                                                @Override
                                                public void onClick(View view, int position) {
                                                    user x=kumpulanuser.get(position);
                                                    Bundle b = new Bundle();
                                                    b.putSerializable("user", x);
                                                    Intent intent = new Intent(getApplicationContext(), detailprofil.class);
                                                    intent.putExtras(b);
                                                    startActivity(intent);
                                                    Log.d("masik sini","haoi");
                                                }
                                            });
                                            rv_seller.setAdapter(adaptertopuser);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setTitle("Top Sellers");
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
