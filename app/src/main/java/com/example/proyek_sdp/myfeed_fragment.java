package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class myfeed_fragment extends Fragment {
    RecyclerView rv_myfeed;
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_myfeed,container,false);;
        setHasOptionsMenu(true);
        rv_myfeed=myview.findViewById(R.id.rv_myfeed);
        if(((home) getActivity())!=null && getActivity()!=null){
            ((home) getActivity()).setActionBarTitle("Feed");
            databaseReference= FirebaseDatabase.getInstance().getReference().child("BarangDatabase");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count=dataSnapshot.getChildrenCount();
                    boolean berhasil_register=true;
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("idpenjual").getValue().toString()) && ds.child("status").getValue().toString().equals("1")){
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
                            data.setStatus(Integer.parseInt(ds.child("berat").getValue().toString()));
                            kumpulanbarang.add(data);
                        }
                    }
                    MyFeedAdapter adapter=new MyFeedAdapter(getContext(),kumpulanbarang);
                    rv_myfeed.setHasFixedSize(true);
                    rv_myfeed.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                    rv_myfeed.setAdapter(adapter);
                    if(kumpulanbarang.size()==0){
                        if(getActivity()!=null){
                            Toast.makeText(getActivity(), "Anda Tidak Memiliki Post!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return myview;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_profil, menu);
        super.onCreateOptionsMenu(menu, inflater);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                        menu.getItem(0).setTitle("Saldo : "+Integer.parseInt(ds.child("saldo").getValue().toString()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
