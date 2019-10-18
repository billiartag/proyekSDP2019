package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class wishlist extends AppCompatActivity {

    ArrayList<barang> listBarang = new ArrayList<barang>();
    RecyclerView rvWishlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        rvWishlist = findViewById(R.id.rvWishlist);

        listBarang.add(new barang("sepeda",R.drawable.bike));
        listBarang.add(new barang("treadmill",R.drawable.electrictreadmill));
        listBarang.add(new barang("logo",R.drawable.logo));

        rvWishlist.setLayoutManager(new GridLayoutManager(this,2));

        GridWishlist adap = new GridWishlist(listBarang);
        rvWishlist.setAdapter(adap);

    }

    class barang{
        String nama;
        int gambar;

        public barang(String nama, int gambar) {
            this.nama = nama;
            this.gambar = gambar;
        }
    }

    public class GridWishlist extends RecyclerView.Adapter<GridWishlist.GridViewHolder> {
        ArrayList<barang> listBarang = new ArrayList<barang>();

        public GridWishlist(ArrayList<barang> listBarang) {
            this.listBarang = listBarang;
        }


        @NonNull
        @Override
        public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_wishlist, parent, false);
            return new GridViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
            holder.namaBarang.setText(listBarang.get(holder.getAdapterPosition()).nama);
            holder.imageBarang.setImageResource(listBarang.get(holder.getAdapterPosition()).gambar);
        }

        @Override
        public int getItemCount() {
            return listBarang.size();
        }

        public class GridViewHolder extends RecyclerView.ViewHolder {
            ImageView imageBarang;
            TextView namaBarang;
            Button btnCari;

            public GridViewHolder(@NonNull View itemView) {
                super(itemView);
                imageBarang = itemView.findViewById(R.id.ivImageBarang);
                namaBarang = itemView.findViewById(R.id.tvNamaBarang);
                btnCari = itemView.findViewById(R.id.btnCari);
            }
        }
    }
}
