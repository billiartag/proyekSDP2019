package com.example.proyek_sdp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopuserAdapter extends RecyclerView.Adapter<TopuserAdapter.TopuserViewHolder> {
    private Context context;
    private ArrayList<user> list_user=new ArrayList<user>();
    private OnItemClickListener onItemClickListener;

    public TopuserAdapter(Context context, ArrayList<user> list_user) {
        this.context = context;
        this.list_user = list_user;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TopuserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_topuser_home,parent,false);
        TopuserViewHolder holder=new TopuserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopuserViewHolder holder, int position) {
        holder.fotoprofil.setImageResource(list_user.get(position).getProfil_picture());
        holder.username.setText(list_user.get(position).getNama());
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
