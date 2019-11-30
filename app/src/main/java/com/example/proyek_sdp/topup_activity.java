package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class topup_activity extends AppCompatActivity {
    Spinner list_nominal;
    List<String> daftar_nominal= new ArrayList<String>();
    ArrayList<history_wallet> list_history_wallet= new ArrayList<history_wallet>();
    TextView tvsaldo_topup_activity;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_history_wallet;
    Button topup;
    RecyclerView rv_history_wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_activity);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
        list_nominal=findViewById(R.id.list_nominal);
        topup=findViewById(R.id.Top_up);
        tvsaldo_topup_activity=findViewById(R.id.tvsaldo_topup_activity);
        rv_history_wallet=findViewById(R.id.rv_history_wallet);

        //start program
        databaseReference_history_wallet= FirebaseDatabase.getInstance().getReference().child("HistoryTopUpDatabase");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count=dataSnapshot.getChildrenCount();
                        boolean berhasil_register=true;
                        for (DataSnapshot ds :dataSnapshot.getChildren()) {
                            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                                user baru=new user();
                                baru.setNama(ds.child("nama").getValue().toString());
                                baru.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                                int jumlah_topup=0;
                                if(list_nominal.getSelectedItemPosition()==0){
                                    jumlah_topup=50000;
                                }
                                else {
                                    jumlah_topup=50000*(list_nominal.getSelectedItemPosition()+1);
                                }

                                //update data saldo di database user
                                baru.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString())+jumlah_topup);
                                tvsaldo_topup_activity.setText("Rp "+(Integer.parseInt(ds.child("saldo").getValue().toString())+jumlah_topup));
                                baru.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                                baru.setPhone(ds.child("phone").getValue().toString());
                                baru.setEmail(ds.child("email").getValue().toString());
                                baru.setBirthdate(ds.child("birthdate").getValue().toString());
                                baru.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                                baru.setPassword(ds.child("password").getValue().toString());
                                baru.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                                databaseReference.child(ds.getKey()).setValue(baru);


                                //masukin ke history topup
                                String key=databaseReference_history_wallet.push().getKey();
                                history_wallet hist_baru=new history_wallet();
                                hist_baru.setId_hist_wallet(key);
                                hist_baru.setId_user_wallet(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                hist_baru.setNominal_berubah("+Rp"+jumlah_topup);
                                hist_baru.setStatus_history("Transfer Masuk");
                                Date dt = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                String time1 = sdf.format(dt);
                                hist_baru.setWaktu_history(time1);
                                databaseReference_history_wallet.child(key).setValue(hist_baru);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        databaseReference_history_wallet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {

                }
                rv_history_wallet.setHasFixedSize(true);
                rv_history_wallet.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                HistoryWalletAdapter adapterflashsale = new HistoryWalletAdapter(getApplicationContext(), list_history_wallet);
                rv_history_wallet.setAdapter(adapterflashsale);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        load_saldo_user();
        for (int i=1;i<=10;i++){
            daftar_nominal.add("Rp "+i*50000);
        }
        ArrayAdapter<String> nominal_adap=new ArrayAdapter<String>(getApplicationContext(),R.layout.custom_spinner,daftar_nominal);
        nominal_adap.setDropDownViewResource(R.layout.custom_spinner);
        list_nominal.setAdapter(nominal_adap);
    }
    public void load_history_wallet(){

    }
    public void load_saldo_user(){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                        tvsaldo_topup_activity.setText("Rp "+Integer.parseInt(ds.child("saldo").getValue().toString()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            Intent move=new Intent(getApplicationContext(), home.class);
            move.putExtra("topup","");
            startActivity(move);
            finish();
        }
        return true;
    }
}
