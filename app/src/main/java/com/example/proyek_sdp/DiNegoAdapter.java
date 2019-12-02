package com.example.proyek_sdp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class DiNegoAdapter extends RecyclerView.Adapter<DiNegoAdapter.DiNegoViewHolder>{
    private Context context;
    private ArrayList<barang_nego> list_nego;
    public DiNegoAdapter(Context context, ArrayList<barang_nego> list_nego) {
        this.context = context;
        this.list_nego = list_nego;
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
        barang barang_temp = list_nego.get(position).getBarang();
        Nego nego_temp = list_nego.get(position).getNego();

        FirebaseStorage.getInstance().getReference().child("img_barang").child(barang_temp.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.gambarNego);
            }
        });

        holder.namaBarang.setText("Nama: "+barang_temp.getNama()+"");
        holder.hargaAwal.setText("Harga awal: "+barang_temp.getHarga()+"");

        holder.hargaNego.setText("Aku nego: "+nego_temp.getNominal_nego()+"");
        holder.sisNego.setText("Sisa nego: "+nego_temp.getSisa_nego()+"");
        String status_nego = nego_temp.getStatus_nego();
        holder.statusNego.setText("Status: "+status_nego+"");
        holder.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiNego)context).dinego(list_nego.get(position).getNego().getId_nego(),"terima");
                Toast.makeText(context, "Nego diterima", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiNego)context).dinego(list_nego.get(position).getNego().getId_nego(),"tolak");
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
        TextView varianNego;
        ImageView gambarNego;
        Button btnTolak, btnTerima,btnNegoUlang;
        public DiNegoViewHolder(@NonNull View itemView) {
            super(itemView);
            namaBarang= itemView.findViewById(R.id.textViewLayoutNegoNama);
            hargaAwal= itemView.findViewById(R.id.textViewLayoutNegoHarga);
            hargaNego= itemView.findViewById(R.id.textViewLayoutNegoHargaBaru);
            sisNego= itemView.findViewById(R.id.textViewLayoutNegoSisaNego);
            statusNego= itemView.findViewById(R.id.textViewLayoutNegoStatusNego);
            varianNego= itemView.findViewById(R.id.textViewLayoutNegoVarian);
            btnTolak= itemView.findViewById(R.id.buttonLayoutNegoTolak);
            btnTerima= itemView.findViewById(R.id.buttonLayoutNegoTerima);
            btnNegoUlang= itemView.findViewById(R.id.buttonLayoutNegoUlang);
            gambarNego = itemView.findViewById(R.id.imageViewLayoutNego);
            btnTerima.setVisibility(View.VISIBLE);
            btnTolak.setVisibility(View.VISIBLE);
            btnNegoUlang.setVisibility(View.GONE);
        }
    }
}
