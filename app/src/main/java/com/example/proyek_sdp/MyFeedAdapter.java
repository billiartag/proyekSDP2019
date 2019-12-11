package com.example.proyek_sdp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyek_sdp.Notifications.Client;
import com.example.proyek_sdp.Notifications.Data;
import com.example.proyek_sdp.Notifications.MyResponse;
import com.example.proyek_sdp.Notifications.Sender;
import com.example.proyek_sdp.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder> {
    private Context context;
    private ArrayList<barang> list_barang;
    //untuk notif
    APIService apiService;
    boolean notify=false;
    user usersekarang=new user();

    public MyFeedAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        //set user pengirim
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds2.child("email").getValue().toString())){
                        user user_dikirim=new user();
                        usersekarang.setRating(Float.parseFloat(ds2.child("rating").getValue().toString()));
                        usersekarang.setNama(ds2.child("nama").getValue().toString());
                        usersekarang.setProfil_picture(Integer.parseInt(ds2.child("profil_picture").getValue().toString()));
                        usersekarang.setPhone(ds2.child("phone").getValue().toString());
                        usersekarang.setEmail(ds2.child("email").getValue().toString());
                        usersekarang.setBirthdate(ds2.child("birthdate").getValue().toString());
                        usersekarang.setId(ds2.child("id").getValue().toString());
                        usersekarang.setPassword(ds2.child("password").getValue().toString());
                        usersekarang.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                        usersekarang.setSaldo(Integer.parseInt(ds2.child("saldo").getValue().toString()));
                        usersekarang.setAlamat(ds2.child("alamat").getValue().toString());
                        usersekarang.setVerifikasi_ktp(Integer.parseInt(ds2.child("verifikasi_ktp").getValue().toString()));
                        usersekarang.setFirebase_user_id(ds2.child("firebase_user_id").getValue().toString());
                        updateToken(FirebaseInstanceId.getInstance().getToken());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        //set default
        holder.ed_hargabarang_myfeed.setText(list_barang.get(position).getHarga()+"");
        holder.ed_hargabarang_myfeed.setEnabled(false);
        //set user yang mau dikirimi notifikasi
        FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (ds.child("id_barang_trans").getValue().toString().equals(list_barang.get(position).getId()) && ds.child("status_trans").getValue().toString().equals("pending")){
                        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                   if(ds.child("id_user_trans").getValue().toString().equals(ds2.child("email").getValue().toString())){
                                        user user_dikirim=new user();
                                        user_dikirim.setRating(Float.parseFloat(ds2.child("rating").getValue().toString()));
                                        user_dikirim.setNama(ds2.child("nama").getValue().toString());
                                        user_dikirim.setProfil_picture(Integer.parseInt(ds2.child("profil_picture").getValue().toString()));
                                        user_dikirim.setPhone(ds2.child("phone").getValue().toString());
                                        user_dikirim.setEmail(ds2.child("email").getValue().toString());
                                        user_dikirim.setBirthdate(ds2.child("birthdate").getValue().toString());
                                        user_dikirim.setId(ds2.child("id").getValue().toString());
                                        user_dikirim.setPassword(ds2.child("password").getValue().toString());
                                        user_dikirim.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                                        user_dikirim.setSaldo(Integer.parseInt(ds2.child("saldo").getValue().toString()));
                                        user_dikirim.setAlamat(ds2.child("alamat").getValue().toString());
                                        user_dikirim.setVerifikasi_ktp(Integer.parseInt(ds2.child("verifikasi_ktp").getValue().toString()));
                                        user_dikirim.setFirebase_user_id(ds2.child("firebase_user_id").getValue().toString());
                                        holder.list_user_dikirim.add(user_dikirim);
                                   }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //btn ubah harga
        holder.btn_ubahharga_myfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btn_ubahharga_myfeed.getText().toString().equals("edit")){
                    holder.btn_ubahharga_myfeed.setText("save");
                    holder.ed_hargabarang_myfeed.setEnabled(true);
                    holder.hargalalu=holder.ed_hargabarang_myfeed.getText().toString();
                }
                else if(holder.btn_ubahharga_myfeed.getText().toString().equals("save")){
                    holder.btn_ubahharga_myfeed.setText("edit");
                    holder.ed_hargabarang_myfeed.setEnabled(false);
                    if(!holder.hargalalu.equals(holder.ed_hargabarang_myfeed.getText().toString())) {
                        //send notifikasi
                        for (int i=0;i<holder.list_user_dikirim.size();i++){
                            user x=holder.list_user_dikirim.get(i);
                            notify=true;
                            final String msg="Terdapat Perubahan Pada Barang !";
                            if(x!=null){
                                if(notify){
                                    sendNotification(x.getFirebase_user_id(),list_barang.get(position).getNama(),msg,x);
                                }
                                notify=false;
                            }
                        }
                        //ubah data di database
                        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").child(list_barang.get(position).getId()).child("harga").setValue(holder.ed_hargabarang_myfeed.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.child("id_barang_trans").getValue().toString().equals(list_barang.get(position).getId()) && ds.child("status_trans").getValue().toString().equals("pending")) {
                                                //update value
                                                FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase").child(ds.child("id_transaksi").getValue().toString()).child("status_trans").setValue("perubahan");

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        //cari gambar img di database
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

    public void sendNotification(String receiver,final String username,final String message,user x){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(usersekarang.getFirebase_user_id(), R.mipmap.logo_icon_app_round,username+": "+message,"New message",x.getFirebase_user_id());

                    Sender sender=new Sender(data,token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200){
                                if(response.body().success!=1){
                                    Toast.makeText(context, "Failed Send Notification!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        Button hapus_feed,btn_ubahharga_myfeed;
        EditText ed_hargabarang_myfeed;
        String hargalalu="";
        ArrayList<user>list_user_dikirim=new ArrayList<user>();

        public MyFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_picture_feed=itemView.findViewById(R.id.profil_picture_feed);
            img_barang_feed=itemView.findViewById(R.id.img_barang_feed);
            user_feed=itemView.findViewById(R.id.user_feed);
            desc=itemView.findViewById(R.id.desc);
            hapus_feed=itemView.findViewById(R.id.hapus_feed);
            btn_ubahharga_myfeed=itemView.findViewById(R.id.btn_ubahharga_myfeed);
            ed_hargabarang_myfeed=itemView.findViewById(R.id.ed_hargabarang_myfeed);
        }
    }
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(usersekarang.getFirebase_user_id()).setValue(token1);
    }
}
