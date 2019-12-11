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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyek_sdp.Notifications.Client;
import com.example.proyek_sdp.Notifications.Data;
import com.example.proyek_sdp.Notifications.MyResponse;
import com.example.proyek_sdp.Notifications.Sender;
import com.example.proyek_sdp.Notifications.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiNegoAdapter extends RecyclerView.Adapter<DiNegoAdapter.DiNegoViewHolder>{
    private Context context;
    private ArrayList<barang_nego> list_nego;
    //untuk notif
    APIService apiService;
    boolean notify=false;
    user usersekarang;
    public DiNegoAdapter(Context context, ArrayList<barang_nego> list_nego) {
        this.context = context;
        this.list_nego = list_nego;
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        usersekarang=new user();
        getusernow();
    }

    @NonNull
    @Override
    public DiNegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_nego,parent,false);
        DiNegoViewHolder holder=new DiNegoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiNegoViewHolder holder, int position) {
        barang barang_temp = list_nego.get(position).getBarang();
        Nego nego_temp = list_nego.get(position).getNego();
        getusertujuan(nego_temp.id_user_nego,holder.x);

        FirebaseStorage.getInstance().getReference().child("img_barang").child(barang_temp.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.gambarNego);
            }
        });

        holder.namaBarang.setText("Nama: "+barang_temp.getNama()+"");
        holder.hargaAwal.setText("Harga awal: "+barang_temp.getHarga()+"");

        holder.hargaNego.setText("Aku nego: "+nego_temp.getNominal_nego()+"");
        holder.sisNego.setText("Sisa nego: "+nego_temp.getSisa_nego()+"");
        String status_nego = nego_temp.getStatus_nego();
        holder.statusNego.setText("Status: "+status_nego+"");
        holder.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //beri notifikasi
                notify=true;
                final String msg="Menerima Nego Anda!";
                if(holder.x!=null){
                    if(notify){
                        sendNotification(holder.x.getFirebase_user_id(),usersekarang.getNama(),msg,holder.x);
                    }
                    notify=false;
                }
                ((DiNego)context).dinego(list_nego.get(position).getNego().getId_nego(),"terima");
                Toast.makeText(context, "Nego diterima", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //beri notifikasi
                notify=true;
                final String msg="Menolak Nego Anda!";
                if(holder.x!=null){
                    if(notify){
                        sendNotification(holder.x.getFirebase_user_id(),usersekarang.getNama(),msg,holder.x);
                    }
                    notify=false;
                }
                ((DiNego)context).dinego(list_nego.get(position).getNego().getId_nego(),"tolak");
                Toast.makeText(context, "Nego ditolak", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_nego.size();
    }

    public class DiNegoViewHolder extends RecyclerView.ViewHolder {
        TextView namaBarang;
        TextView hargaAwal;
        TextView hargaNego;
        TextView sisNego;
        TextView statusNego;
        TextView varianNego;
        ImageView gambarNego;
        user x=new user();
        Button btnTolak, btnTerima,btnNegoUlang;
        public DiNegoViewHolder(@NonNull View itemView) {
            super(itemView);
            namaBarang= itemView.findViewById(R.id.textViewLayoutNegoNama);
            hargaAwal= itemView.findViewById(R.id.textViewLayoutNegoHarga);
            hargaNego= itemView.findViewById(R.id.textViewLayoutNegoHargaBaru);
            sisNego= itemView.findViewById(R.id.textViewLayoutNegoSisaNego);
            statusNego= itemView.findViewById(R.id.textViewLayoutNegoStatusNego);
            varianNego= itemView.findViewById(R.id.textViewLayoutNegoVarian);
            btnTolak= itemView.findViewById(R.id.buttonLayoutNegoTolak);
            btnTerima= itemView.findViewById(R.id.buttonLayoutNegoTerima);
            btnNegoUlang= itemView.findViewById(R.id.buttonLayoutNegoUlang);
            gambarNego = itemView.findViewById(R.id.imageViewLayoutNego);
            btnTerima.setVisibility(View.VISIBLE);
            btnTolak.setVisibility(View.VISIBLE);
            btnNegoUlang.setVisibility(View.GONE);
        }
    }
    //send notifikasi
    public void sendNotification(String receiver,final String username,final String message,user x){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(usersekarang.getFirebase_user_id(), R.mipmap.logo_icon_app_round,username+"- "+message,"New message",x.getFirebase_user_id());

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
    //update token
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(usersekarang.getFirebase_user_id()).setValue(token1);
    }
    public void getusertujuan(String idpenjual,user x){
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(idpenjual)){
                        x.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                        x.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                        x.setAlamat(ds.child("alamat").getValue().toString());
                        x.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString()));
                        x.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                        x.setPassword(ds.child("password").getValue().toString());
                        x.setId(ds.child("id").getValue().toString());
                        x.setBirthdate(ds.child("birthdate").getValue().toString());
                        x.setEmail(ds.child("email").getValue().toString());
                        x.setPhone(ds.child("phone").getValue().toString());
                        x.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                        x.setNama(ds.child("nama").getValue().toString());
                        x.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getusernow(){
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        if(ds!=null){
                            usersekarang.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                            usersekarang.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                            usersekarang.setAlamat(ds.child("alamat").getValue().toString());
                            usersekarang.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString()));
                            usersekarang.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                            usersekarang.setPassword(ds.child("password").getValue().toString());
                            usersekarang.setId(ds.child("id").getValue().toString());
                            usersekarang.setBirthdate(ds.child("birthdate").getValue().toString());
                            usersekarang.setEmail(ds.child("email").getValue().toString());
                            usersekarang.setPhone(ds.child("phone").getValue().toString());
                            usersekarang.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                            usersekarang.setNama(ds.child("nama").getValue().toString());
                            usersekarang.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                            updateToken(FirebaseInstanceId.getInstance().getToken());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
