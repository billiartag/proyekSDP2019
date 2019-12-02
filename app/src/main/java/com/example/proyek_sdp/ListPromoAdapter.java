package com.example.proyek_sdp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListPromoAdapter extends RecyclerView.Adapter<ListPromoAdapter.ListPromoViewHolder> {
    Context context;
    ArrayList<voucher>list_voucher=new ArrayList<voucher>();

    public ListPromoAdapter(Context context, ArrayList<voucher> list_voucher) {
        this.context = context;
        this.list_voucher = list_voucher;
    }

    @NonNull
    @Override
    public ListPromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_promo,parent,false);
        ListPromoViewHolder holder=new ListPromoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListPromoViewHolder holder, int position) {
        holder.nama_voucher.setText("Nama : "+list_voucher.get(position).getNama_promo());
        holder.waktu_voucher.setText("Waktu : "+list_voucher.get(position).getMulai_promo() +" - "+list_voucher.get(position).getSelesai_promo());
        holder.diskon_voucher.setText("Diskon : "+list_voucher.get(position).getDiskon_promo());
        holder.deskripsi_voucher.setText("Deskripsi : "+list_voucher.get(position).getDeskripsi_promo());
    }

    @Override
    public int getItemCount() {
        return list_voucher.size();
    }

    public class ListPromoViewHolder extends RecyclerView.ViewHolder {
        TextView nama_voucher,waktu_voucher,diskon_voucher,deskripsi_voucher;
        public ListPromoViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_voucher=itemView.findViewById(R.id.nama_voucher);
            waktu_voucher=itemView.findViewById(R.id.waktu_voucher);
            diskon_voucher=itemView.findViewById(R.id.diskon_voucher);
            deskripsi_voucher=itemView.findViewById(R.id.deskripsi_voucher);
        }
    }
}
