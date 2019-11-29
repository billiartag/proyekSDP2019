package com.example.proyek_sdp;

import android.content.Context;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFrontAdapter extends RecyclerView.Adapter<ChatFrontAdapter.ChatFrontViewHolder> {
    private Context context;
    private ArrayList<String>list_chat=new ArrayList<String>();
    private OnItemClickListener onItemClickListener;

    public ChatFrontAdapter(Context context, ArrayList<String> list_chat) {
        this.context = context;
        this.list_chat = list_chat;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public ChatFrontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_front_layout,parent,false);
        ChatFrontViewHolder holder=new ChatFrontViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatFrontViewHolder holder, int position) {
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(list_chat.get(position))){
                        FirebaseStorage.getInstance().getReference().child("profil_picture").child(list_chat.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context).load(uri).into(holder.profil_image_chat);
                                holder.username_chat.setText(ds.child("nama").getValue().toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                holder.profil_image_chat.setBackgroundResource(R.drawable.default_profil);
                                holder.username_chat.setText(ds.child("nama").getValue().toString());
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

    @Override
    public int getItemCount() {
        return list_chat.size();
    }

    public class ChatFrontViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        CircleImageView profil_image_chat;
        TextView username_chat;
        public ChatFrontViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_image_chat=itemView.findViewById(R.id.profile_image_chat);
            username_chat=itemView.findViewById(R.id.username_chat);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onClick(itemView, getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(this.getAdapterPosition(),121,0,"Hapus Chat");
        }
    }
    public void delete_chat(int position){
        String email_clicked=list_chat.get(position);
        list_chat.remove(position);
        notifyDataSetChanged();
        FirebaseDatabase.getInstance().getReference().child("ChatDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if((ds.child("penerima_chat").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && ds.child("pengirim_chat").getValue().toString().equals(email_clicked)) || (ds.child("pengirim_chat").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && ds.child("penerima_chat").getValue().toString().equals(email_clicked)) ){
                        FirebaseDatabase.getInstance().getReference().child("ChatDatabase").child(ds.getKey()).getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "chat berhasil dihapus", Toast.LENGTH_SHORT).show();
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
