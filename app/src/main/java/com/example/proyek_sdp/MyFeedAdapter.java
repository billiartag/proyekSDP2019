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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
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
        FirebaseStorage.getInstance().getReference().child("img_barang").child(list_barang.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (context!=null){
                    Glide.with(context).load(uri).into(holder.img_barang_feed);
                }
            }
        });
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (context!=null){
                    Glide.with(context).load(uri).into(holder.profil_picture_feed);
                }
            }
        });
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                        holder.user_feed.setText(ds.child("nama").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.desc.setText("Deskripsi : \n"+list_barang.get(position).getDeskripsi());
        holder.hapus_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_barang=list_barang.get(position).getId();
                barang data=list_barang.get(position);
                list_barang.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Post Berhasil Dihapus", Toast.LENGTH_SHORT).show();

                data.setStatus(0);
                FirebaseDatabase.getInstance().getReference().child("BarangDatabase").child(id_barang).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
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
