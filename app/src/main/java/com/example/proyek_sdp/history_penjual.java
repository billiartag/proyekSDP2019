package com.example.proyek_sdp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class history_penjual extends AppCompatActivity {

    ArrayList<transaksi_class>listtransaksi = new ArrayList<transaksi_class>();
    ListView lvHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoy_penjual_layout);
        lvHistory = findViewById(R.id.lvHistoryPenjualFront);
        refresh();//untuk refresh data list viewnya
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Dititipin");
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

    class adapter extends ArrayAdapter<transaksi_class> {
        ArrayList<transaksi_class> brg = new ArrayList<transaksi_class>();
        Context ctx;
        public adapter(Context context, ArrayList<transaksi_class>a) {
            super(context, R.layout.list_history_front_pembeli_layout,a);
            this.brg = a;
            ctx=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_history_penjual_layout,parent,false);
            TextView tvNamaBarang = row.findViewById(R.id.tvNamaBarang);
            TextView tvStatus = row.findViewById(R.id.tvStatus);
            TextView tvId = row.findViewById(R.id.tvId);
            ImageView img = row.findViewById(R.id.ivImageBarang);
            TextView tv_pembeli = row.findViewById(R.id.textViewPembeli);
            Button btnCancel = row.findViewById(R.id.buttonPenjualCancelOrder);
            Button btnAccept = row.findViewById(R.id.buttonpenjualacceptorder);

            if(brg.get(position).getStatus_trans().equals("pending")){
                tvStatus.setText("Status : Pending");
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setBackgroundColor(Color.GRAY);
            }
            else if(brg.get(position).getStatus_trans().equals("dikonfirmasi")){
                tvStatus.setText("Status : Dikirim");
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setBackgroundColor(Color.YELLOW);
            }
            else if(brg.get(position).getStatus_trans().equals("selesai")){
                tvStatus.setText("Status : Sudah Selesai");
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setBackgroundColor(Color.GREEN);
            }
            else if(brg.get(position).getStatus_trans().equals("dicancel")){
                tvStatus.setText("Status : Dicancel");
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setBackgroundColor(Color.RED);
            }
            DatabaseReference databaseReference_barang= FirebaseDatabase.getInstance().getReference().child("BarangDatabase");
            databaseReference_barang.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count=dataSnapshot.getChildrenCount();
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if (brg.get(position).getId_barang_trans().equals(ds.child("id").getValue().toString())){
                            tvNamaBarang.setText("Nama Barang : "+ds.child("nama").getValue().toString());
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
                        if (brg.get(position).getId_user_trans().equals(ds.child("email").getValue().toString())){
                            tv_pembeli.setText("Pembeli : "+ds.child("nama").getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            tvId.setText("ID Barang : "+brg.get(position).getId_barang_trans());
            tvId.setTextColor(Color.GREEN);
            tvId.setTypeface(null, Typeface.BOLD_ITALIC);
            tvNamaBarang.setTextColor(Color.BLACK);
            tvId.setTextColor(Color.BLACK);
            tv_pembeli.setTextColor(Color.BLACK);
            //kalau sudah selesai / sudah selesai dikirim
            if(!brg.get(position).getStatus_trans().equals("pending")){
                btnCancel.setVisibility(View.GONE);
                btnAccept.setVisibility(View.GONE);
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id_transaksi= brg.get(position).getId_transaksi();
                    DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
                    DatabaseReference databaseReference_user= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
                    DatabaseReference databaseReference_topup= FirebaseDatabase.getInstance().getReference().child("HistoryTopUpDatabase");
                    DatabaseReference databaseReference_voucher= FirebaseDatabase.getInstance().getReference().child("Voucher");
                    //update transaksi status
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
                                    update_trans.setStatus_trans("dicancel");
                                    update_trans.setTotal_trans(ds.child("total_trans").getValue().toString());
                                    update_trans.setVarian_pilihan(ds.child("varian_pilihan").getValue().toString());
                                    update_trans.setWaktu_trans(ds.child("waktu_trans").getValue().toString());
                                    update_trans.setId_transaksi(ds.child("id_transaksi").getValue().toString());
                                    databaseReference_transaksi.child(ds.getKey()).setValue(update_trans).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ctx, "Barang Berhasil Dicancel!", Toast.LENGTH_SHORT).show();
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
                            for (DataSnapshot ds :dataSnapshot2.getChildren()) {
                                if(ds.child("email").getValue().toString().equals(brg.get(position).getId_user_trans())){
                                    //update saldo
                                    user baru=new user();
                                    baru.setNama(ds.child("nama").getValue().toString());
                                    baru.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                                    if(brg.get(position).getId_promo().equals("-")){
                                        baru.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString())+Integer.parseInt(brg.get(position).getTotal_trans()));
                                        baru.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                                        baru.setPhone(ds.child("phone").getValue().toString());
                                        baru.setEmail(ds.child("email").getValue().toString());
                                        baru.setId(ds.child("id").getValue().toString());
                                        baru.setBirthdate(ds.child("birthdate").getValue().toString());
                                        baru.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                                        baru.setPassword(ds.child("password").getValue().toString());
                                        baru.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                                        baru.setAlamat(ds.child("alamat").getValue().toString());
                                        baru.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                                        databaseReference_user.child(ds.getKey()).setValue(baru);
                                    }
                                    else {
                                        databaseReference_voucher.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                                    if(ds2.child("nama_promo").getValue().toString().equals(brg.get(position).getId_promo())){
                                                        baru.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString())+(Integer.parseInt(brg.get(position).getTotal_trans())-(Integer.parseInt(brg.get(position).getTotal_trans())*Integer.parseInt(ds2.child("diskon_promo").getValue().toString())/100)));
                                                        baru.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                                                        baru.setPhone(ds.child("phone").getValue().toString());
                                                        baru.setEmail(ds.child("email").getValue().toString());
                                                        baru.setId(ds.child("id").getValue().toString());
                                                        baru.setBirthdate(ds.child("birthdate").getValue().toString());
                                                        baru.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                                                        baru.setPassword(ds.child("password").getValue().toString());
                                                        baru.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                                                        databaseReference_user.child(ds.getKey()).setValue(baru);
                                                    }
                                                }
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
                    //tambahkan history top up
                    String Key2 = databaseReference_topup.push().getKey();
                    history_wallet hist_baru=new history_wallet();
                    if(brg.get(position).getId_promo().equals("-")){
                        hist_baru.setNominal_berubah("+Rp"+brg.get(position).getTotal_trans());
                        hist_baru.setId_hist_wallet(Key2);
                        hist_baru.setId_user_wallet(brg.get(position).getId_user_trans());
                        hist_baru.setStatus_history("Refund Dari Barang Yang Di Cancel");
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
                    }
                    else {
                        databaseReference_voucher.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                    if(ds2.child("nama_promo").getValue().toString().equals(brg.get(position).getId_promo())){
                                        hist_baru.setNominal_berubah("+Rp"+(Integer.parseInt(brg.get(position).getTotal_trans())-(Integer.parseInt(brg.get(position).getTotal_trans())*Integer.parseInt(ds2.child("diskon_promo").getValue().toString())/100)));
                                        hist_baru.setId_hist_wallet(Key2);
                                        hist_baru.setId_user_wallet(brg.get(position).getId_user_trans());
                                        hist_baru.setStatus_history("Refund Dari Barang Yang Di Cancel");
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
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id_transaksi= brg.get(position).getId_transaksi();
                    DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
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
                                    update_trans.setStatus_trans("dikonfirmasi");
                                    update_trans.setTotal_trans(ds.child("total_trans").getValue().toString());
                                    update_trans.setVarian_pilihan(ds.child("varian_pilihan").getValue().toString());
                                    update_trans.setWaktu_trans(ds.child("waktu_trans").getValue().toString());
                                    update_trans.setId_transaksi(ds.child("id_transaksi").getValue().toString());
                                    databaseReference_transaksi.child(ds.getKey()).setValue(update_trans).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ctx, "Barang Berhasil Dikonfirmasi!", Toast.LENGTH_SHORT).show();
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
                }
            });
            return row;

        }
    }
    public void refresh(){
        //add barang dari list firebase
        DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
        databaseReference_transaksi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                listtransaksi.clear();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("id_seller_trans").getValue().toString())){
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
                lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(view.getContext(),detail_history_penjual.class);
                        startActivity(intent);
                    }
                });
                if(listtransaksi.size()==0){
                    Toast.makeText(history_penjual.this, "Anda Tidak Memiliki History Penjualan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
