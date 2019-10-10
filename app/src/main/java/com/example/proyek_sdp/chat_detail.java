package com.example.proyek_sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class chat_detail extends AppCompatActivity {

    ArrayList<String> a = new ArrayList<String>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        lv = findViewById(R.id.lvIsiChat);
        if(getIntent().getStringArrayListExtra("isichat")!=null){
            a = (ArrayList<String>) getIntent().getStringArrayListExtra("isichat");
        }
        final ArrayAdapter adap = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,a);
        lv.setAdapter(adap);

    }
}
