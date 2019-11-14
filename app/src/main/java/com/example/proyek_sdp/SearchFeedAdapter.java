package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchFeedAdapter extends RecyclerView.Adapter<SearchFeedAdapter.SearchFeedViewHolder> {
    private Context context;
    private ArrayList<barang>list_barang;

    public SearchFeedAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
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
        holder.detailbarang.setText(list_barang.get(position).toString());
        if (list_barang.get(position).getTipe()=="Flash Sale"){
            holder.tipe.setBackgroundColor(Color.parseColor("#FB8C00"));
            holder.tipe.setTextColor(Color.BLACK);
            holder.tipe.setText(list_barang.get(position).getTipe());
        }
        else if (list_barang.get(position).getTipe()=="Pre Order"){
            holder.tipe.setBackgroundColor(Color.BLACK);
            holder.tipe.setTextColor(Color.WHITE);
            holder.tipe.setText(list_barang.get(position).getTipe());
        }
        holder.harga.setText("Rp. "+list_barang.get(position).getHarga());
        holder.harga.setTextColor(Color.parseColor("#651FFF"));
        holder.img.setImageResource(list_barang.get(position).getGambar());
        holder.container_search_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barang x=list_barang.get(position);
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
        return list_barang.size();
    }

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
