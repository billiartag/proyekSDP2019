package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private Context context;
    private ArrayList<barang> list_barang;

    public WishlistAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_wishlist_layout,parent,false);
        WishlistViewHolder holder=new WishlistViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        holder.namabarang.setText(list_barang.get(position).getNama());
        FirebaseStorage.getInstance().getReference().child("img_barang").child(list_barang.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imgbarang);
            }
        });
        holder.cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barang x=list_barang.get(position);
                Bundle b = new Bundle();
                b.putSerializable("barang", x);
                Intent intent = new Intent(context, detail_feed.class);
                intent.putExtras(b);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_barang.size();
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        Button cari;
        TextView namabarang;
        ImageView imgbarang;
        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            cari=itemView.findViewById(R.id.btnCari_wishlist);
            namabarang=itemView.findViewById(R.id.tvNamaBarang);
            imgbarang=itemView.findViewById(R.id.ivImageBarang);
        }
    }
}
