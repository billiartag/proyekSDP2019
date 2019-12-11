package com.example.proyek_sdp;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.proyek_sdp.Notifications.Client;
import com.example.proyek_sdp.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class DiNego extends AppCompatActivity {

    RecyclerView rv_dinego;
    ArrayList<barang> list_barang;
    ArrayList<Nego> list_nego;
    ArrayList<barang_nego> list_join_nego;
    DiNego ctx;
    DiNegoAdapter adapter;
    DatabaseReference databaseReference_dinego;
    DatabaseReference databaseReference_barangnego;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_di_nego);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("DiNego");
        ctx = this;
        rv_dinego = findViewById(R.id.rv_dinego);
        list_nego = new ArrayList<>();
        list_barang= new ArrayList<>();
        list_join_nego = new ArrayList<>();
        databaseReference_dinego= FirebaseDatabase.getInstance().getReference().child("NegoDatabase");
        databaseReference_barangnego= FirebaseDatabase.getInstance().getReference().child("BarangDatabase");
        getDataNego();
    }
    public void getDataNego(){

        databaseReference_dinego.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot row:dataSnapshot.getChildren()) {
                    String user_yang_nego = row.child("id_seller").getValue().toString();
                    String status_nego = row.child("status_nego").getValue().toString();
                    //cari yang sellernya sama dan nego masih pending
                    if(user_yang_nego.equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())&&status_nego.equalsIgnoreCase("pending")) {
                        Nego temp_nego = new Nego();
                        temp_nego.setId_barang_nego(row.child("id_barang_nego").getValue().toString());
                        temp_nego.setId_nego(row.child("id_nego").getValue().toString());
                        temp_nego.setId_seller(row.child("id_seller").getValue().toString());
                        temp_nego.setId_trans_nego(row.child("id_trans_nego").getValue().toString());
                        temp_nego.setId_user_nego(row.child("id_user_nego").getValue().toString());
                        temp_nego.setNominal_nego(Integer.parseInt(row.child("nominal_nego").getValue().toString()));
                        temp_nego.setSisa_nego(Integer.parseInt(row.child("sisa_nego").getValue().toString()));
                        temp_nego.setStatus_nego(row.child("status_nego").getValue().toString());
                        temp_nego.setVarian(row.child("varian").getValue().toString());
                        temp_nego.setWaktu_nego(row.child("waktu_nego").getValue().toString());
                        list_nego.add(temp_nego);

                        databaseReference_barangnego.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                    String id_barang_list = ds.child("id").getValue().toString();
                                    if(id_barang_list.equalsIgnoreCase(temp_nego.getId_barang_nego())){
                                        //barang yang sesuai
                                        barang temp_barang = new barang();
                                        temp_barang.setId(ds.child("id").getValue().toString());
                                        temp_barang.setNama(ds.child("nama").getValue().toString());
                                        temp_barang.setHarga(Integer.parseInt(ds.child("harga").getValue().toString()));
                                        temp_barang.setIdpenjual(ds.child("idpenjual").getValue().toString());
                                        list_barang.add(temp_barang);
//                                        Log.d("yang sama", temp_nego.getId_barang_nego()+"&&"+temp_barang.getNama());
                                        barang_nego join_nego = new barang_nego(temp_barang,temp_nego);
                                        //masukkin ke list
                                        list_join_nego.add(join_nego);
                                    }
                                }
                                //masukin di rv
                                rv_dinego.setHasFixedSize(true);
                                rv_dinego.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                                adapter = new DiNegoAdapter(ctx,list_join_nego);
                                rv_dinego.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
    }
    public void dinego(String id_nego, String kondisi_nego){
        databaseReference_dinego.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot row:dataSnapshot.getChildren()) {
                    if(row.child("id_nego").getValue().toString().equalsIgnoreCase(id_nego)){
                        databaseReference_dinego.child(row.child("id_nego").getValue().toString()).child("status_nego").setValue(kondisi_nego);
                        refreshData();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        if(kondisi_nego.equalsIgnoreCase("terima")){

            //kode ubah status nego jadi terima

        }
        else if(kondisi_nego.equalsIgnoreCase("tolak")){
            //kode ubah status nego jadi tolak
            databaseReference_dinego.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot row:dataSnapshot.getChildren()) {
//                        Log.d("sama",row.child("id_nego").getValue().toString()+"--"+(id_nego)+"="+row.child("id_nego").getValue().toString().equalsIgnoreCase(id_nego));
                        if(row.child("id_nego").getValue().toString().equalsIgnoreCase(id_nego)){
                            Toast.makeText(ctx, row.child("id_nego").getValue().toString()+"", Toast.LENGTH_SHORT).show();
                            databaseReference_dinego.child(row.child("id_nego").getValue().toString()).child("status_nego").setValue("tolak");
                            refreshData();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/
    }
    public void refreshData(){
        list_join_nego.clear();
        list_barang.clear();
        list_nego.clear();
        adapter.notifyDataSetChanged();
        getDataNego();
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
}
