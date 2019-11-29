package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SearchFeedAdapter extends RecyclerView.Adapter<SearchFeedAdapter.SearchFeedViewHolder> implements Filterable {
    private Context context;
    private ArrayList<barang>list_barang;
    private ArrayList<barang>list_barang_example=new ArrayList<barang>();

    public SearchFeedAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
        list_barang_example.addAll(list_barang);
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
                filtered_barang.addAll(list_barang);
            }
            else {
                String fillterpatter = charSequence.toString().toLowerCase().trim();
                for (barang x:list_barang) {
                    if(x.getNama().toLowerCase().contains(fillterpatter)){
                        filtered_barang.add(x);
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
