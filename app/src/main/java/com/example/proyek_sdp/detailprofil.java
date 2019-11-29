package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

public class detailprofil extends AppCompatActivity {
    ImageView profil_pict;
    TextView nama;
    TextView rating;
    Button chat;
    ImageButton report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailprofil);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
        chat=findViewById(R.id.msg);
        nama=findViewById(R.id.username);
        profil_pict=findViewById(R.id.profil_pict);
        rating=findViewById(R.id.rating);
        user x= (user) getIntent().getExtras().getSerializable("user");
        nama.setText("Nama : "+x.getNama());
        rating.setText("Rating : "+x.getRating());
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(x.getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(profil_pict);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profil_pict.setBackgroundResource(R.drawable.default_profil);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(),chat_detail.class);
                Bundle b = new Bundle();
                b.putSerializable("user", x);
                move.putExtras(b);
                startActivity(move);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_detailprofil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            finish();
        }
        else{
            Intent move=new Intent(getApplicationContext(),report.class);
            startActivity(move);
        }
        return true;
    }
}
