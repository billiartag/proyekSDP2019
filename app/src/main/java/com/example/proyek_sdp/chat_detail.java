package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.proyek_sdp.Notifications.Client;
import com.example.proyek_sdp.Notifications.Data;
import com.example.proyek_sdp.Notifications.MyResponse;
import com.example.proyek_sdp.Notifications.Sender;
import com.example.proyek_sdp.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chat_detail extends AppCompatActivity {
    ImageButton imgbtn_send;
    EditText edisichat;
    RecyclerView rv_chat_detail;
    String id_penerima;
    ArrayList<chat>list_chat=new ArrayList<chat>();
    DatabaseReference databaseReference_chat;
    user x,usersekarang;
    //untuk notif
    APIService apiService;
    boolean notify=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        imgbtn_send=findViewById(R.id.imgbtn_send_chat);
        edisichat=findViewById(R.id.edisichat);
        rv_chat_detail=findViewById(R.id.rv_detail_chat);
        x=new user();
        usersekarang=new user();

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        //start program
        ActionBar ab=getSupportActionBar();
        //dapetin data user yang di tekan
        if (getIntent().getSerializableExtra("user")!=null){
            x=(user)getIntent().getExtras().getSerializable("user");
            ab.setTitle(x.getNama());
            id_penerima=x.getEmail();
        }
        if (getIntent().hasExtra("iduser")){
            //dapetin user di db
            FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if(ds.child("email").getValue().toString().equals(getIntent().getExtras().getString(("iduser")))){
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
                            x.setRating(Integer.parseInt(ds.child("rating").getValue().toString()));
                            ab.setTitle(x.getNama());
                            id_penerima=x.getEmail();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //dapatkan data user sekarang
        getusernow();
        //load awal
        load_chat();
        imgbtn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id_penerima!=null && !edisichat.getText().toString().trim().equals("")){
                    chat chat_baru=new chat();
                    chat_baru.setIsi_chat(edisichat.getText().toString());
                    chat_baru.setPenerima_chat(id_penerima);
                    chat_baru.setPengirim_chat(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Date dt = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String time1 = sdf.format(dt);
                    chat_baru.setWaktu_kirim_chat(time1);
                    //beri notifikasi
                    notify=true;
                    final String msg=edisichat.getText().toString();
                    if(x!=null){
                        if(notify){
                            sendNotification(x.getFirebase_user_id(),usersekarang.getNama(),msg);
                        }
                        notify=false;
                    }
                    //insert ke database
                    databaseReference_chat= FirebaseDatabase.getInstance().getReference().child("ChatDatabase");
                    String Key = databaseReference_chat.push().getKey();
                    chat_baru.setId(Key);
                    databaseReference_chat.child(Key).setValue(chat_baru).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            load_chat();
                            edisichat.setText("");
                        }
                    });
                }
            }
        });
    }
    public void sendNotification(String receiver,final String username,final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(usersekarang.getFirebase_user_id(), R.mipmap.logo_icon_app_round,username+": "+message,"New message",x.getFirebase_user_id());

                    Sender sender=new Sender(data,token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200){
                                if(response.body().success!=1){
                                    Toast.makeText(chat_detail.this, "Failed Send Notification!", Toast.LENGTH_SHORT).show();
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
    public void load_chat(){
        list_chat.clear();
        FirebaseDatabase.getInstance().getReference().child("ChatDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if((ds.child("pengirim_chat").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && ds.child("penerima_chat").getValue().toString().equals(id_penerima)) || ds.child("pengirim_chat").getValue().toString().equals(id_penerima) && ds.child("penerima_chat").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        chat x=new chat();
                        x.setId(ds.child("id").getValue().toString());
                        x.setIsi_chat(ds.child("isi_chat").getValue().toString());
                        x.setPenerima_chat(ds.child("penerima_chat").getValue().toString());
                        x.setPengirim_chat(ds.child("pengirim_chat").getValue().toString());
                        x.setWaktu_kirim_chat(ds.child("waktu_kirim_chat").getValue().toString());
                        list_chat.add(x);
                    }
                }
                ChatDetailAdapter adapter=new ChatDetailAdapter(getApplicationContext(),list_chat);
                rv_chat_detail.setHasFixedSize(true);
                rv_chat_detail.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                rv_chat_detail.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuBack){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //update token
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(usersekarang.getFirebase_user_id()).setValue(token1);
    }
    public void getusernow(){
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
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
                        usersekarang.setRating(Integer.parseInt(ds.child("rating").getValue().toString()));
                        updateToken(FirebaseInstanceId.getInstance().getToken());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
