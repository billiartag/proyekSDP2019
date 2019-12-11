package com.example.proyek_sdp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AkuNegoAdapter extends RecyclerView.Adapter<AkuNegoAdapter.AkuNegoViewHolder> {
    private Context context;
    private ArrayList<barang_nego> list_nego;

    public AkuNegoAdapter(Context context, ArrayList<barang_nego> list_nego) {
        this.context = context;
        this.list_nego = list_nego;
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
        barang barang_temp = list_nego.get(position).getBarang();
        Nego nego_temp = list_nego.get(position).getNego();
try {
    FirebaseStorage.getInstance().getReference().child("img_barang").child(barang_temp.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            Glide.with(context).load(uri).into(holder.gambarNego);
        }
    });
}catch (Exception e){}

        holder.namaBarang.setText("Nama: "+barang_temp.getNama()+"");
        holder.hargaAwal.setText("Harga awal: "+barang_temp.getHarga()+"");
        holder.varianNego.setText("Varian: "+nego_temp.getVarian()+"");
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(list_nego.get(position).getNego().id_seller)){
                        holder.textViewsiapayangmelakukan_nego_layout.setText("Penjual : "+ds.child("nama").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.hargaNego.setText("Aku nego: "+nego_temp.getNominal_nego()+"");
        holder.sisNego.setText("Sisa nego: "+nego_temp.getSisa_nego()+"");
        String status_nego = nego_temp.getStatus_nego();
        holder.statusNego.setText("Status: "+status_nego+"");
        if(status_nego.equalsIgnoreCase("tolak")){
            holder.statusNego.setTextColor(Color.RED);
            if(nego_temp.getSisa_nego()>0){
                holder.btnNegoUlang.setVisibility(View.VISIBLE);}
            else{
                holder.btnNegoUlang.setVisibility(View.GONE);
            }
        }
        else if(status_nego.equalsIgnoreCase("terima")){
            holder.statusNego.setTextColor(Color.BLUE);
            holder.btnNegoUlang.setVisibility(View.VISIBLE);
            holder.btnNegoUlang.setText("Lanjut beli");
            holder.btnNegoUlang.setBackgroundColor(Color.GREEN);
        }
        holder.btnNegoUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status_nego.equalsIgnoreCase("terima")){
                    ((AkuNego)context).akuBeli(list_nego.get(position));
                }
                else
                {
                    ((AkuNego)context).akuNego(list_nego.get(position));
                }
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
        TextView varianNego;
        ImageView gambarNego;
        TextView textViewsiapayangmelakukan_nego_layout;
        Button btnTolak, btnTerima,btnNegoUlang;
        public AkuNegoViewHolder(@NonNull View itemView) {
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
            textViewsiapayangmelakukan_nego_layout=itemView.findViewById(R.id.textViewsiapayangmelakukan_nego_layout);
            btnTerima.setVisibility(View.GONE);
            btnTolak.setVisibility(View.GONE);
        }
    }
}
