package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ListFollowAdapter extends RecyclerView.Adapter<ListFollowAdapter.ListFollowViewHolder> {
    Context context;
    ArrayList<user>list_user=new ArrayList<user>();
    int tipe=-1;
    ArrayList<user>list_user_example=new ArrayList<user>();
    EditText edsearch;

    public ListFollowAdapter(Context context, ArrayList<user> list_user, int tipe,EditText edsearch) {
        this.context = context;
        this.list_user = list_user;
        this.tipe = tipe;
        this.edsearch=edsearch;
        if(!edsearch.getText().toString().trim().equals("")){
            for (user x:list_user) {
                if(x.getNama().contains(edsearch.getText().toString())){
                    list_user_example.add(x);
                }
            }
        }
        else {
            list_user_example.addAll(list_user);
        }
    }

    @NonNull
    @Override
    public ListFollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_follow_layout,parent,false);
        ListFollowViewHolder holder=new ListFollowViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListFollowViewHolder holder, int position) {
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(list_user_example.get(position).getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.profil_picture_follow_list);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseStorage.getInstance().getReference().child("profil_picture").child("default_profil.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(holder.profil_picture_follow_list);
                    }
                });
            }
        });
        holder.tvprofil_follow_list.setText(list_user_example.get(position).getNama());
        if(tipe==0){
            holder.tv_follow_list.setVisibility(View.INVISIBLE);
            holder.tv_follow_list.setEnabled(false);
        }
        else if(tipe==1){
            holder.tv_follow_list.setText("Unfollow");
            holder.tv_follow_list.setTextColor(Color.parseColor("#E53935"));
        }
        holder.tv_follow_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=list_user_example.get(position).getEmail();
                list_user_example.remove(position);
                FirebaseDatabase.getInstance().getReference().child("FollowDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count=dataSnapshot.getChildrenCount();
                        for (DataSnapshot ds :dataSnapshot.getChildren()) {
                            if(ds.child("following").getValue().toString().equals(id) && ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                FirebaseDatabase.getInstance().getReference().child("FollowDatabase").child(ds.getKey()).setValue(null);
                                Toast.makeText(context, "Berhasil Di Un-Follow!", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        holder.container_follow_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user x=list_user_example.get(position);
                Bundle b = new Bundle();
                b.putSerializable("user", x);
                Intent intent = new Intent(context, detailprofil.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_user_example.size();
    }

    public class ListFollowViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profil_picture_follow_list;
        TextView tvprofil_follow_list,tv_follow_list;
        LinearLayout container_follow_list;
        public ListFollowViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_picture_follow_list=itemView.findViewById(R.id.profil_picture_follow_list);
            tvprofil_follow_list=itemView.findViewById(R.id.tvprofil_follow_list);
            tv_follow_list=itemView.findViewById(R.id.tv_follow_list);
            container_follow_list=itemView.findViewById(R.id.container_follow_list);
        }
    }
}
