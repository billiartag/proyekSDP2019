package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class myfeed_fragment extends Fragment {
    RecyclerView rv_myfeed;
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_myfeed,container,false);;
        setHasOptionsMenu(true);
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_profil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.reminder){
            //Toast.makeText(getContext(), "ini reminder", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(),reminder.class);
            startActivity(i);
        }
        else if (item.getItemId()==R.id.top_up){
            Intent move=new Intent(getActivity(),topup_activity.class);
            startActivity(move);
        }
        else if(item.getItemId()==R.id.hsell){
            Intent i = new Intent(getActivity(),history_penjual.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.hbuy){
            Intent i = new Intent(getActivity(),history_pembeli.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            Intent i = new Intent(getActivity(),Login.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
