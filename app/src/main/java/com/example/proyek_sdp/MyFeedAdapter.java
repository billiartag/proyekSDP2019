package com.example.proyek_sdp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder> {
    private Context context;
    private ArrayList<barang> list_barang;

    public MyFeedAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
    }

    @NonNull
    @Override
    public MyFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_feed_layout,parent,false);
        MyFeedViewHolder holder=new MyFeedViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyFeedViewHolder holder, int position) {
        holder.img_barang_feed.setBackgroundResource(list_barang.get(position).getGambar());
        holder.desc.setText("Deskripsi : \n"+list_barang.get(position).getDeskripsi());
        holder.hapus_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_barang.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_barang.size();
    }

    public class MyFeedViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profil_picture_feed;
        ImageView img_barang_feed;
        TextView user_feed,desc;
        Button hapus_feed;

        public MyFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_picture_feed=itemView.findViewById(R.id.profil_picture_feed);
            img_barang_feed=itemView.findViewById(R.id.img_barang_feed);
            user_feed=itemView.findViewById(R.id.user_feed);
            desc=itemView.findViewById(R.id.desc);
            hapus_feed=itemView.findViewById(R.id.hapus_feed);
        }
    }
}
