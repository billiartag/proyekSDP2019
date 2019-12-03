package com.example.proyek_sdp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
public class history_penjual extends AppCompatActivity {

    ArrayList<transaksi_class>listtransaksi = new ArrayList<transaksi_class>();
    ListView lvHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoy_penjual_layout);
        lvHistory = findViewById(R.id.lvHistoryPenjualFront);


        //add barang dari list firebase
        DatabaseReference databaseReference_transaksi= FirebaseDatabase.getInstance().getReference().child("TransaksiDatabase");
        databaseReference_transaksi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("id_seller_trans").getValue().toString())){
                        transaksi_class trans=new transaksi_class();
                        trans.setId_transaksi(ds.child("id_transaksi").getValue().toString());
                        trans.setWaktu_trans(ds.child("waktu_trans").getValue().toString());
                        trans.setVarian_pilihan(ds.child("varian_pilihan").getValue().toString());
                        trans.setTotal_trans(ds.child("total_trans").getValue().toString());
                        trans.setStatus_trans(ds.child("status_trans").getValue().toString());
                        trans.setKeterangan_trans(ds.child("keterangan_trans").getValue().toString());
                        trans.setJumlah_barang_trans(Integer.parseInt(ds.child("jumlah_barang_trans").getValue().toString()));
                        trans.setId_user_trans(ds.child("id_user_trans").getValue().toString());
                        trans.setId_seller_trans(ds.child("id_seller_trans").getValue().toString());
                        trans.setId_promo(ds.child("id_promo").getValue().toString());
                        trans.setId_barang_trans(ds.child("id_barang_trans").getValue().toString());
                        trans.setAlamat_pengiriman_trans(ds.child("alamat_pengiriman_trans").getValue().toString());
                        listtransaksi.add(trans);
                    }
                }
                adapter adap = new adapter(getApplicationContext(),listtransaksi);
                lvHistory.setAdapter(adap);
                lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(view.getContext(),detail_history_penjual.class);
                        startActivity(intent);
                    }
                });
                if(listtransaksi.size()==0){
                    Toast.makeText(history_penjual.this, "Anda Tidak Memiliki History Penjualan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Dititipin");
    }
    public void cancelOrder(String orderID){
        Toast.makeText(this, orderID+"", Toast.LENGTH_SHORT).show();
        //kasi kode cancel ke firebase disini
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            finish();
        }
        return true;
    }

    class adapter extends ArrayAdapter<transaksi_class> {
        ArrayList<transaksi_class> brg = new ArrayList<transaksi_class>();
        Context ctx;
        public adapter(Context context, ArrayList<transaksi_class>a) {
            super(context, R.layout.list_history_front_pembeli_layout,a);
            this.brg = a;
            ctx=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_history_penjual_layout,parent,false);
            TextView tvNamaBarang = row.findViewById(R.id.tvNamaBarang);
            TextView tvStatus = row.findViewById(R.id.tvStatus);
            TextView tvId = row.findViewById(R.id.tvId);
            ImageView img = row.findViewById(R.id.ivImageBarang);
            TextView tv_pembeli = row.findViewById(R.id.textViewPembeli);
            Button btnCancel = row.findViewById(R.id.buttonPenjualCancelOrder);

            if(brg.get(position).getStatus_trans().equals("pending")){
                tvStatus.setText("Pending");
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setBackgroundColor(Color.GRAY);
            }
            else if(brg.get(position).getStatus_trans().equals("dikonfirmasi")){
                tvStatus.setText("Dikirim");
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setBackgroundColor(Color.YELLOW);
            }
            else if(brg.get(position).getStatus_trans().equals("selesai")){
                tvStatus.setText("Sudah Selesai");
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setBackgroundColor(Color.GREEN);
            }
            DatabaseReference databaseReference_barang= FirebaseDatabase.getInstance().getReference().child("BarangDatabase");
            databaseReference_barang.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count=dataSnapshot.getChildrenCount();
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if (brg.get(position).getId_barang_trans().equals(ds.child("id").getValue().toString())){
                            tvNamaBarang.setText(ds.child("nama").getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FirebaseStorage.getInstance().getReference().child("img_barang").child(brg.get(position).getId_barang_trans()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (getApplicationContext()!=null){
                        Glide.with(getApplicationContext()).load(uri).into(img);
                    }
                }
            });
            DatabaseReference databaseReference_user= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
            databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count=dataSnapshot.getChildrenCount();
                    for (DataSnapshot ds :dataSnapshot.getChildren()) {
                        if (brg.get(position).getId_seller_trans().equals(ds.child("email").getValue().toString())){
                            tv_pembeli.setText(ds.child("nama").getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            tvId.setText(brg.get(position).getId_barang_trans());
            tvId.setTextColor(Color.GREEN);
            tvId.setTypeface(null, Typeface.BOLD_ITALIC);
            tvNamaBarang.setTextColor(Color.BLACK);
            tvId.setTextColor(Color.BLACK);
            tv_pembeli.setTextColor(Color.BLACK);
            //kalau sudah selesai / sudah selesai dikirim
            if(!brg.get(position).getStatus_trans().equals("pending")){
                btnCancel.setVisibility(View.GONE);
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id_transaksi= brg.get(position).getId_transaksi();
//                    Toast.makeText(history_penjual.this, "ID barang yang diambil: "+id_barang, Toast.LENGTH_SHORT).show();
                    cancelOrder(id_transaksi);
                }
            });
            return row;

        }
    }

}
