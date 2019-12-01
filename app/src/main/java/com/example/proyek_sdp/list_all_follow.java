package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_all_follow extends AppCompatActivity {
    RecyclerView rv_follow_list;
    EditText edfiltersearch_list_follow;
    TextView tipe_follow_list;
    ArrayList<user> list_user=new ArrayList<user>();
    ImageView img_search_list_follow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_follow);
        rv_follow_list=findViewById(R.id.rv_list_follow);
        edfiltersearch_list_follow=findViewById(R.id.edfiltersearch_list_follow);
        img_search_list_follow=findViewById(R.id.img_search_list_follow);
        tipe_follow_list=findViewById(R.id.tipe_follow_list);
        //start program
        if(getIntent().getExtras().getString("tipe_follow").equals("0")){
            tipe_follow_list.setText("Followers :");
        }
        else if(getIntent().getExtras().getString("tipe_follow").equals("1")){
            tipe_follow_list.setText("Following :");
        }
        loaddatauser(getIntent().getExtras().getString("tipe_follow"));
        edfiltersearch_list_follow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edfiltersearch_list_follow.getText().toString().equals("")){
                    loaddatauser(getIntent().getExtras().getString("tipe_follow"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        img_search_list_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddatauser(getIntent().getExtras().getString("tipe_follow"));
            }
        });
    }
    public void loaddatauser(String tipe){
        //following
        rv_follow_list.setHasFixedSize(true);
        rv_follow_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        if(tipe.equals("0")){
            FirebaseDatabase.getInstance().getReference().child("FollowDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count=dataSnapshot.getChildrenCount();
                    list_user.clear();
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if(ds.child("following").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    long count=dataSnapshot2.getChildrenCount();
                                    for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                        if(ds2.child("email").getValue().equals(ds.child("id_user").getValue().toString())){
                                            user follow_user=new user();
                                            follow_user.setId(ds2.child("id").getValue().toString());
                                            follow_user.setNama(ds2.child("nama").getValue().toString());
                                            follow_user.setEmail(ds2.child("email").getValue().toString());
                                            follow_user.setPhone(ds2.child("phone").getValue().toString());
                                            follow_user.setBirthdate(ds2.child("birthdate").getValue().toString());
                                            follow_user.setPassword(ds2.child("password").getValue().toString());
                                            follow_user.setRating(Float.parseFloat(ds2.child("rating").getValue().toString()));
                                            follow_user.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                                            follow_user.setSaldo(Integer.parseInt(ds2.child("saldo").getValue().toString()));
                                            follow_user.setVerifikasi_ktp(Integer.parseInt(ds2.child("verifikasi_ktp").getValue().toString()));
                                            follow_user.setProfil_picture(Integer.parseInt(ds2.child("profil_picture").getValue().toString()));
                                            list_user.add(follow_user);
                                        }
                                    }
                                    ListFollowAdapter adaptertopuser = new ListFollowAdapter(getApplicationContext(), list_user,0,edfiltersearch_list_follow);
                                    rv_follow_list.setAdapter(adaptertopuser);
                                    if(list_user.isEmpty()){
                                        Toast.makeText(list_all_follow.this, "Anda Tidak Mem-iliki Follower!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
        //followers
        else if(tipe.equals("1")){
            FirebaseDatabase.getInstance().getReference().child("FollowDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count=dataSnapshot.getChildrenCount();
                    list_user.clear();
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if(ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    long count=dataSnapshot2.getChildrenCount();
                                    for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                        if(ds2.child("email").getValue().equals(ds.child("following").getValue().toString())){
                                            user follow_user=new user();
                                            follow_user.setId(ds2.child("id").getValue().toString());
                                            follow_user.setNama(ds2.child("nama").getValue().toString());
                                            follow_user.setEmail(ds2.child("email").getValue().toString());
                                            follow_user.setPhone(ds2.child("phone").getValue().toString());
                                            follow_user.setBirthdate(ds2.child("birthdate").getValue().toString());
                                            follow_user.setPassword(ds2.child("password").getValue().toString());
                                            follow_user.setRating(Float.parseFloat(ds2.child("rating").getValue().toString()));
                                            follow_user.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                                            follow_user.setSaldo(Integer.parseInt(ds2.child("saldo").getValue().toString()));
                                            follow_user.setVerifikasi_ktp(Integer.parseInt(ds2.child("verifikasi_ktp").getValue().toString()));
                                            follow_user.setProfil_picture(Integer.parseInt(ds2.child("profil_picture").getValue().toString()));
                                            list_user.add(follow_user);
                                        }
                                    }
                                    ListFollowAdapter adaptertopuser = new ListFollowAdapter(getApplicationContext(), list_user,1,edfiltersearch_list_follow);
                                    rv_follow_list.setAdapter(adaptertopuser);
                                    if(list_user.isEmpty()){
                                        Toast.makeText(list_all_follow.this, "Anda Tidak Mem-Follow Siapa Pun!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            Intent move=new Intent(getApplicationContext(), home.class);
            move.putExtra("topup","");
            startActivity(move);
            finish();
        }
        return true;
    }
}
