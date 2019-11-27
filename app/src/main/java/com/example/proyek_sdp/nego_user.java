package com.example.proyek_sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class nego_user extends AppCompatActivity {

    TextView namaBarang;
    TextView hargaBarang;
    TextView sisaNego;
    EditText hargaBaru;
    ImageView gambarBarang;
    Button btnNego;

    ArrayList<barang> list_barang;
    Boolean nego_baru;
    String id_nego;
    int sisa_nego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nego_user);
        namaBarang=findViewById(R.id.textViewNegoNamaBarang);
        hargaBarang=findViewById(R.id.textViewNegoHargaBarang);
        sisaNego=findViewById(R.id.textViewNegoBatas);
        hargaBaru=findViewById(R.id.editTextNegoHarga);
        gambarBarang=findViewById(R.id.imageViewBarangNego);
        btnNego=findViewById(R.id.buttonNegoSubmit);

        nego_baru=true;
        sisa_nego=-1;
        id_nego="";
        Intent i = getIntent();
        if(i.getExtras()!=null){
            nego_baru=false;
            Bundle b = i.getExtras();
            id_nego = b.getString("id_nego");
            //ambil detail barang disini

            //
            sisa_nego=b.getInt("sisa_nego");
            sisaNego.setText("Sisa nego: "+sisa_nego+"");
        }
        btnNego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nilai_nego =hargaBaru.getText().toString();
                if(!nilai_nego.equalsIgnoreCase("")){
                    if(nego_baru){
                        //masukin nego baru
                    }
                    else{
                        //update pake id_nego yang dikirim
                    }
                    Toast.makeText(nego_user.this, "Nego sudah dikirim, silahkan tunggu balasan..", Toast.LENGTH_SHORT).show();
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
