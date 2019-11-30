package com.example.proyek_sdp;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class cart extends AppCompatActivity {
    ArrayList<CartClass> kumpulanbarang = new ArrayList<CartClass>();
    Button bayar;
    TextView total_harga;
    RecyclerView rv_cart;
    CartClass barang_bundle;

    CartAdapter adapter;
    CartDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bayar = findViewById(R.id.bayar_cart);
        total_harga = findViewById(R.id.harga_barang_cart);
        rv_cart = findViewById(R.id.rv_cart);
        rv_cart.setHasFixedSize(true);
        rv_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        adapter = new CartAdapter(this, kumpulanbarang);
        rv_cart.setAdapter(adapter);

        db = Room.databaseBuilder(this, CartDB.class, "db_sdp").build();

        //ambil data semua dari room
        getData();
        // ambil dari bundle kalau ada
        Intent i = getIntent();
        CartClass b =  i.getParcelableExtra("barang");
        if (b != null) {
            barang_bundle =b;
        //masukin room disini

            //cek kalau ada sudahan
            boolean isAdaSudah = false;
            CartClass kalau_ada = null;
            Toast.makeText(this, kumpulanbarang.size()+"", Toast.LENGTH_SHORT).show();
            for (CartClass r:kumpulanbarang) {
                Toast.makeText(this, r.getId_barang_cart()+"=="+b.getId_barang_cart()+"", Toast.LENGTH_SHORT).show();
                if(r.getId_barang_cart().equalsIgnoreCase(b.getId_barang_cart())){
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
                    Toast.makeText(this, "Maaf, titipan barang sudah melebihi batas titipan", Toast.LENGTH_SHORT).show();
                }
            }
            //kalau gaada buat baru
            else{
                insertEntryCart(barang_bundle);
            }
            //
            Toast.makeText(this, barang_bundle.getNama_barang_cart() + " ini ", Toast.LENGTH_SHORT).show();
        }

        int total = 0;
        for (CartClass x : kumpulanbarang) {
            total += (x.getHarga_barang()*x.getJumlah_barang());
        }
        total_harga.setText("Rp " + total);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Cart");
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
            kumpulanbarang.addAll(db.cartDAO().getAllBarang());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
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
