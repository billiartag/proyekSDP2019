package com.example.proyek_sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class chat_front extends AppCompatActivity {

    ArrayList<chat> kumpulanChat = new ArrayList<chat>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_front);
        kumpulanChat.add(new chat("edwin","alfon"));
        ArrayList<isi_chat> a = new ArrayList<isi_chat>();
        a.add(new isi_chat("edwin","hai"));
        a.add(new isi_chat("alfon","hmmmm"));
        a.add(new isi_chat("edwin","Hiya :D"));
        a.add(new isi_chat("alfon",">:O"));
        kumpulanChat.get(0).setIsi(a);
        lv = findViewById(R.id.lvchat);

        final ArrayAdapter adap = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,kumpulanChat);
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> isichat = new ArrayList<String >();
                for (int j = 0; j < kumpulanChat.get(i).getIsi().size(); j++) {
                    isichat.add(kumpulanChat.get(i).getIsi().get(j).toString());
                }
                Intent in = new Intent(getApplicationContext(),chat_detail.class);
                Bundle b = new Bundle();
                b.putStringArrayList("isichat",isichat);
                in.putExtras(b);
                startActivity(in);
            }
        });

    }


}
