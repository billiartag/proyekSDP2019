package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_front extends AppCompatActivity {

    ArrayList<chat> kumpulanChat = new ArrayList<chat>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_front);
        kumpulanChat.add(new chat("edwin","alfon"));
        ArrayList<isi_chat> list_chat = new ArrayList<isi_chat>();
        list_chat.add(new isi_chat("edwin","hai"));
        list_chat.add(new isi_chat("alfon","hmmmm"));
        list_chat.add(new isi_chat("edwin","Hiya :D"));
        list_chat.add(new isi_chat("alfon",">:O"));
        kumpulanChat.get(0).setIsi(list_chat);
        lv = findViewById(R.id.lvchat);
        ArrayList<String> kumpulanchatstring = new ArrayList<String>();
        ArrayList<Integer> kumpulangambar = new ArrayList<Integer>();
        kumpulanchatstring.add("alfon");
        kumpulangambar.add(R.drawable.bike);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");

        //final ArrayAdapter adap = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,kumpulanChat);
        adapter adap = new adapter(this,kumpulanchatstring,kumpulangambar,kumpulanChat);
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
        ArrayList<chat>list_chat;

        public adapter(Context c,ArrayList<String>list_pengirim,ArrayList<Integer>list_gambar,ArrayList<chat>list_chat){
            super(c,R.layout.list_chat_front_layout,R.id.time,list_pengirim);
            this.pengirim = list_pengirim;
            this.gambar = list_gambar;
            this.list_chat = list_chat;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_chat_front_layout,parent,false);
            TextView username = row.findViewById(R.id.username_chat);
            TextView display_chat = row.findViewById(R.id.isi_chat_display);
            CircleImageView img = row.findViewById(R.id.profile_image_chat);
            username.setTextColor(Color.BLACK);
            username.setText(pengirim.get(position));
            display_chat.setTextColor(Color.BLACK);
            display_chat.setText(list_chat.get(0).getIsi().get(list_chat.get(0).getIsi().size()-1).isi);
            img.setImageResource(gambar.get(position));

            return row;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuBack){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
