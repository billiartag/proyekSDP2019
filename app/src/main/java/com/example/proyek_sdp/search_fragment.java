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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class search_fragment extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    CheckBox checkBox_negara_search;
    CheckBox checkBox_kategori_search;
    Spinner spinner_negara_search;
    EditText edekategori_search;
    SearchFeedAdapter adapter;
    String[]list_all_country;
    SearchView searchView;
    Fragment fragment;
    int menu_sekarang=-1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((home) getActivity()).setActionBarTitle("Search");
        View myview=inflater.inflate(R.layout.fragment_search,container,false);
        checkBox_negara_search = myview.findViewById(R.id.checkBox_negara_search);
        checkBox_kategori_search = myview.findViewById(R.id.checkBox_kategori_search);
        spinner_negara_search = myview.findViewById(R.id.spinner_negara_search);
        edekategori_search = myview.findViewById(R.id.kategori_search_isi);
        BottomNavigationView tabbar = myview.findViewById(R.id.tabbar_search);
        menu_sekarang=R.id.tab_flashsale_search;
        //start program
        //dapatkan data semua negara
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
        //tab search
        tabbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.tab_flashsale_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                    menu_sekarang=R.id.tab_flashsale_search;
                }
                else if(menuItem.getItemId()==R.id.tab_preorder_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
                    menu_sekarang=R.id.tab_preorder_search;
                }
                return true;
            }
        });
        //tekan checkbox negara
        checkBox_negara_search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_negara_search.isChecked()){
                    spinner_negara_search.setEnabled(true);
                    if(menu_sekarang==R.id.tab_flashsale_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                    }
                    else if(menu_sekarang==R.id.tab_preorder_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
                    }
                }
                else {
                    spinner_negara_search.setEnabled(false);
                    if(menu_sekarang==R.id.tab_flashsale_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                    }
                    else if(menu_sekarang==R.id.tab_preorder_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
                    }
                }
            }
        });
        //spinner negara
        spinner_negara_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(menu_sekarang==R.id.tab_flashsale_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                }
                else if(menu_sekarang==R.id.tab_preorder_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //checkbox kategori
        checkBox_kategori_search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_kategori_search.isChecked()){
                    edekategori_search.setEnabled(true);
                    if(menu_sekarang==R.id.tab_flashsale_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                    }
                    else if(menu_sekarang==R.id.tab_preorder_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
                    }
                }
                else {
                    edekategori_search.setEnabled(false);
                    if(menu_sekarang==R.id.tab_flashsale_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                    }
                    else if(menu_sekarang==R.id.tab_preorder_search){
                        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
                    }
                }
            }
        });
        //edit text kategori
        edekategori_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(menu_sekarang==R.id.tab_flashsale_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                }
                else if(menu_sekarang==R.id.tab_preorder_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if(menu_sekarang==R.id.tab_flashsale_search){
            getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
        }
        else if(menu_sekarang==R.id.tab_preorder_search){
            getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
        }

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
                if(menu_sekarang==R.id.tab_flashsale_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_flashsale_search()).commit();
                }
                else if(menu_sekarang==R.id.tab_preorder_search){
                    getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_container_search,new fragment_preorder_search()).commit();
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

}
