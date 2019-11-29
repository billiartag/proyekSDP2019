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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class chat_detail extends AppCompatActivity {
    ImageButton imgbtn_send;
    EditText edisichat;
    RecyclerView rv_chat_detail;
    String id_penerima;
    ArrayList<chat>list_chat=new ArrayList<chat>();
    DatabaseReference databaseReference_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        imgbtn_send=findViewById(R.id.imgbtn_send_chat);
        edisichat=findViewById(R.id.edisichat);
        rv_chat_detail=findViewById(R.id.rv_detail_chat);

        ActionBar ab=getSupportActionBar();
        if (getIntent().getSerializableExtra("user")!=null){
            user x=(user)getIntent().getExtras().getSerializable("user");
            ab.setTitle(x.getNama());
            id_penerima=x.getEmail();
        }
        if (getIntent().hasExtra("iduser")){
            String x=getIntent().getExtras().getString("iduser");
            ab.setTitle(x);
            id_penerima=x;
        }
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
}
