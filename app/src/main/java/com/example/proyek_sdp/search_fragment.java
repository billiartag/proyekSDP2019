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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search_fragment extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    RecyclerView rv_search_feed;
    SearchFeedAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View myview=inflater.inflate(R.layout.fragment_search,container,false);
        rv_search_feed = myview.findViewById(R.id.rv_search_feed);

        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("idpenjual").getValue().toString())){
                        barang data=new barang();
                        data.setId(ds.child("id").getValue().toString());
                        data.setDeskripsi(ds.child("deskripsi").getValue().toString());
                        data.setIdpenjual(ds.child("idpenjual").getValue().toString());
                        data.setJenis(ds.child("jenis").getValue().toString());
                        data.setNama(ds.child("nama").getValue().toString());
                        data.setLokasi(ds.child("lokasi").getValue().toString());
                        data.setVarian(ds.child("varian").getValue().toString());
                        data.setMaksimal(Integer.parseInt(ds.child("maksimal").getValue().toString()));
                        data.setWaktu_selesai(ds.child("waktu_selesai").getValue().toString());
                        data.setWaktu_upload(ds.child("waktu_upload").getValue().toString());
                        data.setHarga(Integer.parseInt(ds.child("harga").getValue().toString()));
                        kumpulanbarang.add(data);
                    }
                }
                rv_search_feed.setHasFixedSize(true);
                rv_search_feed.setLayoutManager(new GridLayoutManager(getContext(),2));
                adapter = new SearchFeedAdapter(getActivity(), kumpulanbarang);
                rv_search_feed.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myview;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_search, menu);
        MenuInflater menuInflater=getActivity().getMenuInflater();
        MenuItem searchitem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) searchitem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
