package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class myfeed_fragment extends Fragment {
    ListView list_feed;
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_myfeed,container,false);;
        list_feed=myview.findViewById(R.id.lv_feed);
        kumpulanbarang.add(new barang("Treadmill",25000,"bagus untuk kesehatan","05:45:21","Flash Sale",R.drawable.treadmill,"Cosmas",12));
        kumpulanbarang.add(new barang("electronic Treadmill",55000,"bagus untuk kesehatan dan otot kaki","07:45:21","Pre Order",R.drawable.electrictreadmill,"Alfon",14));
        kumpulanbarang.add(new barang("Bike",75000,"bagus untuk kesehatan dan mudah di pakai tanpa keluar rumah","08:45:21","Flash Sale",R.drawable.bike,"Edwin",6));
        adapter adap=new adapter(getContext(),kumpulanbarang);
        list_feed.setAdapter(adap);
        return myview;
    }
    class adapter extends ArrayAdapter<barang> {
        Context context;
        ArrayList<barang>barang;

        public adapter(Context c,ArrayList<barang>barang){
            super(c,R.layout.list_barang_layout,barang);
            this.barang = barang;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_feed_layout,parent,false);
            TextView desc=row.findViewById(R.id.desc);
            desc.setText(desc.getText().toString()+"\n"+kumpulanbarang.get(position).getDeskripsi());
            return row;
        }

    }
}
