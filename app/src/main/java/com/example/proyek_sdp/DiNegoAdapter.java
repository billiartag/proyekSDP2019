package com.example.proyek_sdp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiNegoAdapter extends RecyclerView.Adapter<DiNegoAdapter.DiNegoViewHolder>{
    private Context context;
    private ArrayList<Nego> list_nego;
    private ArrayList<barang> list_barang;

    public DiNegoAdapter(Context context, ArrayList<Nego> list_nego, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_nego = list_nego;
        this.list_barang = list_barang;
    }

    @NonNull
    @Override
    public DiNegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_nego,parent,false);
        DiNegoViewHolder holder=new DiNegoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiNegoViewHolder holder, int position) {

        barang temp = null;
        for (barang r:list_barang) {
            //kalau idnya sama ambil jadi temp
        }
//        holder.namaBarang.setText(temp.getNama()+"");
//        holder.hargaAwal.setText(temp.getHarga()+"");
        holder.hargaNego.setText("Dinego: "+list_nego.get(position).getNominal_nego()+"");
        holder.sisNego.setText("Sisa nego: "+list_nego.get(position).getSisa_nego()+"");
        holder.statusNego.setText("Status: "+list_nego.get(position).getStatus_nego()+"");
        holder.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiNego)context).dinego(list_nego.get(position).getId_nego(),"terima");
                Toast.makeText(context, "Nego diterima", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiNego)context).dinego(list_nego.get(position).getId_nego(),"tolak");
                Toast.makeText(context, "Nego ditolak", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_nego.size();
    }

    public class DiNegoViewHolder extends RecyclerView.ViewHolder {
        TextView namaBarang;
        TextView hargaAwal;
        TextView hargaNego;
        TextView sisNego;
        TextView statusNego;
        Button btnTolak, btnTerima,btnNegoUlang;
        public DiNegoViewHolder(@NonNull View itemView) {
            super(itemView);
            namaBarang= itemView.findViewById(R.id.textViewLayoutNegoNama);
            hargaAwal= itemView.findViewById(R.id.textViewLayoutNegoHarga);
            hargaNego= itemView.findViewById(R.id.textViewLayoutNegoHargaBaru);
            sisNego= itemView.findViewById(R.id.textViewLayoutNegoSisaNego);
            statusNego= itemView.findViewById(R.id.textViewLayoutNegoStatusNego);
            btnTolak= itemView.findViewById(R.id.buttonLayoutNegoTolak);
            btnTerima= itemView.findViewById(R.id.buttonLayoutNegoTerima);
            btnNegoUlang= itemView.findViewById(R.id.buttonLayoutNegoUlang);

            btnTerima.setVisibility(View.VISIBLE);
            btnTolak.setVisibility(View.VISIBLE);
            btnNegoUlang.setVisibility(View.GONE);
        }
    }
}
