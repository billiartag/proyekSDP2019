package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class detail_feed extends AppCompatActivity {
    ImageView img,imghati;
    TextView tipe;
    TextView nama;
    TextView harga;
    TextView deskripsi;
    TextView pemilik;
    TextView durasi;
    TextView max;
    TextView tvberatbarang_detailfeed;
    Button beli;
    Button nego;
    RadioGroup radioGroup_varian_feed;
    String hasil_radio_varian="";
    boolean sudahdifavorite=false;
    DatabaseReference databaseReference_wishlist;
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
        radioGroup_varian_feed=findViewById(R.id.radioGroup_varian_feed);
        tvberatbarang_detailfeed=findViewById(R.id.tvberatbarang_detailfeed);


        //start program
        //isi data
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
        deskripsi.setText("Deskripsi : \n"+x.getDeskripsi());
        tvberatbarang_detailfeed.setText("Berat Barang : "+x.getBerat()+" Gram");
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
        //untuk ngatur warna radio buttonnya
        String[] split_varian=x.getVarian().split(",");
        for (int i=0;i<split_varian.length;i++){
            RadioButton varian_baru=new RadioButton(getApplicationContext());
            varian_baru.setText(split_varian[i]);
            varian_baru.setTextColor(Color.BLACK);
            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] colors = new int[] {
                    Color.BLACK,
                    Color.RED,
                    Color.GREEN,
                    Color.BLUE
            };
            ColorStateList list_color=new ColorStateList(states,colors);
            varian_baru.setButtonTintList(list_color);
            varian_baru.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    hasil_radio_varian=varian_baru.getText().toString();
                    Toast.makeText(detail_feed.this, hasil_radio_varian, Toast.LENGTH_SHORT).show();
                }
            });
            radioGroup_varian_feed.addView(varian_baru);
        }
        durasi.setText("Durasi : "+x.getWaktu_selesai());
        max.setText("Max barang yang dapat dipesan : "+x.getMaksimal());
        if (tipe.getText().toString().equals("Flash Sale")){
            tipe.setBackgroundColor(Color.parseColor("#FB8C00"));
            tipe.setTextColor(Color.BLACK);
            beli.setText("Tambah Ke Keranjang");
        }
        else if (tipe.getText().toString().equals("Pre Order")){
            tipe.setBackgroundColor(Color.BLACK);
            tipe.setTextColor(Color.WHITE);
            beli.setText("Lakukan Pre Order");
        }
        //wishlist
        databaseReference_wishlist= FirebaseDatabase.getInstance().getReference().child("WishListDatabase");
        databaseReference_wishlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_barang").getValue().toString().equals(x.getId()) && ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        imghati.setImageResource(R.drawable.ic_hatimerah_black_24dp);
                        sudahdifavorite = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imghati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sudahdifavorite == false) {
                    //isi wishlist ke database
                    String Key = databaseReference_wishlist.push().getKey();
                    WishList_class wish_baru=new WishList_class();
                    wish_baru.setId_user(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    wish_baru.setId_wishlist(Key);
                    wish_baru.setId_barang(x.getId());
                    databaseReference_wishlist.child(Key).setValue(wish_baru).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            imghati.setImageResource(R.drawable.ic_hatimerah_black_24dp);
                            Toast.makeText(getApplicationContext(), "Berhasil Di Tambah Ke WishList!", Toast.LENGTH_SHORT).show();
                            sudahdifavorite = true;
                        }
                    });
                }else if(sudahdifavorite == true) {
                    databaseReference_wishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count=dataSnapshot.getChildrenCount();
                            for (DataSnapshot ds :dataSnapshot.getChildren()) {
                                if(ds.child("id_barang").getValue().toString().equals(x.getId()) && ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                    databaseReference_wishlist.child(ds.getKey()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            imghati.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                            Toast.makeText(getApplicationContext(), "Berhasil Menghapus Barang Dari Wishlist!", Toast.LENGTH_SHORT).show();
                                            sudahdifavorite=false;
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rb =(RadioButton)findViewById(radioGroup_varian_feed.getCheckedRadioButtonId());
                if(radioGroup_varian_feed.getCheckedRadioButtonId()!=-1){
                    Toast.makeText(getApplicationContext(), "Tambah ke Keranjang Berhasil!", Toast.LENGTH_SHORT).show();
                    //intent ke cart
                    finish();
                    Intent i = new Intent(detail_feed.this, cart.class);
                    CartClass temp = new CartClass(x.getId(),x.getNama(),x.getWaktu_selesai(),x.getHarga(), 1 ,x.getMaksimal(),rb.getText().toString());
                    i.putExtra("barang", temp);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Anda Harus Memilih Varian Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rb =(RadioButton)findViewById(radioGroup_varian_feed.getCheckedRadioButtonId());
                if(radioGroup_varian_feed.getCheckedRadioButtonId()!=-1) {
                    Intent i = new Intent(detail_feed.this, nego_user.class);
                    //isi param lempar disini
                    i.putExtra("jenis_nego", "baru");
                    i.putExtra("barang", x);
                    i.putExtra("varian_nego", rb.getText().toString());//masukkin pilihan varian disini
                    startActivity(i);
                }
            }
        });
        CountDownTimer timer=new CountDownTimer(999999999, 1000) {
            @Override
            public void onTick(long l) {
                String waktu=x.getWaktu_selesai();
                String[] waktu_split=waktu.split(":");
                if(waktu_split.length>1){
                    int jam_selesai=Integer.parseInt(waktu_split[0]);
                    int menit_selesai=Integer.parseInt(waktu_split[1]);
                    int detik_selesai=Integer.parseInt(waktu_split[2]);
                    int total_waktu_selesai=(jam_selesai*3600) + (menit_selesai*60) + detik_selesai;
                    Calendar now = Calendar.getInstance();
                    int jam_mulai=now.get(Calendar.HOUR_OF_DAY);
                    int menit_mulai=now.get(Calendar.MINUTE);
                    int detik_mulai=now.get(Calendar.SECOND);
                    int total_waktu_mulai=(jam_mulai*3600) + (menit_mulai*60) + detik_mulai;
                    if(total_waktu_selesai-total_waktu_mulai>0){
                        durasi.setText("Sisa Waktu : "+formatSeconds(total_waktu_selesai-total_waktu_mulai));
                    }
                    else {
                        durasi.setText("expired");
                    }
                }
                else {
                    durasi.setText("Mulai : "+x.getWaktu_mulai()+" - "+x.getWaktu_selesai());
                }
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        }.start();
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
    public static String formatSeconds(int timeInSeconds)
    {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }
}
