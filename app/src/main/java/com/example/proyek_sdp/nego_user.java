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
import com.example.proyek_sdp.Notifications.Client;
import com.example.proyek_sdp.Notifications.Data;
import com.example.proyek_sdp.Notifications.MyResponse;
import com.example.proyek_sdp.Notifications.Sender;
import com.example.proyek_sdp.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    //untuk notif
    APIService apiService;
    boolean notify=false;
    user x,usersekarang;

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
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        nego_baru=true;
        sisa_nego=-1;
        id_nego="";
        status_nego_db="-";

        databaseReference_nego= FirebaseDatabase.getInstance().getReference().child("NegoDatabase");
        x=new user();
        usersekarang=new user();
        //dapatkan data user sekarang
        getusernow();
        //dapatkan data user penjual
        getusertujuan();
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
                            id_nego = row.child("id_nego").getValue().toString();
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
                        //send notifikasi pertama kali mulai nego
                        //beri notifikasi
                        notify=true;
                        final String msg="Melakukan Nego Dengan Anda!";
                        if(x!=null){
                            if(notify){
                                sendNotification(x.getFirebase_user_id(),usersekarang.getNama(),msg);
                            }
                            notify=false;
                        }
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
                        //beri notifikasi
                        notify=true;
                        final String msg="Melakukan Nego Dengan Anda!";
                        if(x!=null){
                            if(notify){
                                sendNotification(x.getFirebase_user_id(),usersekarang.getNama(),msg);
                            }
                            notify=false;
                        }
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
    //send notifikasi
    public void sendNotification(String receiver,final String username,final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(usersekarang.getFirebase_user_id(), R.mipmap.logo_icon_app_round,username+"- "+message,"TitipAku",x.getFirebase_user_id());

                    Sender sender=new Sender(data,token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200){
                                if(response.body().success!=1){
                                    Toast.makeText(nego_user.this, "Failed Send Notification!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //update token
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(usersekarang.getFirebase_user_id()).setValue(token1);
    }
    public void getusertujuan(){
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(barang_nego_sek.getIdpenjual())){
                        x.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                        x.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                        x.setAlamat(ds.child("alamat").getValue().toString());
                        x.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString()));
                        x.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                        x.setPassword(ds.child("password").getValue().toString());
                        x.setId(ds.child("id").getValue().toString());
                        x.setBirthdate(ds.child("birthdate").getValue().toString());
                        x.setEmail(ds.child("email").getValue().toString());
                        x.setPhone(ds.child("phone").getValue().toString());
                        x.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                        x.setNama(ds.child("nama").getValue().toString());
                        x.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getusernow(){
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        if(ds!=null){
                            usersekarang.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                            usersekarang.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                            usersekarang.setAlamat(ds.child("alamat").getValue().toString());
                            usersekarang.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString()));
                            usersekarang.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                            usersekarang.setPassword(ds.child("password").getValue().toString());
                            usersekarang.setId(ds.child("id").getValue().toString());
                            usersekarang.setBirthdate(ds.child("birthdate").getValue().toString());
                            usersekarang.setEmail(ds.child("email").getValue().toString());
                            usersekarang.setPhone(ds.child("phone").getValue().toString());
                            usersekarang.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                            usersekarang.setNama(ds.child("nama").getValue().toString());
                            usersekarang.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                            updateToken(FirebaseInstanceId.getInstance().getToken());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}