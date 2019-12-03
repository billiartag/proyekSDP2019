package com.example.proyek_sdp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class AkuNego extends AppCompatActivity {
    RecyclerView rv_akunego;
    ArrayList<barang> list_barang;
    ArrayList<Nego> list_nego;
    ArrayList<barang_nego> list_join_nego;
    AkuNego ctx;

    DatabaseReference databaseReference_akunego;
    DatabaseReference databaseReference_barangnego;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aku_nego);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("AkuNego");
        rv_akunego = findViewById(R.id.rv_akunego);

        ctx=this;
        databaseReference_akunego= FirebaseDatabase.getInstance().getReference().child("NegoDatabase");
        databaseReference_barangnego= FirebaseDatabase.getInstance().getReference().child("BarangDatabase");

        //init list
        list_nego = new ArrayList<>();
        list_barang= new ArrayList<>();
        list_join_nego = new ArrayList<>();

        //ambil data nego
        databaseReference_akunego.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot row:dataSnapshot.getChildren()) {
                    String user_yang_nego = row.child("id_user_nego").getValue().toString();
                    //ambil semua yang user
                    if(user_yang_nego.equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
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

                        //cari barang yang memenuhi syarat
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
                                        temp_barang.setDeskripsi(ds.child("deskripsi").getValue().toString());
                                        temp_barang.setIdpenjual(ds.child("idpenjual").getValue().toString());
                                        temp_barang.setJenis(ds.child("jenis").getValue().toString());
                                        temp_barang.setKategori(ds.child("kategori").getValue().toString());
                                        temp_barang.setLokasi(ds.child("lokasi").getValue().toString());
                                        temp_barang.setVarian(ds.child("varian").getValue().toString());
                                        temp_barang.setWaktu_mulai(ds.child("waktu_mulai").getValue().toString());
                                        temp_barang.setWaktu_selesai(ds.child("waktu_selesai").getValue().toString());
                                        temp_barang.setWaktu_upload(ds.child("waktu_upload").getValue().toString());
                                        temp_barang.setHarga(Integer.parseInt(ds.child("harga").getValue().toString()));
                                        temp_barang.setMaksimal(Integer.parseInt(ds.child("maksimal").getValue().toString()));
                                        list_barang.add(temp_barang);
                                        barang_nego join_nego = new barang_nego(temp_barang,temp_nego);
                                        //masukkin ke list
                                        list_join_nego.add(join_nego);
                                        //masukin di rv
                                        rv_akunego.setHasFixedSize(true);
                                        rv_akunego.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                                        AkuNegoAdapter adapter = new AkuNegoAdapter(ctx,list_join_nego);
                                        rv_akunego.setAdapter(adapter);
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
    }
    public void akuBeli(barang_nego barang_dibeli){
        //trigger ke beli disini
        finish();
        Intent i = new Intent(AkuNego.this, payment_nego.class);
        i.putExtra("barang_nego", barang_dibeli);
        i.putExtra("brg",  barang_dibeli.getBarang());
        i.putExtra("nego", barang_dibeli.getNego());

        startActivity(i);
    }
    public void akuNego(barang_nego obj_pass){
        //buat intent nego
        finish();
        Intent i = new Intent(AkuNego.this,nego_user.class);
        i.putExtra("jenis_nego", "lama");
        i.putExtra("barang", obj_pass.getBarang());
        i.putExtra("nego", obj_pass.getNego());
        i.putExtra("idnego", obj_pass.getNego().getId_nego());
        i.putExtra("varian_nego", obj_pass.getNego().getVarian());//masukkin pilihan varian disini
        startActivity(i);
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