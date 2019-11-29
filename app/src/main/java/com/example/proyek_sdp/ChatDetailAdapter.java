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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.ChatDetailViewHolder> {
    private Context context;
    private ArrayList<chat>list_chat=new ArrayList<chat>();

    public ChatDetailAdapter(Context context, ArrayList<chat> list_chat) {
        this.context = context;
        this.list_chat = list_chat;
    }

    @NonNull
    @Override
    public ChatDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_layout,parent,false);
        ChatDetailViewHolder holder=new ChatDetailViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailViewHolder holder, int position) {
        if(list_chat.get(position).getPengirim_chat().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            holder.chat.setText(list_chat.get(position).getIsi_chat());
            holder.waktu_chat_detail.setText(list_chat.get(position).getWaktu_kirim_chat());
            holder.chat.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.waktu_chat_detail.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
        else if(!list_chat.get(position).getPengirim_chat().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if(ds.child("email").getValue().toString().equals(list_chat.get(position).getPengirim_chat())){
                            FirebaseStorage.getInstance().getReference().child("profil_picture").child(list_chat.get(position).getPengirim_chat()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(context).load(uri).into(holder.profi_picture_chat_detail);
                                    holder.chat.setText(list_chat.get(position).getIsi_chat());
                                    holder.waktu_chat_detail.setText(list_chat.get(position).getWaktu_kirim_chat());
                                    holder.chat.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                    holder.waktu_chat_detail.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    holder.profi_picture_chat_detail.setBackgroundResource(R.drawable.default_profil);
                                    holder.chat.setText(list_chat.get(position).getIsi_chat());
                                    holder.waktu_chat_detail.setText(list_chat.get(position).getWaktu_kirim_chat());
                                    holder.chat.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                    holder.waktu_chat_detail.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list_chat.size();
    }

    public class ChatDetailViewHolder extends RecyclerView.ViewHolder {
        TextView chat,waktu_chat_detail;
        CircleImageView profi_picture_chat_detail;
        public ChatDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            chat=itemView.findViewById(R.id.tvBubbleChat);
            waktu_chat_detail=itemView.findViewById(R.id.waktu_chat_detail);
            profi_picture_chat_detail=itemView.findViewById(R.id.profil_picture_chat_detail);
        }
    }
}
