package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class chat_detail extends AppCompatActivity {

    ArrayList<String> a = new ArrayList<String>();
    ArrayList<String> un = new ArrayList<>();
    ListView lv;
    String penerima = "edwin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        lv = findViewById(R.id.lvIsiChat);
        if(getIntent().getStringArrayListExtra("isichat")!=null){
            a = (ArrayList<String>) getIntent().getStringArrayListExtra("isichat");
        }
        un.add("edwin");
        un.add("alfon");
        un.add("edwin");
        un.add("alfon");
        final ArrayAdapter adap = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,a);
        //lv.setAdapter(adap);

        adapter ad = new adapter(this,un,penerima,a);
        lv.setAdapter(ad);

    }

    class adapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> pengirim;
        ArrayList<String> isi;
        String username;

        public adapter(Context c,ArrayList<String>p,String un,ArrayList<String>isi){
            super(c,R.layout.list_chat_layout,R.id.tvBubbleChat,isi);
            this.pengirim = p;
            this.isi = isi;
            this.username = un;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_chat_layout,parent,false);
            TextView tvbubble = row.findViewById(R.id.tvBubbleChat);
            TextView tvuser = row.findViewById(R.id.tvUsername);
            if(username.equals(pengirim.get(position))){
                tvbubble.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvuser.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            tvbubble.setText(isi.get(position));
            tvbubble.setTextColor(Color.BLACK);

            tvuser.setText(pengirim.get(position));
            tvuser.setTextColor(Color.GRAY);

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
