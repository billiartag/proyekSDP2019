package com.example.proyek_sdp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class TopFlashSaleHomeAdapter extends RecyclerView.Adapter<TopFlashSaleHomeAdapter.TopFlashSaleHomeViewHolder> {
    private Context context;
    private ArrayList<barang> list_barang;
    private OnItemClickListener onItemClickListener;

    public TopFlashSaleHomeAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TopFlashSaleHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flashsale_home,parent,false);
        TopFlashSaleHomeViewHolder holder=new TopFlashSaleHomeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopFlashSaleHomeViewHolder holder, int position) {
        FirebaseStorage.getInstance().getReference().child("img_barang").child(list_barang.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (context!=null){
                    Glide.with(context).load(uri).into(holder.gambar);
                }
            }
        });
        holder.harga.setText("Rp. "+list_barang.get(position).getHarga());
        if (list_barang.get(position).getNama().length()>10){
            holder.nama.setText(list_barang.get(position).getNama().substring(0,10)+"...");
        }
        else {
            holder.nama.setText(list_barang.get(position).getNama()+"...");
        }
    }

    @Override
    public int getItemCount() {
        return list_barang.size();
    }

    public class TopFlashSaleHomeViewHolder extends RecyclerView.ViewHolder {
        public TextView nama,harga;
        public ImageView gambar;
        public TopFlashSaleHomeViewHolder(@NonNull final View itemView) {
            super(itemView);
            nama=itemView.findViewById(R.id.nama);
            harga=itemView.findViewById(R.id.harga);
            gambar=itemView.findViewById(R.id.gambar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onClick(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }
}
