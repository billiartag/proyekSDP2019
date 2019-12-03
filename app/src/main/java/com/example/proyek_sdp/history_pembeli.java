package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class history_pembeli extends AppCompatActivity {
    ArrayList<transaksi_class> listtransaksi = new ArrayList<transaksi_class>();
    ListView lvHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pembeli_layout);
        lvHistory = findViewById(R.id.listHistoryPembeliFront);
        //listBarang.add(new barangHistory("sepeda1",0,"edwin","Jakartah","31 Februari 2021"));
        DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
        databaseReference_transaksi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("id_user_trans").getValue().toString())){
                        transaksi_class trans=new transaksi_class();
                        trans.setId_transaksi(ds.child("id_transaksi").getValue().toString());
                        trans.setWaktu_trans(ds.child("waktu_trans").getValue().toString());
                        trans.setVarian_pilihan(ds.child("varian_pilihan").getValue().toString());
                        trans.setTotal_trans(ds.child("total_trans").getValue().toString());
                        trans.setStatus_trans(ds.child("status_trans").getValue().toString());
                        trans.setKeterangan_trans(ds.child("keterangan_trans").getValue().toString());
                        trans.setJumlah_barang_trans(Integer.parseInt(ds.child("jumlah_barang_trans").getValue().toString()));
                        trans.setId_user_trans(ds.child("id_user_trans").getValue().toString());
                        trans.setId_seller_trans(ds.child("id_seller_trans").getValue().toString());
                        trans.setId_promo(ds.child("id_promo").getValue().toString());
                        trans.setId_barang_trans(ds.child("id_barang_trans").getValue().toString());
                        trans.setAlamat_pengiriman_trans(ds.child("alamat_pengiriman_trans").getValue().toString());
                        listtransaksi.add(trans);
                    }
                }
                adapter adap = new adapter(getApplicationContext(),listtransaksi);
                lvHistory.setAdapter(adap);
                lvHistory.setDividerHeight(10);
                if(listtransaksi.size()==0){
                    Toast.makeText(history_pembeli.this, "Anda Tidak Memiliki History Pembelian!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ActionBar ab=getSupportActionBar();
        ab.setTitle("Aku titip");
    }


    class adapter extends ArrayAdapter<transaksi_class>{
        ArrayList<transaksi_class> brg;

        public adapter(Context context, ArrayList<transaksi_class>a) {
            super(context, R.layout.list_history_front_pembeli_layout,a);
            this.brg = a;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_history_front_pembeli_layout,parent,false);
            TextView tvnamabarang = row.findViewById(R.id.tvNamaBarang);
            TextView tvstatus = row.findViewById(R.id.tvStatusBarang);
            TextView tvPenjual = row.findViewById(R.id.tvPenjual);
            TextView tvLokasi = row.findViewById(R.id.tvLokasi);
            TextView tvTglBeli = row.findViewById(R.id.tvTglBeli);
            ImageView img = row.findViewById(R.id.ivImageBarang);

            if(brg!=null){
                if(brg.get(position).getStatus_trans().equals("pending")){
                    tvstatus.setText("Belum Dikonfirmasi");
                    tvstatus.setTextColor(Color.BLACK);
                    tvstatus.setBackgroundColor(Color.GRAY);
                }
                else if(brg.get(position).getStatus_trans().equals("dikonfirmasi")){
                    tvstatus.setText("Dikirim");
                    tvstatus.setTextColor(Color.BLACK);
                    tvstatus.setBackgroundColor(Color.YELLOW);
                }
                else if(brg.get(position).getStatus_trans().equals("selesai")){
                    tvstatus.setText("Sudah sampai");
                    tvstatus.setTextColor(Color.BLACK);
                    tvstatus.setBackgroundColor(Color.GREEN);
                }
                tvTglBeli.setText(brg.get(position).getWaktu_trans()+"");
                DatabaseReference databaseReference_barang= FirebaseDatabase.getInstance().getReference().child("BarangDatabase");
                databaseReference_barang.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count=dataSnapshot.getChildrenCount();
                        for (DataSnapshot ds :dataSnapshot.getChildren()) {
                            if (brg.get(position).getId_barang_trans().equals(ds.child("id").getValue().toString())){
                                tvnamabarang.setText("Barang: "+ds.child("nama").getValue().toString());
                                tvLokasi.setText("Lokasi: "+ds.child("lokasi").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                FirebaseStorage.getInstance().getReference().child("img_barang").child(brg.get(position).getId_barang_trans()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (getApplicationContext()!=null){
                            Glide.with(getApplicationContext()).load(uri).into(img);
                        }
                    }
                });
                DatabaseReference databaseReference_user= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
                databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count=dataSnapshot.getChildrenCount();
                        for (DataSnapshot ds :dataSnapshot.getChildren()) {
                            if (brg.get(position).getId_seller_trans().equals(ds.child("email").getValue().toString())){
                                tvPenjual.setText("Penjual: "+ds.child("nama").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            tvnamabarang.setTextColor(Color.BLACK);
            tvPenjual.setTextColor(Color.BLACK);
            tvLokasi.setTextColor(Color.BLACK);
            tvTglBeli.setTextColor(Color.BLACK);

            return row;

        }

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
