package com.example.proyek_sdp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class fragment_preorder_search extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    RecyclerView rv_preorder_search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_preorder_search_layout,container,false);
        rv_preorder_search=myview.findViewById(R.id.rv_preorder_search);
        load_data_barang();
        return  myview;
    }
    public void load_data_barang(){
        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kumpulanbarang.clear();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("idpenjual").getValue().toString()) && ds.child("status").getValue().toString().equals("1") && ds.child("jenis").getValue().equals("Pre Order")){
                        barang data=new barang();
                        data.setId(ds.child("id").getValue().toString());
                        data.setDeskripsi(ds.child("deskripsi").getValue().toString());
                        data.setIdpenjual(ds.child("idpenjual").getValue().toString());
                        data.setJenis(ds.child("jenis").getValue().toString());
                        data.setNama(ds.child("nama").getValue().toString());
                        data.setLokasi(ds.child("lokasi").getValue().toString());
                        data.setVarian(ds.child("varian").getValue().toString());
                        data.setMaksimal(Integer.parseInt(ds.child("maksimal").getValue().toString()));
                        data.setWaktu_mulai(ds.child("waktu_mulai").getValue().toString());
                        data.setWaktu_selesai(ds.child("waktu_selesai").getValue().toString());
                        data.setWaktu_upload(ds.child("waktu_upload").getValue().toString());
                        data.setHarga(Integer.parseInt(ds.child("harga").getValue().toString()));
                        data.setKategori(ds.child("kategori").getValue().toString());
                        data.setBerat(Integer.parseInt(ds.child("berat").getValue().toString()));
                        data.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                        kumpulanbarang.add(data);
                    }
                }
                rv_preorder_search.setHasFixedSize(true);
                rv_preorder_search.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                SearchFeedPreOrderAdapter adapter = new SearchFeedPreOrderAdapter(getActivity(), kumpulanbarang,((search_fragment) getParentFragment()).spinner_negara_search , ((search_fragment) getParentFragment()).checkBox_negara_search, ((search_fragment) getParentFragment()).edekategori_search, ((search_fragment) getParentFragment()).checkBox_kategori_search, ((search_fragment) getParentFragment()).searchView);
                rv_preorder_search.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
