package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class nego_user extends AppCompatActivity {

    TextView namaBarang;
    TextView hargaBarang;
    TextView sisaNego;
    TextView varianNego;
    EditText hargaBaru;
    ImageView gambarBarang;
    Button btnNego;

    ArrayList<barang> list_barang;
    Boolean nego_baru;
    String id_nego;
    int sisa_nego;
    barang barang_nego_sek;
    String jenis_nego;
    String status_nego_db;
    String varian_nego;

    DatabaseReference databaseReference_nego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nego_user);
        namaBarang=findViewById(R.id.textViewNegoNamaBarang);
        hargaBarang=findViewById(R.id.textViewNegoHargaBarang);
        sisaNego=findViewById(R.id.textViewNegoBatas);
        varianNego=findViewById(R.id.textViewNegoVarianBarang);
        hargaBaru=findViewById(R.id.editTextNegoHarga);
        gambarBarang=findViewById(R.id.imageViewBarangNego);
        btnNego=findViewById(R.id.buttonNegoSubmit);

        nego_baru=true;
        sisa_nego=-1;
        id_nego="";
        status_nego_db="-";

        databaseReference_nego= FirebaseDatabase.getInstance().getReference().child("NegoDatabase");

        Intent i = getIntent();
        if(i.getExtras()!=null){
            jenis_nego = i.getStringExtra("jenis_nego");
            barang_nego_sek = (barang) i.getSerializableExtra("barang");
            varian_nego = i.getStringExtra("varian_nego");
            id_nego = i.getStringExtra("idnego");

            //ambil dari db
            databaseReference_nego.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                //cek data dulu
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot row :dataSnapshot.getChildren()) {
                        //cek apakah sudah ada nego dg barang tsb, yang dilakuin user tsb, dan variannya sama
                        if(
                            barang_nego_sek.getId().equalsIgnoreCase(row.child("id_barang_nego").getValue().toString())
                            &&
                            FirebaseAuth.getInstance().getCurrentUser().getEmail().equalsIgnoreCase(row.child("id_user_nego").getValue().toString())
                            &&
                            varian_nego.equalsIgnoreCase(row.child("varian").getValue().toString())
                        ){
                            //kalau ada..
                            status_nego_db = row.child("status_nego").getValue().toString();
                            sisa_nego = Integer.parseInt(row.child("sisa_nego").getValue().toString());
                            jenis_nego="lama";
                        }
                    }
                    sisaNego.setText("Sisa nego: "+sisa_nego);
                    if(jenis_nego.equalsIgnoreCase("lama")){
                        //karna sudah ada sebelummnya, maka tampilin statusnya
                        nego_baru=false;
                        if(status_nego_db.equalsIgnoreCase("pending")){
                            //pending
                            btnNego.setEnabled(false);
                            hargaBaru.setEnabled(false);
                            Toast.makeText(nego_user.this, "Silahkan tunggu balasan nego anda...", Toast.LENGTH_SHORT).show();
                        }
                        else if(status_nego_db.equalsIgnoreCase("tolak")){
                            //dibatalin
                            if(sisa_nego>0){
                                //masih bisa nego lagi
                                Toast.makeText(nego_user.this, "Nego sebelumnya ditolak, yuk negoin lagi!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //nego sudah habis
                                btnNego.setEnabled(false);
                                hargaBaru.setEnabled(false);
                                Toast.makeText(nego_user.this, "Nego anda tidak berhasil...", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(status_nego_db.equalsIgnoreCase("terima")){
                            //nego berhasil
                            btnNego.setEnabled(false);
                            hargaBaru.setEnabled(false);
                            Toast.makeText(nego_user.this, "Silahkan lanjutkan pembelian pada page profil", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            //masukin detail disini
            namaBarang.setText(barang_nego_sek.getNama()+"");
            hargaBarang.setText("Rp. "+ barang_nego_sek.getHarga()+"");
            varianNego.setText(varian_nego);
            FirebaseStorage.getInstance().getReference().child("img_barang").child(barang_nego_sek.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(gambarBarang);
                }
            });
            //
            if(nego_baru){sisa_nego = 3;}
            sisaNego.setText("Sisa nego: "+sisa_nego+"");
        }
        btnNego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nilai_nego =hargaBaru.getText().toString();
                if(!nilai_nego.equalsIgnoreCase("")){
                    if(nego_baru){
                        //masukin nego baru
                        Nego temp_nego = new Nego();

                        //check harga memenuhi syarat nego:
                        double fraksi_harga = barang_nego_sek.getHarga()*0.2;
                        //cek harga negonya lebih kecil dari batasnya (20%==0.2)
                        if(Integer.parseInt(nilai_nego)<barang_nego_sek.getHarga()-fraksi_harga){
                            //kakean negooo
                            temp_nego.setStatus_nego("tolak");
                        }
                        else{
                            //ok boz
                            temp_nego.setStatus_nego("pending");
                        }
                        temp_nego.setNominal_nego(Integer.parseInt(nilai_nego));
                        temp_nego.setId_trans_nego("-");
                        temp_nego.setVarian(varian_nego);
                        temp_nego.setId_barang_nego(barang_nego_sek.getId());
                        Calendar waktu_sekarang  = new GregorianCalendar();
                        String waktu = waktu_sekarang.get(Calendar.DATE)+"-"+waktu_sekarang.get(Calendar.MONTH)+"-"+waktu_sekarang.get(Calendar.YEAR)+"-"+waktu_sekarang.get(Calendar.HOUR_OF_DAY)+"-"+waktu_sekarang.get(Calendar.MINUTE);
                        temp_nego.setWaktu_nego(waktu);
                        temp_nego.setSisa_nego(sisa_nego-1);
                        temp_nego.setId_seller(barang_nego_sek.getIdpenjual());
                        temp_nego.setId_user_nego(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        //masukin ke db
                        databaseReference_nego.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Key = databaseReference_nego.push().getKey();
                                temp_nego.setId_nego(Key);
                                databaseReference_nego.child(Key)
                                        .setValue(temp_nego)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(nego_user.this, "Nego sudah dikirim, silahkan tunggu balasan..", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    else{//nego yang lama
                        //init db nego
                        //check harga memenuhi syarat nego:
                        double fraksi_harga = barang_nego_sek.getHarga()*0.2;
                        //cek harga negonya lebih kecil dari batasnya (20%==0.2)
                        if(Integer.parseInt(nilai_nego)<(barang_nego_sek.getHarga()-fraksi_harga)){
                            //kakean negooo
                            databaseReference_nego.child(id_nego).child("status_nego").setValue("tolak");
                        }
                        else{
                            //ok boz
                            databaseReference_nego.child(id_nego).child("status_nego").setValue("pending");
                        }
                        databaseReference_nego.child(id_nego).child("sisa_nego").setValue(sisa_nego-1);
                        databaseReference_nego.child(id_nego).child("nominal_nego").setValue(Integer.parseInt(nilai_nego));
                    }
                    finish();
                }
                else{
                    Toast.makeText(nego_user.this, "Nilai nego harap diinput", Toast.LENGTH_SHORT).show();
                }
            }
        });
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