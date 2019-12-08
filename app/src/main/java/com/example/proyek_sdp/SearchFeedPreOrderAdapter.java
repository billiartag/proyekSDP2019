package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SearchFeedPreOrderAdapter extends RecyclerView.Adapter<SearchFeedPreOrderAdapter.SearchFeedPreOrderViewHolder> {
    private Context context;
    private ArrayList<barang> list_barang;
    private ArrayList<barang>list_barang_example=new ArrayList<barang>();
    private Spinner spinner_negara;
    private CheckBox checkBox_negara;
    private EditText edkategori_search;
    private CheckBox checkBox_kategori;
    private SearchView searchView;

    public SearchFeedPreOrderAdapter(Context context, ArrayList<barang> list_barang, Spinner spinner_negara, CheckBox checkBox_negara, EditText edkategori_search, CheckBox checkBox_kategori, SearchView searchView) {
        this.context = context;
        this.list_barang = list_barang;
        this.spinner_negara = spinner_negara;
        this.checkBox_negara = checkBox_negara;
        this.edkategori_search = edkategori_search;
        this.checkBox_kategori = checkBox_kategori;
        this.searchView = searchView;
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
    public SearchFeedPreOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_preorder_layout,parent,false);
        SearchFeedPreOrderViewHolder holder=new SearchFeedPreOrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFeedPreOrderViewHolder holder, int position) {
        holder.tv_tipe_preorder_search.setText(list_barang_example.get(position).getJenis());
        holder.tv_waktu_preorder_search.setText("Dari : "+list_barang_example.get(position).getWaktu_mulai()+" Sampai : "+list_barang_example.get(position).getWaktu_selesai());
        holder.tv_nama_preorder_search.setText(list_barang_example.get(position).getNama());
        holder.tv_deskripsi_preorder_search.setText(list_barang_example.get(position).getDeskripsi());
        FirebaseStorage.getInstance().getReference().child("img_barang").child(list_barang_example.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.img_preorder_search);
            }
        });
        holder.container_barang_preorder_search.setOnClickListener(new View.OnClickListener() {
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

    public class SearchFeedPreOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tipe_preorder_search,tv_waktu_preorder_search,tv_nama_preorder_search,tv_deskripsi_preorder_search;
        ImageView img_preorder_search;
        LinearLayout container_barang_preorder_search;
        public SearchFeedPreOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tipe_preorder_search=itemView.findViewById(R.id.tv_tipe_preorder_search);
            tv_waktu_preorder_search=itemView.findViewById(R.id.tv_waktu_preorder_search);
            tv_nama_preorder_search=itemView.findViewById(R.id.tv_nama_preorder_search);
            tv_deskripsi_preorder_search=itemView.findViewById(R.id.tv_deskripsi_preorder_search);
            img_preorder_search=itemView.findViewById(R.id.img_preorder_search);
            container_barang_preorder_search=itemView.findViewById(R.id.container_barang_preorder_search);
        }
    }
    public static String formatSeconds(int timeInSeconds)
    {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }
}
