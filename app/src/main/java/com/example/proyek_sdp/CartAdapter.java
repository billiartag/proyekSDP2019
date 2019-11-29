package com.example.proyek_sdp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<barang> list_barang;

    public CartAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cart_layout,parent,false);
        CartViewHolder holder=new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.container_barang_cart.setBackgroundResource(android.R.drawable.alert_light_frame);
        //holder.img_barang_cart.setBackgroundResource(list_barang.get(position).getGambar());
        holder.nama_barang.setText(list_barang.get(position).getNama());
        //holder.harga_barang.setText("Rp "+list_barang.get(position).getHarga());
        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Ditambah Ke Wishlist", Toast.LENGTH_SHORT).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_barang.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.total_barang.getText().toString())>1){
                    holder.total_barang.setText(Integer.parseInt(holder.total_barang.getText().toString())-1+"");
                }
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.total_barang.setText(Integer.parseInt(holder.total_barang.getText().toString())+1+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_barang.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container_barang_cart;
        ImageView img_barang_cart;
        TextView nama_barang,harga_barang;
        ImageButton wishlist,delete,min,plus;
        EditText total_barang;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img_barang_cart=itemView.findViewById(R.id.img_barang_cart);
            nama_barang=itemView.findViewById(R.id.judul_barang_cart);
            harga_barang=itemView.findViewById(R.id.harga_barang_cart);
            wishlist=itemView.findViewById(R.id.btn_wishlist_cart);
            delete=itemView.findViewById(R.id.btn_delete_cart);
            min=itemView.findViewById(R.id.btn_min_cart);
            plus=itemView.findViewById(R.id.btn_plus_cart);
            total_barang=itemView.findViewById(R.id.jumlah_barang_cart);
            container_barang_cart=itemView.findViewById(R.id.container_barang_cart);
        }
    }
}
