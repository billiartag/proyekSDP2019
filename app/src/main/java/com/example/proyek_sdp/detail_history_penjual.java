package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class detail_history_penjual extends AppCompatActivity {


    LinearLayout ll1;
    LinearLayout ll2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_penjual);

        //set Linear Layout background color
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll1.setBackgroundColor(Color.GRAY);
        ll2.setBackgroundColor(Color.GRAY);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
    }



}
