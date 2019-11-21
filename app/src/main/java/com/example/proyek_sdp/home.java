package com.example.proyek_sdp;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class home extends AppCompatActivity {
    TextView mTextMessage;
    String judul="";
    String jenis="";
    String varian="";
    String max="";
    String lokasi="";
    String time_dari="";
    String time_ke="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navlistener);
        if(getIntent().hasExtra("lokasi")){
            judul=getIntent().getExtras().getString("judul");
            jenis=getIntent().getExtras().getString("jenis");
            varian=getIntent().getExtras().getString("varian");
            max=getIntent().getExtras().getString("max");
            lokasi=getIntent().getExtras().getString("lokasi");
            time_dari=getIntent().getExtras().getString("time_dari");
            time_ke=getIntent().getExtras().getString("time_ke");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new post_fragment()).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home_fragment()).commit();
        }
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    BottomNavigationView.OnNavigationItemSelectedListener navlistener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment=new home_fragment();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedfragment=new home_fragment();
                    break;
                case R.id.search:
                    selectedfragment=new search_fragment();
                    break;
                case R.id.navigation_personal:
                    selectedfragment=new personal_fragment();
                    break;
                case R.id.navigation_post:
                    selectedfragment=new post_fragment();
                    break;
                case R.id.myfeed:
                    selectedfragment=new myfeed_fragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedfragment).commit();
            return true;
        }
    };
}
