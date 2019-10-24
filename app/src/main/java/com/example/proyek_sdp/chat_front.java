package com.example.proyek_sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
        ArrayList<String> kumpulanchatstring = new ArrayList<String>();
        ArrayList<Integer> kumpulangambar = new ArrayList<Integer>();
        kumpulanchatstring.add("alfon");
        kumpulangambar.add(R.drawable.bike);

        //final ArrayAdapter adap = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,kumpulanChat);
        adapter adap = new adapter(this,kumpulanchatstring,kumpulangambar);
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> isichat = new ArrayList<String >();
                for (int j = 0; j < kumpulanChat.get(i).getIsi().size(); j++) {
                    isichat.add(kumpulanChat.get(i).getIsi().get(j).isi);
                }
                Intent in = new Intent(getApplicationContext(),chat_detail.class);
                Bundle b = new Bundle();
                b.putStringArrayList("isichat",isichat);
                in.putExtras(b);
                startActivity(in);
            }
        });

    }

    class adapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> pengirim;
        String username;
        ArrayList<Integer>gambar;

        public adapter(Context c,ArrayList<String>p,ArrayList<Integer>g){
            super(c,R.layout.list_chat_front_layout,R.id.time,p);
            this.pengirim = p;
            this.gambar = g;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_chat_front_layout,parent,false);
            TextView tvbubble = row.findViewById(R.id.judul);
            ImageView img = row.findViewById(R.id.gambar_barang);
            tvbubble.setTextColor(Color.BLACK);
            tvbubble.setText(pengirim.get(position));
            img.setImageResource(gambar.get(position));

            return row;
        }

    }


}
