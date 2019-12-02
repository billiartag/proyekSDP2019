package com.example.proyek_sdp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<CartClass> list_barang;

    public CartAdapter(Context context, ArrayList<CartClass> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cart_layout,parent,false);
        CartViewHolder holder=new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        //get objek
        CartClass cartSekarang = list_barang.get(position);
        //isi ke view
    //gambar barang
        //holder.img_barang_cart.setBackgroundResource(list_barang.get(position).getGambar());
        FirebaseStorage.getInstance().getReference().child("img_barang").child(cartSekarang.getId_barang_cart()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.img_barang_cart);
            }
        });
    //jumlah barang
        holder.total_barang.setText(cartSekarang.getJumlah_barang()+"");
    //nama barang
        holder.nama_barang.setText(cartSekarang.getNama_barang_cart());
    //harga barang
        holder.harga_barang.setText("Rp "+cartSekarang.getHarga_barang());
    //varian barang
        holder.varian_barang.setText("Varian: "+cartSekarang.getVarian_barang());
        //wishlist
        holder.sudahdifavorite = false;
        DatabaseReference databaseReference_wishlist= FirebaseDatabase.getInstance().getReference().child("WishListDatabase");
        databaseReference_wishlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_barang").getValue().toString().equals(cartSekarang.getId_barang_cart()) && ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        holder.wishlist.setImageResource(R.drawable.ic_hatimerah_black_24dp);
                        holder.sudahdifavorite = true;
                    }
                }
                if(holder.sudahdifavorite==false){
                    holder.wishlist.setImageResource(R.drawable.ic_favorite_cart_24dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    //barang wishlist
        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.sudahdifavorite){
                    databaseReference_wishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count=dataSnapshot.getChildrenCount();
                            for (DataSnapshot ds :dataSnapshot.getChildren()) {
                                if(ds.child("id_barang").getValue().toString().equals(cartSekarang.getId_barang_cart()) && ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                    databaseReference_wishlist.child(ds.getKey()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            holder.wishlist.setImageResource(R.drawable.ic_favorite_cart_24dp);
                                            holder.sudahdifavorite = false;
                                            Toast.makeText(context, "Berhasil Dihapus dari WishList!", Toast.LENGTH_SHORT).show();
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
                else {
                    String Key = databaseReference_wishlist.push().getKey();
                    WishList_class wish_baru=new WishList_class();
                    wish_baru.setId_user(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    wish_baru.setId_wishlist(Key);
                    wish_baru.setId_barang(cartSekarang.getId_barang_cart());
                    databaseReference_wishlist.child(Key).setValue(wish_baru).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            holder.wishlist.setImageResource(R.drawable.ic_hatimerah_black_24dp);
                            holder.sudahdifavorite = true;
                            Toast.makeText(context, "Berhasil Di Tambah Ke WishList!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                notifyDataSetChanged();
            }
        });
    //barang del cart
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((cart)context).deleteEntryCart(cartSekarang);
                Toast.makeText(context, "barang telah dihapus dari cart", Toast.LENGTH_SHORT).show();
            }
        });
    //barang minus cart
        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.total_barang.getText().toString())>1){
                    CartClass cartTemp = cartSekarang;
                    cartTemp.setJumlah_barang(cartTemp.getJumlah_barang()-1);
                    ((cart)context).updateEntryCart(cartTemp);
                    holder.total_barang.setText(cartTemp.getJumlah_barang()+"");
                }
                else{
                    //nilai kosong then hapus
                    ((cart)context).deleteEntryCart(cartSekarang);
                    Toast.makeText(context, "barang telah dihapus dari cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    //barang plus cart
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartSekarang.getJumlah_barang()+1<=cartSekarang.getJumlah_maks_barang()){
                    CartClass cartTemp = cartSekarang;
                    cartTemp.setJumlah_barang(cartTemp.getJumlah_barang()+1);
                    ((cart)context).updateEntryCart(cartTemp);
                    holder.total_barang.setText(cartTemp.getJumlah_barang()+"");}
                else{
                    Toast.makeText(context, "Maaf, titipan barang sudah melebihi batas titipan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_barang.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container_barang_cart;
        ImageView img_barang_cart;
        TextView nama_barang,harga_barang,varian_barang;
        ImageButton wishlist,delete,min,plus;
        EditText total_barang;
        boolean sudahdifavorite;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img_barang_cart=itemView.findViewById(R.id.img_barang_cart);
            nama_barang=itemView.findViewById(R.id.judul_barang_cart);
            harga_barang=itemView.findViewById(R.id.harga_barang_cart);
            wishlist=itemView.findViewById(R.id.btn_wishlist_cart);
            delete=itemView.findViewById(R.id.btn_delete_cart);
            min=itemView.findViewById(R.id.btn_min_cart);
            plus=itemView.findViewById(R.id.btn_plus_cart);
            total_barang=itemView.findViewById(R.id.jumlah_barang_cart);
            container_barang_cart=itemView.findViewById(R.id.container_barang_cart);
            varian_barang = itemView.findViewById(R.id.varian_barang_cart);
        }
    }
}
