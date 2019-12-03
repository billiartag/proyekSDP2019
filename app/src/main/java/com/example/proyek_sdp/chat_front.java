package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_front extends AppCompatActivity {

    ArrayList<chat> list_chat = new ArrayList<chat>();
    ArrayList<String> list_all_user_chatted = new ArrayList<String>();
    RecyclerView rv_chat_front;
    ChatFrontAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_front);
        rv_chat_front = findViewById(R.id.rv_chat_front);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Chat");
        FirebaseDatabase.getInstance().getReference().child("ChatDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("pengirim_chat").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) || ds.child("penerima_chat").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        chat x=new chat();
                        x.setId(ds.child("id").getValue().toString());
                        x.setIsi_chat(ds.child("isi_chat").getValue().toString());
                        x.setPenerima_chat(ds.child("penerima_chat").getValue().toString());
                        x.setPengirim_chat(ds.child("pengirim_chat").getValue().toString());
                        x.setWaktu_kirim_chat(ds.child("waktu_kirim_chat").getValue().toString());
                        list_chat.add(x);
                    }
                }
                for (chat x:list_chat) {
                    if (x.getPenerima_chat().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        boolean berhasil=true;
                        for (String y:list_all_user_chatted) {
                            if(y.equals(x.getPengirim_chat())){
                                berhasil=false;
                            }
                        }
                        if(berhasil){
                            list_all_user_chatted.add(x.getPengirim_chat());
                        }
                    }
                    else {
                        boolean berhasil=true;
                        for (String y:list_all_user_chatted) {
                            if(y.equals(x.getPenerima_chat())){
                                berhasil=false;
                            }
                        }
                        if(berhasil){
                            list_all_user_chatted.add(x.getPenerima_chat());
                        }
                    }
                }
                rv_chat_front.setHasFixedSize(true);
                rv_chat_front.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                adapter=new ChatFrontAdapter(getApplicationContext(),list_all_user_chatted);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent move=new Intent(getApplicationContext(),chat_detail.class);
                        Bundle b = new Bundle();
                        b.putSerializable("iduser", list_all_user_chatted.get(position));
                        move.putExtras(b);
                        startActivity(move);
                    }
                });
                rv_chat_front.setAdapter(adapter);
                registerForContextMenu(rv_chat_front);
                if (list_all_user_chatted.size()==0){
                    Toast.makeText(chat_front.this, "Chat Anda Kosong!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==121){
            adapter.delete_chat(item.getGroupId());
        }
        return super.onContextItemSelected(item);
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
