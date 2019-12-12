package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.text.ChoiceFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class payment_nego extends AppCompatActivity {

    Button btnTransaksi;
    ImageButton btnPlus, btnMin;
    TextView tvNama, tvHarga;
    EditText edJumlah, edKeterangan, edAlamat;
    ImageView gambarBarang;
    NumberFormat format;
    DatabaseReference databaseReferenceTrx;
    DatabaseReference databaseReferenceUser;
    barang brg_hasil;
    Nego nego_hasil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_nego);

        btnTransaksi = findViewById(R.id.buttonPaymentNegoPesan);
        btnPlus = findViewById(R.id.imageButtonPaymentNegoPlus);
        btnMin = findViewById(R.id.imageButtonPaymentNegoMinus);
        tvNama = findViewById(R.id.textViewPaymentNegoNama);
        tvHarga = findViewById(R.id.textViewPaymentNegoHarga);
        edJumlah = findViewById(R.id.editTextPaymentNegoJumlah);
        edKeterangan = findViewById(R.id.editTextPaymentNegoKeterangan);
        edAlamat = findViewById(R.id.editTextPaymentNegoAlamat);
        gambarBarang = findViewById(R.id.imageViewPaymentNego);

        databaseReferenceTrx= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
        databaseReferenceUser= FirebaseDatabase.getInstance().getReference().child("UserDatabase");

        //get data from intent
        Intent i = getIntent();
        brg_hasil = (barang) i.getSerializableExtra("brg");
        nego_hasil = i.getParcelableExtra("nego");
        tvNama.setText(brg_hasil.getNama());

        Locale locale = new Locale("in","ID");
        format = NumberFormat.getCurrencyInstance(locale);
        tvHarga.setText("Harga: "+format.format(nego_hasil.getNominal_nego())+"");
        edJumlah.setText(1+"");
        try {
            FirebaseStorage.getInstance().getReference().child("img_barang").child(brg_hasil.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(gambarBarang);
                }
            });
        }catch (Exception e){}

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nilai_sekarang=0;
                if(edJumlah.getText().toString().equalsIgnoreCase("")){nilai_sekarang=0;}
                else{nilai_sekarang = Integer.parseInt(edJumlah.getText().toString());}
                if(nilai_sekarang-1>0){
                    edJumlah.setText((nilai_sekarang-1)+"");
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nilai_sekarang=0;
                if(edJumlah.getText().toString().equalsIgnoreCase("")){nilai_sekarang=0;}
                else{nilai_sekarang = Integer.parseInt(edJumlah.getText().toString());}
                if(brg_hasil.getMaksimal()<nilai_sekarang+1){
                    Toast.makeText(payment_nego.this, "Batas maksimal barang telah tercapai..", Toast.LENGTH_SHORT).show();
                }
                else{
                    edJumlah.setText((nilai_sekarang+1)+"");
                }
            }
        });
        TextView tvTotal = findViewById(R.id.textViewPaymentNegoTotal);
        //update total
        edJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!edJumlah.getText().toString().equalsIgnoreCase("")){
                    //kalau isi tidack kosonk
                    int total = nego_hasil.getNominal_nego()*(Integer.parseInt(edJumlah.getText().toString()));
                    String tulis_total = format.format(total);
                    tvTotal.setText("Total: "+tulis_total);
                }
            }
        });
        tvTotal.setText("Total: "+format.format(nego_hasil.getNominal_nego())+"");
        //buat transaksi disini
        btnTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kalau alamat ga kosong
                int jumlah_barang =Integer.parseInt(edJumlah.getText().toString());
                if(!edAlamat.getText().toString().equalsIgnoreCase("")&&!edJumlah.getText().toString().equalsIgnoreCase("")){
                    //cek melebih batas pesen
                    if(jumlah_barang>brg_hasil.getMaksimal()){
                        Toast.makeText(payment_nego.this, "Jumlah maksimal pemesanan: "+brg_hasil.getMaksimal(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String id_user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        final int[] saldo_user = {0}; //manggilnya saldo_user[0]
                        //cek saldo cukup?
                        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                                    //ini cocok
                                    if(ds.child("email").getValue().toString().equalsIgnoreCase(id_user)){
                                        saldo_user[0] = Integer.parseInt(ds.child("saldo").getValue().toString());
                                        int total_harga = (nego_hasil.getNominal_nego()*jumlah_barang);
                                        if(saldo_user[0] -total_harga>=0){
                                            //masih cukup
                                            Calendar waktu_sekarang  = new GregorianCalendar();
                                            String waktu = waktu_sekarang.get(Calendar.DATE)+"/"+waktu_sekarang.get(Calendar.MONTH)+"/"+waktu_sekarang.get(Calendar.YEAR)+"-"+waktu_sekarang.get(Calendar.HOUR_OF_DAY)+":"+waktu_sekarang.get(Calendar.MINUTE);
                                            //buat transaksi
                                            transaksi_class trx = new transaksi_class();
                                            trx.setId_user_trans(id_user);
                                            trx.setId_seller_trans(brg_hasil.getIdpenjual());
                                            trx.setWaktu_trans(waktu);
                                            trx.setKeterangan_trans((edKeterangan.getText().toString().equalsIgnoreCase(""))?"-":edKeterangan.getText().toString());
                                            trx.setStatus_trans("dikonfirmasi");
                                            trx.setId_barang_trans(brg_hasil.getId());
                                            trx.setVarian_pilihan(nego_hasil.getVarian());
                                            trx.setTotal_trans(total_harga+"");
                                            trx.setAlamat_pengiriman_trans(edAlamat.getText().toString());
                                            trx.setId_promo("-");
                                            trx.setJumlah_barang_trans(jumlah_barang);

                                            databaseReferenceTrx.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String Key = databaseReferenceTrx.push().getKey();
                                                    trx.setId_transaksi(Key);
                                                    databaseReferenceTrx.child(Key).setValue(trx).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(payment_nego.this, "Terimakasih, pesanan anda akan segera diproses.\n Silahkan lihat history transaksi Anda.", Toast.LENGTH_SHORT).show();
                                                            //ubah saldo user
                                                            ubahSaldoUser(saldo_user[0],total_harga);
                                                            //ubah status nego
                                                            ubahStatusNego();
                                                            //buat transaksi baru
                                                            buatTransaksi(total_harga);
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            finish();
                                        }
                                        else{
                                            //duit kurang
                                            Toast.makeText(payment_nego.this, "Maaf, saldo Anda kurang. Yuk topup dulu", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(payment_nego.this, "Mohon isi alamat dan jumlah barang", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get alamat , masukkin di edit text
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                for (DataSnapshot row:dataSnapshot.getChildren()) {
                    if(row.child("email").getValue().toString().equalsIgnoreCase(email)){
                        //masukin
                        String alamat = row.child("alamat").getValue().toString();
                        edAlamat.setText(alamat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void buatTransaksi(int total_harga) {
        DatabaseReference databaseReferenceTopup = FirebaseDatabase.getInstance().getReference().child("HistoryTopUpDatabase");
        databaseReferenceTopup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                history_wallet historyWallet = new history_wallet();
                String Key = databaseReferenceTopup.push().getKey();
                historyWallet.setId_hist_wallet(Key);
                historyWallet.setId_user_wallet(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                historyWallet.setNominal_berubah("-Rp"+total_harga);
                historyWallet.setStatus_history("Transaksi keluar");
                Calendar waktu_sekarang  = new GregorianCalendar();
                String waktu  = waktu_sekarang.get(Calendar.DATE)+"/"+waktu_sekarang.get(Calendar.MONTH)+"/"+waktu_sekarang.get(Calendar.YEAR)+" - "+waktu_sekarang.get(Calendar.HOUR)+":"+waktu_sekarang.get(Calendar.MINUTE)+" "+(waktu_sekarang.get((Calendar.AM_PM))==1?"PM":"AM");
                historyWallet.setWaktu_history(waktu);
                databaseReferenceTopup.child(Key).setValue(historyWallet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ubahStatusNego(){
        DatabaseReference databaseReference_nego =FirebaseDatabase.getInstance().getReference().child("NegoDatabase");
        databaseReference_nego.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference_nego.child(nego_hasil.getId_nego()).child("status_nego").setValue("selesai");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void ubahSaldoUser(int saldo,int total){
        int saldo_sek = saldo-total;
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        databaseReferenceUser.child(ds.child("id").getValue().toString()).child("saldo").setValue(saldo_sek+"");
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void ubahMaksBarang(String id_barang, int jumlah_terjual){

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