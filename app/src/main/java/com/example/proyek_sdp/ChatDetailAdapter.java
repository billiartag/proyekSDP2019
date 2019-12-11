package com.example.proyek_sdp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.ChatDetailViewHolder> {
    private Context context;
    private ArrayList<chat>list_chat=new ArrayList<chat>();
    String temptgl="";

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
            String[] splitwaktu=list_chat.get(position).getWaktu_kirim_chat().split("-");
            if(!temptgl.equals(splitwaktu[0])){
                temptgl=splitwaktu[0];
                //cek today
                Date dt = new Date();
                SimpleDateFormat sdf2 = new SimpleDateFormat("d/MM/yyyy");
                String time2 = sdf2.format(dt);
                //convert string tanggal ke masing2 atribute tanggal
                String temp_tanggal_hitung[]=splitwaktu[0].trim().split("/");
                int tgl=Integer.parseInt(temp_tanggal_hitung[0]);
                int bulan=Integer.parseInt(temp_tanggal_hitung[1]);
                int tahun=Integer.parseInt(temp_tanggal_hitung[2]);
                //convert string tanggal sekarang ke masing masing attribute tanggal
                String temp_tanggal_hitung_sekarang[]=time2.trim().split("/");
                int tgl_sekarang=Integer.parseInt(temp_tanggal_hitung_sekarang[0]);
                int bulan_sekarang=Integer.parseInt(temp_tanggal_hitung_sekarang[1]);
                int tahun_sekarang=Integer.parseInt(temp_tanggal_hitung_sekarang[2]);
                if(tgl_sekarang-1==tgl && bulan_sekarang==bulan && tahun_sekarang==tahun){
                    holder.tvtanggal_detail_feed.setText("Kemarin");
                }
                else if(time2.trim().equals(temptgl.trim())){
                    holder.tvtanggal_detail_feed.setText("Hari Ini");
                }
                //bukan yesterday atau today
                else {
                    holder.tvtanggal_detail_feed.setText(splitwaktu[0]);
                }
                holder.tvtanggal_detail_feed.setBackgroundResource(R.drawable.border);
            }
            else {
                holder.tvtanggal_detail_feed.setVisibility(View.GONE);
            }
            holder.chat.setText(list_chat.get(position).getIsi_chat());
            holder.waktu_chat_detail.setText(splitwaktu[1]);
            holder.chat.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.waktu_chat_detail.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.profi_picture_chat_detail.setVisibility(View.GONE);

        }
        else if(!list_chat.get(position).getPengirim_chat().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            String[] splitwaktu=list_chat.get(position).getWaktu_kirim_chat().split("-");
            if(!temptgl.equals(splitwaktu[0])){
                temptgl=splitwaktu[0];
                //cek today
                Date dt = new Date();
                SimpleDateFormat sdf2 = new SimpleDateFormat("d/MM/yyyy");
                String time2 = sdf2.format(dt);
                //convert string tanggal ke masing2 atribute tanggal
                String temp_tanggal_hitung[]=splitwaktu[0].trim().split("/");
                int tgl=Integer.parseInt(temp_tanggal_hitung[0]);
                int bulan=Integer.parseInt(temp_tanggal_hitung[1]);
                int tahun=Integer.parseInt(temp_tanggal_hitung[2]);
                //convert string tanggal sekarang ke masing masing attribute tanggal
                String temp_tanggal_hitung_sekarang[]=time2.trim().split("/");
                int tgl_sekarang=Integer.parseInt(temp_tanggal_hitung_sekarang[0]);
                int bulan_sekarang=Integer.parseInt(temp_tanggal_hitung_sekarang[1]);
                int tahun_sekarang=Integer.parseInt(temp_tanggal_hitung_sekarang[2]);
                if(tgl_sekarang-1==tgl && bulan_sekarang==bulan && tahun_sekarang==tahun){
                    holder.tvtanggal_detail_feed.setText("Kemarin");
                }
                else if(time2.trim().equals(temptgl.trim())){
                    holder.tvtanggal_detail_feed.setText("Hari Ini");
                }
                //bukan yesterday atau today
                else {
                    holder.tvtanggal_detail_feed.setText(splitwaktu[0]);
                }
                holder.tvtanggal_detail_feed.setBackgroundResource(R.drawable.border);
            }
            else {
                holder.tvtanggal_detail_feed.setVisibility(View.GONE);
            }
            holder.chat.setText(list_chat.get(position).getIsi_chat());
            holder.waktu_chat_detail.setText(splitwaktu[1]);
            holder.chat.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.waktu_chat_detail.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(list_chat.get(position).getPengirim_chat())){
                        FirebaseStorage.getInstance().getReference().child("profil_picture").child(list_chat.get(position).getPengirim_chat()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context).load(uri).into(holder.profi_picture_chat_detail);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                holder.profi_picture_chat_detail.setBackgroundResource(R.drawable.default_profil);
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

    public class ChatDetailViewHolder extends RecyclerView.ViewHolder {
        TextView chat,waktu_chat_detail,tvtanggal_detail_feed;
        CircleImageView profi_picture_chat_detail;
        public ChatDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtanggal_detail_feed=itemView.findViewById(R.id.tvtanggal_detail_feed);
            chat=itemView.findViewById(R.id.tvBubbleChat);
            waktu_chat_detail=itemView.findViewById(R.id.waktu_chat_detail);
            profi_picture_chat_detail=itemView.findViewById(R.id.profil_picture_chat_detail);
        }
    }
}
