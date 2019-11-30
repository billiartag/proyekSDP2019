package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SearchFeedAdapter extends RecyclerView.Adapter<SearchFeedAdapter.SearchFeedViewHolder> implements Filterable {
    private Context context;
    private ArrayList<barang>list_barang;
    private ArrayList<barang>list_barang_example=new ArrayList<barang>();
    private Spinner spinner_negara;
    private CheckBox checkBox_negara;
    private EditText edkategori_search;
    private CheckBox checkBox_kategori;
    private SearchView searchView;

    public SearchFeedAdapter(Context context, ArrayList<barang> list_barang, Spinner spinner_negara, CheckBox checkBox_negara, EditText edkategori_search, CheckBox checkBox_kategori, SearchView searchView) {
        this.context = context;
        this.list_barang = list_barang;
        this.spinner_negara=spinner_negara;
        this.checkBox_negara=checkBox_negara;
        this.edkategori_search=edkategori_search;
        this.checkBox_kategori=checkBox_kategori;
        this.searchView=searchView;
        if(checkBox_negara.isChecked() && checkBox_kategori.isChecked() && !searchView.getQuery().equals("")){
            for (barang x:list_barang) {
                if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase()) && x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase()) && x.getNama().toLowerCase().contains(searchView.getQuery().toString().toLowerCase()) ){
                    list_barang_example.add(x);
                }
            }
        }
        else if(checkBox_negara.isChecked() && !searchView.getQuery().equals("")){
            for (barang x:list_barang) {
                if(x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase()) && x.getNama().toLowerCase().contains(searchView.getQuery().toString().toLowerCase()) ){
                    list_barang_example.add(x);
                }
            }
        }
        else if (checkBox_kategori.isChecked() && !searchView.getQuery().equals("")){
            for (barang x:list_barang) {
                if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase()) && x.getNama().toLowerCase().contains(searchView.getQuery().toString().toLowerCase()) ){
                    list_barang_example.add(x);
                }
            }
        }
        else if(checkBox_negara.isChecked() && checkBox_kategori.isChecked()){
            for (barang x:list_barang) {
                if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase()) && x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase())){
                    list_barang_example.add(x);
                }
            }
        }
        else if(checkBox_negara.isChecked()){
            list_barang_example.clear();
            for (barang x:list_barang) {
                if(x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase())){
                    list_barang_example.add(x);
                }
            }
        }
        else if(checkBox_kategori.isChecked()){
            for (barang x:list_barang) {
                if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase())){
                    list_barang_example.add(x);
                }
            }

        }
        else if(!searchView.getQuery().equals("")){
            for (barang x:list_barang) {
                if(x.getNama().toLowerCase().contains(searchView.getQuery().toString().toLowerCase())){
                    list_barang_example.add(x);
                }
            }

        }
        else {
            list_barang_example.addAll(list_barang);
        }
    }

    @NonNull
    @Override
    public SearchFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_layout,parent,false);
        SearchFeedViewHolder holder=new SearchFeedViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFeedViewHolder holder, int position) {
        holder.detailbarang.setTextColor(Color.BLACK);
        holder.detailbarang.setText(list_barang_example.get(position).toString());
        if (list_barang_example.get(position).getJenis().equals("Flash Sale")){
            holder.tipe.setBackgroundColor(Color.parseColor("#FB8C00"));
            holder.tipe.setTextColor(Color.BLACK);
            holder.tipe.setText(list_barang_example.get(position).getJenis());
        }
        else if (list_barang_example.get(position).getJenis().equals("Pre Order")){
            holder.tipe.setBackgroundColor(Color.BLACK);
            holder.tipe.setTextColor(Color.WHITE);
            holder.tipe.setText(list_barang_example.get(position).getJenis());
        }
        holder.harga.setText("Rp. "+list_barang_example.get(position).getHarga());
        holder.harga.setTextColor(Color.parseColor("#651FFF"));

        FirebaseStorage.getInstance().getReference().child("img_barang").child(list_barang_example.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.img);
            }
        });

        holder.container_search_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barang x=list_barang_example.get(position);
                Bundle b = new Bundle();
                b.putSerializable("barang", x);
                Intent intent = new Intent(context, detail_feed.class);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_barang_example.size();
    }

    @Override
    public Filter getFilter() {
        return cobafilter;
    }

    private Filter cobafilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<barang>filtered_barang=new ArrayList<barang>();
            if(charSequence==null || charSequence.length()==0){
                if(checkBox_negara.isChecked() && checkBox_kategori.isChecked()){
                    for (barang x:list_barang) {
                        if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase()) && x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase())){
                            filtered_barang.add(x);
                        }
                    }
                }
                else if(checkBox_negara.isChecked()){
                    for (barang x:list_barang) {
                        if(x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase())){
                            filtered_barang.add(x);
                        }
                    }
                }
                else if (checkBox_kategori.isChecked()){
                    for (barang x:list_barang) {
                        if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase())){
                            filtered_barang.add(x);
                        }
                    }
                }
                else {
                    list_barang_example.clear();
                    filtered_barang.addAll(list_barang);
                }
            }
            else {
                String fillterpatter = charSequence.toString().toLowerCase().trim();
                if(checkBox_negara.isChecked() && checkBox_kategori.isChecked()){
                    for (barang x:list_barang) {
                        if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase()) && x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase()) && x.getNama().toLowerCase().contains(fillterpatter)){
                            filtered_barang.add(x);
                        }
                    }
                }
                else if(checkBox_negara.isChecked()){
                    for (barang x:list_barang) {
                        if(x.getLokasi().toLowerCase().contains(spinner_negara.getSelectedItem().toString().toLowerCase()) && x.getNama().toLowerCase().contains(fillterpatter)){
                            filtered_barang.add(x);
                        }
                    }
                }
                else if (checkBox_kategori.isChecked()){
                    for (barang x:list_barang) {
                        if(x.getKategori().toLowerCase().contains(edkategori_search.getText().toString().toLowerCase()) && x.getNama().toLowerCase().contains(fillterpatter)){
                            filtered_barang.add(x);
                        }
                    }
                }
                else {
                    for (barang x:list_barang) {
                        if(x.getNama().toLowerCase().contains(fillterpatter)){
                            filtered_barang.add(x);
                        }
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filtered_barang;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list_barang_example.clear();
            list_barang_example.addAll((ArrayList<barang>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class SearchFeedViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container_search_feed;
        TextView detailbarang,tipe,harga;
        ImageView img;
        public SearchFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            detailbarang = itemView.findViewById(R.id.username);
            tipe = itemView.findViewById(R.id.tipe);
            img = itemView.findViewById(R.id.gambar_barang);
            harga = itemView.findViewById(R.id.harga);
            container_search_feed=itemView.findViewById(R.id.container_search_feed);
        }
    }
}
