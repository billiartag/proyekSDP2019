package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class AkuNego extends AppCompatActivity {

    RecyclerView rv_akunego;
    ArrayList<barang> list_barang;
    ArrayList<Nego> list_nego;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aku_nego);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("AkuNego");
        rv_akunego = findViewById(R.id.rv_akunego);
        //ambil list nego dari db
        list_nego = new ArrayList<>();
        //ambil list barang dari db
        list_barang= new ArrayList<>();

        //item - hapus kalau sudah ambil dari server
        list_nego.add(new Nego("1","pending","5",250000, 2,"25/12/2019" ));
        list_nego.add(new Nego("2","tolak","6",1250000, 1,"26/12/2019" ));
        list_nego.add(new Nego("3","tolak","7",450000, 0,"27/12/2019" ));
        list_nego.add(new Nego("4","terima","8",650000, 1,"28/12/2019" ));
        //
        rv_akunego.setHasFixedSize(true);
        rv_akunego.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        AkuNegoAdapter adapter = new AkuNegoAdapter(this, list_nego,list_barang);
        rv_akunego.setAdapter(adapter);


    }
    public void akuNego(String id_nego,int sisa_nego){
        //buat intent nego
        finish();
        Intent i = new Intent(AkuNego.this,nego_user.class);
        Bundle b = new Bundle();
        b.putString("id_nego", id_nego);
        b.putInt("sisa_nego", sisa_nego);
        i.putExtras(b);
        startActivity(i);
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
