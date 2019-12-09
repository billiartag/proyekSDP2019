package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailPembayaran_PayPal extends AppCompatActivity {
    TextView tvid_pembayaran_paypal,tvstatus_pembayaran_paypal,tvjumlah_pembayaran_paypal;
    Button btnback_paypal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran__pay_pal);
        tvid_pembayaran_paypal=findViewById(R.id.tvid_pembayaran_paypal);
        tvstatus_pembayaran_paypal=findViewById(R.id.tvstatus_pembayaran_paypal);
        tvjumlah_pembayaran_paypal=findViewById(R.id.tvjumlah_pembayaran_paypal);
        btnback_paypal=findViewById(R.id.btnback_paypal);

        //get intent
        Intent move=getIntent();
        try {
            JSONObject jsonObject=new JSONObject(move.getStringExtra("DetailPembayaran"));
            showDetails(jsonObject.getJSONObject("response"),move.getStringExtra("JumlahPembayaran"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnback_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(),topup_activity.class);
                startActivity(move);
                finish();
            }
        });
    }

    private void showDetails(JSONObject response, String jumlahPembayaran) {
        try {
            tvid_pembayaran_paypal.setText(response.getString("id"));

            tvstatus_pembayaran_paypal.setText(response.getString("state"));
            if(response.getString("state").equals("approved")){
                Toast.makeText(this, "pembayaran sukses", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
                DatabaseReference databaseReference_history_wallet= FirebaseDatabase.getInstance().getReference().child("HistoryTopUpDatabase");
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
                                double dollar=14084.50704225352;
                                int jumlah_topup=(int)(Double.parseDouble(jumlahPembayaran)*dollar);

                                //update data saldo di database user
                                baru.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString())+jumlah_topup);
                                baru.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                                baru.setPhone(ds.child("phone").getValue().toString());
                                baru.setEmail(ds.child("email").getValue().toString());
                                baru.setId(ds.child("id").getValue().toString());
                                baru.setBirthdate(ds.child("birthdate").getValue().toString());
                                baru.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                                baru.setPassword(ds.child("password").getValue().toString());
                                baru.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                                baru.setAlamat(ds.child("alamat").getValue().toString());
                                baru.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
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
                                SimpleDateFormat sdf2 = new SimpleDateFormat("d/MM/yyyy");
                                String time2 = sdf2.format(dt);
                                hist_baru.setWaktu_history(time2+" - "+time1);
                                databaseReference_history_wallet.child(key).setValue(hist_baru);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            tvjumlah_pembayaran_paypal.setText("$"+jumlahPembayaran);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
