package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class DiNego extends AppCompatActivity {

    RecyclerView rv_dinego;
    ArrayList<barang> list_barang;
    ArrayList<Nego> list_nego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_di_nego);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("DiNego");
        rv_dinego = findViewById(R.id.rv_dinego);

        //ambil list nego dari db
        list_nego = new ArrayList<>();
        //ambil list barang dari db
        list_barang= new ArrayList<>();

        //item - hapus kalau sudah ambil dari server
        list_nego.add(new Nego("1","pending","5",250000, 2,"25/12/2019" ));
        list_nego.add(new Nego("2","pending","6",1250000, 1,"26/12/2019" ));
        list_nego.add(new Nego("3","tolak","7",450000, 0,"27/12/2019" ));
        list_nego.add(new Nego("4","terima","8",650000, 1,"28/12/2019" ));
        //
        //masukin yang pending aja
        ArrayList<Nego> list_nego_pending= new ArrayList<>();
        for (Nego r:list_nego) {
            if(r.getStatus_nego().equalsIgnoreCase("pending")){
                list_nego_pending.add(r);
            }
        }

        rv_dinego.setHasFixedSize(true);
        rv_dinego.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        DiNegoAdapter adapter = new DiNegoAdapter(this, list_nego_pending,list_barang);
        rv_dinego.setAdapter(adapter);
    }
    public void dinego(String id_nego, String kondisi_nego){
        if(kondisi_nego.equalsIgnoreCase("terima")){
            //kode ubah status nego jadi terima
            //buat beli barang disini
        }
        else if(kondisi_nego.equalsIgnoreCase("tolak")){
            //kode ubah status nego jadi terima
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
            finish();
        }
        return true;
    }
}
