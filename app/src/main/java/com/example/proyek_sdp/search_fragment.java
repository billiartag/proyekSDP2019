package com.example.proyek_sdp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class search_fragment extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    RecyclerView rv_search_feed;
    CheckBox checkBox_negara_search;
    CheckBox checkBox_kategori_search;
    Spinner spinner_negara_search;
    EditText edekategori_search;
    SearchFeedAdapter adapter;
    String[]list_all_country;
    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((home) getActivity()).setActionBarTitle("TitipAku");
        View myview=inflater.inflate(R.layout.fragment_search,container,false);
        rv_search_feed = myview.findViewById(R.id.rv_search_feed);
        checkBox_negara_search = myview.findViewById(R.id.checkBox_negara_search);
        checkBox_kategori_search = myview.findViewById(R.id.checkBox_kategori_search);
        spinner_negara_search = myview.findViewById(R.id.spinner_negara_search);
        edekategori_search = myview.findViewById(R.id.kategori_search_isi);

        String[] locales = Locale.getISOCountries();
        list_all_country=new String[locales.length];
        int ctr=0;
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            list_all_country[ctr]=obj.getDisplayCountry();
            ctr++;
        }
        ArrayAdapter<String> NegaraAdapter = new ArrayAdapter<String>(getContext(),   android.R.layout.simple_spinner_item, list_all_country);
        NegaraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_negara_search.setAdapter(NegaraAdapter);
        spinner_negara_search.setEnabled(false);
        edekategori_search.setEnabled(false);
        checkBox_negara_search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_negara_search.isChecked()){
                    spinner_negara_search.setEnabled(true);
                    load_data_barang();
                }
                else {
                    spinner_negara_search.setEnabled(false);
                    load_data_barang();
                }
            }
        });
        spinner_negara_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                load_data_barang();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        checkBox_kategori_search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_kategori_search.isChecked()){
                    edekategori_search.setEnabled(true);
                    load_data_barang();
                }
                else {
                    edekategori_search.setEnabled(false);
                    load_data_barang();
                }
            }
        });
        edekategori_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                load_data_barang();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        load_data_barang();

        return myview;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_search, menu);
        MenuInflater menuInflater=getActivity().getMenuInflater();
        MenuItem searchitem=menu.findItem(R.id.search);
        //search view
        searchView=(SearchView) searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (adapter!=null){
                    adapter.getFilter().filter(s);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    public void load_data_barang(){
        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kumpulanbarang.clear();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("idpenjual").getValue().toString()) && ds.child("status").getValue().toString().equals("1")){
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
                rv_search_feed.setHasFixedSize(true);
                rv_search_feed.setLayoutManager(new GridLayoutManager(getContext(),2));
                adapter = new SearchFeedAdapter(getActivity(), kumpulanbarang,spinner_negara_search,checkBox_negara_search,edekategori_search,checkBox_kategori_search,searchView);
                rv_search_feed.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
