package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class history_pembeli extends AppCompatActivity {
    ArrayList<transaksi_class> listtransaksi = new ArrayList<transaksi_class>();
    ListView lvHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pembeli_layout);
        lvHistory = findViewById(R.id.listHistoryPembeliFront);
        refresh();
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
            TextView tvjumlah = row.findViewById(R.id.tvjumlahbarang_pembeli);
            TextView tvvarian = row.findViewById(R.id.tvvarian_pembeli);
            TextView tvharga = row.findViewById(R.id.tvharga_pembeli);
            ImageView img = row.findViewById(R.id.ivImageBarang);
            TextView temp=new TextView(getApplicationContext());
            Button sudahsampai=row.findViewById(R.id.buttonsudahsampai_pembeli);

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
                else if(brg.get(position).getStatus_trans().equals("dicancel")){
                    tvstatus.setText("Dicancel");
                    tvstatus.setTextColor(Color.BLACK);
                    tvstatus.setBackgroundColor(Color.RED);
                }
                tvTglBeli.setText("Waktu : "+brg.get(position).getWaktu_trans()+"");
                tvjumlah.setText("Jumlah Barang : "+brg.get(position).getJumlah_barang_trans()+"");
                tvvarian.setText("Varian : "+brg.get(position).getVarian_pilihan()+"");
                if(brg.get(position).getId_promo().equals("-")){
                    tvharga.setText("Total : Rp"+brg.get(position).getTotal_trans()+"");
                }
                else {
                    DatabaseReference databaseReference_voucher= FirebaseDatabase.getInstance().getReference().child("Voucher");
                    databaseReference_voucher.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count=dataSnapshot.getChildrenCount();
                            for (DataSnapshot ds :dataSnapshot.getChildren()) {
                                if (brg.get(position).getId_promo().equals(ds.child("nama_promo").getValue().toString())){
                                    tvharga.setText("Total : Rp"+(Integer.parseInt(brg.get(position).getTotal_trans())-(Integer.parseInt(brg.get(position).getTotal_trans())*Integer.parseInt(ds.child("diskon_promo").getValue().toString())/100)));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
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
            sudahsampai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //update transaksi
                    String id_transaksi= brg.get(position).getId_transaksi();
                    DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
                    DatabaseReference databaseReference_user= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
                    DatabaseReference databaseReference_topup= FirebaseDatabase.getInstance().getReference().child("HistoryTopUpDatabase");
                    DatabaseReference databaseReference_voucher= FirebaseDatabase.getInstance().getReference().child("Voucher");
                    databaseReference_transaksi.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count=dataSnapshot.getChildrenCount();
                            for (DataSnapshot ds :dataSnapshot.getChildren()) {
                                if (ds.child("id_transaksi").getValue().toString().equals(id_transaksi)){
                                    transaksi_class update_trans=new transaksi_class();
                                    update_trans.setAlamat_pengiriman_trans(ds.child("alamat_pengiriman_trans").getValue().toString());
                                    update_trans.setId_barang_trans(ds.child("id_barang_trans").getValue().toString());
                                    update_trans.setId_promo(ds.child("id_promo").getValue().toString());
                                    update_trans.setId_seller_trans(ds.child("id_seller_trans").getValue().toString());
                                    update_trans.setId_user_trans(ds.child("id_user_trans").getValue().toString());
                                    update_trans.setJumlah_barang_trans(Integer.parseInt(ds.child("jumlah_barang_trans").getValue().toString()));
                                    update_trans.setKeterangan_trans(ds.child("keterangan_trans").getValue().toString());
                                    update_trans.setStatus_trans("selesai");
                                    update_trans.setTotal_trans(ds.child("total_trans").getValue().toString());
                                    update_trans.setVarian_pilihan(ds.child("varian_pilihan").getValue().toString());
                                    update_trans.setWaktu_trans(ds.child("waktu_trans").getValue().toString());
                                    update_trans.setId_transaksi(ds.child("id_transaksi").getValue().toString());
                                    databaseReference_transaksi.child(ds.getKey()).setValue(update_trans).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Anda Menyatakan Barang Telah sampai!", Toast.LENGTH_SHORT).show();
                                            refresh();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //update saldo
                    databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                if(ds2.child("email").getValue().toString().equals(brg.get(position).getId_seller_trans())){
                                    //update saldo
                                    user baru=new user();
                                    baru.setNama(ds2.child("nama").getValue().toString());
                                    baru.setProfil_picture(Integer.parseInt(ds2.child("profil_picture").getValue().toString()));
                                    baru.setSaldo(Integer.parseInt(ds2.child("saldo").getValue().toString())+Integer.parseInt(brg.get(position).getTotal_trans()));
                                    baru.setRating(Float.parseFloat(ds2.child("rating").getValue().toString()));
                                    baru.setPhone(ds2.child("phone").getValue().toString());
                                    baru.setEmail(ds2.child("email").getValue().toString());
                                    baru.setId(ds2.child("id").getValue().toString());
                                    baru.setBirthdate(ds2.child("birthdate").getValue().toString());
                                    baru.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                                    baru.setPassword(ds2.child("password").getValue().toString());
                                    baru.setVerifikasi_ktp(Integer.parseInt(ds2.child("verifikasi_ktp").getValue().toString()));
                                    databaseReference_user.child(ds2.getKey()).setValue(baru);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //tambah di top up history
                    String Key2 = databaseReference_topup.push().getKey();
                    history_wallet hist_baru=new history_wallet();
                    hist_baru.setNominal_berubah("+Rp"+brg.get(position).getTotal_trans());
                    hist_baru.setId_hist_wallet(Key2);
                    hist_baru.setId_user_wallet(brg.get(position).getId_seller_trans());
                    hist_baru.setStatus_history("Transaksi Masuk Dari Jual Barang");
                    Date dt = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String time2 = sdf.format(dt);
                    Calendar now2 = Calendar.getInstance();
                    hist_baru.setWaktu_history(now2.get(Calendar.DAY_OF_MONTH)+"/"+now2.get(Calendar.MONTH)+"/"+now2.get(Calendar.YEAR)+"-"+time2);
                    databaseReference_topup.child(Key2).setValue(hist_baru).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    Intent move=new Intent(getApplicationContext(),reviewdanratingActivity.class);
                    move.putExtra("iduser",brg.get(position).getId_seller_trans());
                    startActivity(move);
                }
            });

            if(!brg.get(position).getStatus_trans().equals("dikonfirmasi")){
                sudahsampai.setVisibility(View.GONE);
            }
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
    public void refresh(){
        DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
        databaseReference_transaksi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                listtransaksi.clear();
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
    }
}
