package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        report=findViewById(R.id.btn_report);
        nama=findViewById(R.id.judul);
        profil_pict=findViewById(R.id.profil_pict);
        rating=findViewById(R.id.rating);
        user x= (user) getIntent().getExtras().getSerializable("user");
        nama.setText("Nama : "+x.getNama());
        profil_pict.setBackgroundResource(x.getProfil_picture());
        rating.setText("Rating : "+x.getRating());
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(),chat_front.class);
                startActivity(move);
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(),report.class);
                startActivity(move);
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
