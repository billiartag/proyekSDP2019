package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.Calendar;

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
        if (holder.timer != null) {
            holder.timer.cancel();
        }
        holder.timer = new CountDownTimer(999999999, 1000) {

            @Override
            public void onTick(long l) {
                String waktu=list_barang.get(position).getWaktu_selesai();
                String[] waktu_split=waktu.split(":");
                if(waktu_split.length>1){
                    int jam_selesai=Integer.parseInt(waktu_split[0]);
                    int menit_selesai=Integer.parseInt(waktu_split[1]);
                    int detik_selesai=Integer.parseInt(waktu_split[2]);
                    int total_waktu_selesai=(jam_selesai*3600) + (menit_selesai*60) + detik_selesai;
                    Calendar now = Calendar.getInstance();
                    int jam_mulai=now.get(Calendar.HOUR_OF_DAY);
                    int menit_mulai=now.get(Calendar.MINUTE);
                    int detik_mulai=now.get(Calendar.SECOND);
                    int total_waktu_mulai=(jam_mulai*3600) + (menit_mulai*60) + detik_mulai;
                    if(total_waktu_selesai-total_waktu_mulai>0){
                        holder.tvtimer_search.setText("Sisa Waktu : \n"+formatSeconds(total_waktu_selesai-total_waktu_mulai));
                    }
                    else {
                        holder.tvtimer_search.setText("expired");
                    }
                }
                else {
                    holder.tvtimer_search.setText("Mulai : \n"+list_barang.get(position).getWaktu_mulai()+"\n Sampai \n"+list_barang.get(position).getWaktu_selesai());
                }
            }

            @Override
            public void onFinish() {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        }.start();
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
        TextView detailbarang,tipe,harga,tvtimer_search;
        CountDownTimer timer;
        ImageView img;
        public SearchFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            detailbarang = itemView.findViewById(R.id.username);
            tipe = itemView.findViewById(R.id.tipe);
            img = itemView.findViewById(R.id.gambar_barang);
            harga = itemView.findViewById(R.id.harga);
            tvtimer_search = itemView.findViewById(R.id.tvtimer_search);
            container_search_feed=itemView.findViewById(R.id.container_search_feed);
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
