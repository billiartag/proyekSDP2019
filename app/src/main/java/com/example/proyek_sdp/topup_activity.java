package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class topup_activity extends AppCompatActivity {
    Spinner list_nominal;
    List<String> daftar_nominal= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_activity);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
        list_nominal=findViewById(R.id.list_nominal);
        for (int i=1;i<=10;i++){
            daftar_nominal.add(i*50+".000");
        }
        ArrayAdapter<String> nominal_adap=new ArrayAdapter<String>(getApplicationContext(),R.layout.custom_spinner,daftar_nominal);
        nominal_adap.setDropDownViewResource(R.layout.custom_spinner);
        list_nominal.setAdapter(nominal_adap);
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
