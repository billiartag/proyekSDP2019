package com.example.proyek_sdp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myfeed_fragment extends Fragment {
    RecyclerView rv_myfeed;
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_myfeed,container,false);;
        rv_myfeed=myview.findViewById(R.id.rv_myfeed);
        kumpulanbarang.add(new barang("Treadmill",25000,"bagus untuk kesehatan","05:45:21","Flash Sale",12,R.drawable.treadmill,"cosmas"));
        kumpulanbarang.add(new barang("electronic Treadmill",55000,"bagus untuk kesehatan dan otot kaki","07:45:21","Pre Order",14,R.drawable.electrictreadmill,"Alfon"));
        kumpulanbarang.add(new barang("Bike",75000,"bagus untuk kesehatan dan mudah di pakai tanpa keluar rumah","08:45:21","Flash Sale",6,R.drawable.bike,"Edwin"));
        MyFeedAdapter adapter=new MyFeedAdapter(getContext(),kumpulanbarang);
        rv_myfeed.setHasFixedSize(true);
        rv_myfeed.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_myfeed.setAdapter(adapter);
        return myview;
    }
}
