package com.example.proyek_sdp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopuserSeeallAdapter extends RecyclerView.Adapter<TopuserSeeallAdapter.TopuserViewHolder> {
    private Context context;
    private ArrayList<user> list_user=new ArrayList<user>();
    private OnItemClickListener onItemClickListener;

    public TopuserSeeallAdapter(Context context, ArrayList<user> list_user) {
        this.context = context;
        this.list_user = list_user;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TopuserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_topuser_seeall,parent,false);
        TopuserViewHolder holder=new TopuserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopuserViewHolder holder, int position) {
        holder.username.setText(list_user.get(position).getNama());
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(list_user.get(position).getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.fotoprofil);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.fotoprofil.setBackgroundResource(R.drawable.default_profil);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_user.size();
    }

    public class TopuserViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView fotoprofil;
        public TextView username;
        public TopuserViewHolder(@NonNull final View itemView) {
            super(itemView);
            fotoprofil=itemView.findViewById(R.id.profile_image);
            username=itemView.findViewById(R.id.edemail_login);
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
