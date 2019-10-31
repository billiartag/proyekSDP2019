package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class search_fragment extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    ListView lv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View myview=inflater.inflate(R.layout.fragment_search,container,false);
        kumpulanbarang.add(new barang("Treadmill",25000,"bagus untuk kesehatan","05:45:21","Flash Sale",R.drawable.treadmill,"Cosmas",12));
        kumpulanbarang.add(new barang("electronic Treadmill",55000,"bagus untuk kesehatan dan otot kaki","07:45:21","Pre Order",R.drawable.electrictreadmill,"Alfon",14));
        kumpulanbarang.add(new barang("Bike",75000,"bagus untuk kesehatan dan mudah di pakai tanpa keluar rumah","08:45:21","Flash Sale",R.drawable.bike,"Edwin",6));
        lv = myview.findViewById(R.id.containerfeed);
        adapter adap = new adapter(getContext(),kumpulanbarang);
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                barang x=kumpulanbarang.get(i);
                Bundle b = new Bundle();
                b.putSerializable("barang", x);
                Intent intent = new Intent(getContext(), detail_feed.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        return myview;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class adapter extends ArrayAdapter<barang> {
        Context context;
        ArrayList<barang>barang;

        public adapter(Context c,ArrayList<barang>barang){
            super(c,R.layout.list_search_layout,barang);
            this.barang = barang;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_search_layout,parent,false);
            TextView detailbarang = row.findViewById(R.id.judul);
            TextView tipe = row.findViewById(R.id.tipe);
            ImageView img = row.findViewById(R.id.gambar_barang);
            TextView harga = row.findViewById(R.id.harga);
            detailbarang.setTextColor(Color.BLACK);
            detailbarang.setText(barang.get(position).toString());
            if (barang.get(position).getTipe()=="Flash Sale"){
                tipe.setBackgroundColor(Color.YELLOW);
                tipe.setTextColor(Color.BLACK);
                tipe.setText(barang.get(position).getTipe());
            }
            else if (barang.get(position).getTipe()=="Pre Order"){
                tipe.setBackgroundColor(Color.BLACK);
                tipe.setTextColor(Color.WHITE);
                tipe.setText(barang.get(position).getTipe());
            }
            harga.setText("Rp. "+barang.get(position).getHarga());
            harga.setTextColor(Color.parseColor("#651FFF"));
            img.setImageResource(barang.get(position).getGambar());

            return row;
        }

    }
}
