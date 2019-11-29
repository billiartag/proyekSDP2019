package com.example.proyek_sdp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AkuNegoAdapter extends RecyclerView.Adapter<AkuNegoAdapter.AkuNegoViewHolder> {
    private Context context;
    private ArrayList<Nego> list_nego;
    private ArrayList<barang> list_barang;

    public AkuNegoAdapter(Context context, ArrayList<Nego> list_nego, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_nego = list_nego;
        this.list_barang = list_barang;
    }

    @NonNull
    @Override
    public AkuNegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_nego,parent,false);
        AkuNegoViewHolder holder=new AkuNegoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AkuNegoViewHolder holder, int position) {
        barang temp = null;
        for (barang r:list_barang) {
            //kalau idnya sama ambil jadi temp
        }
//        holder.namaBarang.setText("Nama: "temp.getNama()+"");
//        holder.hargaAwal.setText(temp.getHarga()+"");
        holder.hargaNego.setText("Aku nego: "+list_nego.get(position).getNominal_nego()+"");
        holder.sisNego.setText("Sisa nego: "+list_nego.get(position).getSisa_nego()+"");
        String status_nego = list_nego.get(position).getStatus_nego();
        holder.statusNego.setText("Status: "+status_nego+"");
        if(status_nego.equalsIgnoreCase("tolak")){
            holder.statusNego.setTextColor(Color.RED);
            if(list_nego.get(position).getSisa_nego()>0){
                holder.btnNegoUlang.setVisibility(View.VISIBLE);}
            else{
                holder.btnNegoUlang.setVisibility(View.GONE);
            }
        }
        else if(status_nego.equalsIgnoreCase("terima")){
            holder.statusNego.setTextColor(Color.BLUE);
        }
        holder.btnNegoUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ((AkuNego)context).akuNego(list_nego.get(position).getId_nego(), list_nego.get(position).getSisa_nego());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_nego.size();
    }


    public class AkuNegoViewHolder extends RecyclerView.ViewHolder {
        TextView namaBarang;
        TextView hargaAwal;
        TextView hargaNego;
        TextView sisNego;
        TextView statusNego;
        Button btnTolak, btnTerima,btnNegoUlang;
        public AkuNegoViewHolder(@NonNull View itemView) {
            super(itemView);
            namaBarang= itemView.findViewById(R.id.textViewLayoutNegoNama);
            hargaAwal= itemView.findViewById(R.id.textViewLayoutNegoHarga);
            hargaNego= itemView.findViewById(R.id.textViewLayoutNegoHargaBaru);
            sisNego= itemView.findViewById(R.id.textViewLayoutNegoSisaNego);
            statusNego= itemView.findViewById(R.id.textViewLayoutNegoStatusNego);
            btnTolak= itemView.findViewById(R.id.buttonLayoutNegoTolak);
            btnTerima= itemView.findViewById(R.id.buttonLayoutNegoTerima);
            btnNegoUlang= itemView.findViewById(R.id.buttonLayoutNegoUlang);
            btnTerima.setVisibility(View.GONE);
            btnTolak.setVisibility(View.GONE);
        }
    }
}
