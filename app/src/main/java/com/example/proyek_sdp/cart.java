package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.Update;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class cart extends AppCompatActivity {
    ArrayList<CartClass> kumpulanbarang = new ArrayList<CartClass>();
    Button bayar;
    TextView total_harga,promo,diskon;
    RecyclerView rv_cart;
    RecyclerView rv_promo_cart;
    CartClass barang_bundle;
    CartAdapter adapter;
    CartDB db;
    Boolean sudahSelesaiInsert;
    EditText edlokasi_cart;
    Button btnlokasi_cart;
    int totalasli=-1;
    int totalinsert=-1;
    int tekanpromo=-1;
    ArrayList<voucher>list_voucher=new ArrayList<voucher>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bayar = findViewById(R.id.bayar_cart);
        total_harga = findViewById(R.id.harga_barang_cart);
        rv_cart = findViewById(R.id.rv_cart);
        rv_promo_cart = findViewById(R.id.rv_promo_cart);
        promo = findViewById(R.id.nama_promo_cart);
        diskon = findViewById(R.id.jumlah_diskon_cart);

        //start program
        if(getIntent().hasExtra("lokasi_cart")){
            edlokasi_cart.setText(getIntent().getExtras().getString("lokasi_cart"));
        }
        //cetak promo
        FirebaseDatabase.getInstance().getReference().child("Voucher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("status_promo").getValue().toString().equals("1")){
                        voucher x=new voucher();
                        x.setNama_promo(ds.child("nama_promo").getValue().toString());
                        x.setDeskripsi_promo(ds.child("deskripsi_promo").getValue().toString());
                        x.setDiskon_promo(ds.child("diskon_promo").getValue().toString());
                        x.setMulai_promo(ds.child("mulai_promo").getValue().toString());
                        x.setSelesai_promo(ds.child("selesai_promo").getValue().toString());
                        x.setStatus_promo(ds.child("status_promo").getValue().toString());
                        list_voucher.add(x);
                    }
                }
                rv_promo_cart.setHasFixedSize(true);
                rv_promo_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                ListPromoAdapter adapter = new ListPromoAdapter(getApplicationContext(), list_voucher);
                rv_promo_cart.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        promo.setText("Promo : "+list_voucher.get(position).getNama_promo());
                        diskon.setText("Diskon : "+list_voucher.get(position).getDiskon_promo()+"%");
                        total_harga.setText("Rp "+(totalasli-(totalasli*(Integer.parseInt(list_voucher.get(position).getDiskon_promo()))/100)));
                        totalinsert=totalasli-(totalasli*(Integer.parseInt(list_voucher.get(position).getDiskon_promo()))/100);
                        tekanpromo=position;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //buat rv untuk cart
        rv_cart.setHasFixedSize(true);
        rv_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        adapter = new CartAdapter(this, kumpulanbarang);
        rv_cart.setAdapter(adapter);
        db = Room.databaseBuilder(this, CartDB.class, "db_sdp").build();

        sudahSelesaiInsert = false;
        //ambil data semua dari room
        getData();
        //tekan button bayar
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
                for(int i=0;i<kumpulanbarang.size();i++){
                    String Key = databaseReference_transaksi.push().getKey();
                    transaksi_class trans_baru=new transaksi_class();
                    trans_baru.setId_barang_trans(kumpulanbarang.get(i).getId_barang_cart());
                    trans_baru.setAlamat_pengiriman_trans("");
                    trans_baru.setId_promo(list_voucher.get(tekanpromo).getNama_promo());
                    trans_baru.setId_seller_trans(kumpulanbarang.get(i).getEmail_user());
                    trans_baru.setId_user_trans(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    trans_baru.setJumlah_barang_trans(kumpulanbarang.get(i).getJumlah_barang());
                    trans_baru.setKeterangan_trans("Beli Dari Cart");
                    trans_baru.setStatus_trans("pending");
                    trans_baru.setTotal_trans(totalinsert+"");
                    trans_baru.setVarian_pilihan(kumpulanbarang.get(i).getVarian_barang());
                    Calendar now = Calendar.getInstance();
                    trans_baru.setWaktu_trans(now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.MONTH)+"/"+now.get(Calendar.YEAR));
                    databaseReference_transaksi.child(Key).setValue(trans_baru).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
                Toast.makeText(cart.this, "pembayaran berhasil,..silahkan tunggu konfirmasi Jasa Titip...!", Toast.LENGTH_SHORT).show();
            }
        });
        //tekan button lokasi
        edlokasi_cart.setFocusable(false);
        btnlokasi_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(), google_maps_current_location.class);
                move.putExtra("cart","");
                startActivity(move);
            }
        });
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Cart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_cart, menu);
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                        menu.getItem(0).setTitle("Saldo : "+Integer.parseInt(ds.child("saldo").getValue().toString()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public void getData(){
        new getAllCart().execute();
    }
    public void insertEntryCart(CartClass obj){
        new insertEntryCart().execute(obj);
    }
    public void deleteEntryCart(CartClass obj){
        new deleteCart().execute(obj);
    }
    public void updateEntryCart(CartClass obj){
        new UpdateCart().execute(obj);
    }
    private class getAllCart extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            kumpulanbarang.clear();
            kumpulanbarang.addAll(db.cartDAO().getAllBarang(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            //kalau sudah selesai masuk bool true in

            if(!sudahSelesaiInsert){
                // ambil dari bundle kalau ada
                Intent i = getIntent();
                CartClass b =  i.getParcelableExtra("barang");
                if (b != null) {
                    barang_bundle =b;
                    //masukin room disini
                    //cek kalau ada sudahan
                    boolean isAdaSudah = false;
                    CartClass kalau_ada = null;
                    for (CartClass r:kumpulanbarang) {
                        if(r.getId_barang_cart().equalsIgnoreCase(b.getId_barang_cart())&&r.getVarian_barang().equalsIgnoreCase(b.getVarian_barang())){
                            isAdaSudah=true;
                            kalau_ada = r;
                            break;
                        }
                    }
                    //kalau ada
                    if(isAdaSudah){
                        if(kalau_ada.getJumlah_barang()+1<=kalau_ada.getJumlah_maks_barang()){
                            kalau_ada.setJumlah_barang(kalau_ada.getJumlah_barang()+1);
                            updateEntryCart(kalau_ada);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Maaf, titipan barang sudah melebihi batas titipan", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //kalau gaada buat baru
                    else{
                        insertEntryCart(barang_bundle);
                    }
                    //
                }
                sudahSelesaiInsert = true;
            }
            int total = 0;
            for (CartClass x : kumpulanbarang) {
                total += (x.getHarga_barang()*x.getJumlah_barang());
            }
            totalasli=total;
            if(tekanpromo!=-1){
                total_harga.setText("Rp "+(totalasli-(totalasli*(Integer.parseInt(list_voucher.get(tekanpromo).getDiskon_promo()))/100)));
                totalinsert=totalasli-(totalasli*(Integer.parseInt(list_voucher.get(tekanpromo).getDiskon_promo()))/100);
            }
            else {
                total_harga.setText("Rp " + total);
                totalinsert=total;
            }

        }
    }
    private class deleteCart extends AsyncTask<CartClass,Void,Void>{

        @Override
        protected Void doInBackground(CartClass... barangs) {
            db.cartDAO().deleteBarang(barangs[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getData();
        }
    }
    private class insertEntryCart extends AsyncTask<CartClass,Void,Void>{

        @Override
        protected Void doInBackground(CartClass... barangs) {
            db.cartDAO().addNewBarang(barangs[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getData();
        }
    }
    private class UpdateCart extends AsyncTask<CartClass,Void,Void>{
        @Override
        protected Void doInBackground(CartClass... cartClasses) {
            db.cartDAO().updateBarang(cartClasses[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getData();
        }
    }
}
