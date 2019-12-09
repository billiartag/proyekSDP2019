package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Instrumentation;
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

import com.example.proyek_sdp.Config.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class topup_activity extends AppCompatActivity {
    //PAYPAL
    public static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config =new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);


    //DECLARE PROGRAM
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
        ab.setTitle("Top-Up");
        list_nominal=findViewById(R.id.list_nominal);
        topup=findViewById(R.id.Top_up);
        tvsaldo_topup_activity=findViewById(R.id.tvsaldo_topup_activity);
        rv_history_wallet=findViewById(R.id.rv_history_wallet);
        //start paypal service
        Intent move=new Intent(getApplicationContext(),PayPalService.class);
        move.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(move);
        //start program
        databaseReference_history_wallet= FirebaseDatabase.getInstance().getReference().child("HistoryTopUpDatabase");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process_payment();
            }
        });
        load_history_wallet();
        load_saldo_user();
        for (int i=1;i<=10;i++){
            daftar_nominal.add("Rp "+i*50000);
        }
        ArrayAdapter<String> nominal_adap=new ArrayAdapter<String>(getApplicationContext(),R.layout.custom_spinner,daftar_nominal);
        nominal_adap.setDropDownViewResource(R.layout.custom_spinner);
        list_nominal.setAdapter(nominal_adap);
    }
    public void load_history_wallet(){
        databaseReference_history_wallet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                list_history_wallet.clear();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_user_wallet").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        history_wallet history_baru=new history_wallet();
                        history_baru.setWaktu_history(ds.child("waktu_history").getValue().toString());
                        history_baru.setStatus_history(ds.child("status_history").getValue().toString());
                        history_baru.setId_user_wallet(ds.child("id_user_wallet").getValue().toString());
                        history_baru.setId_hist_wallet(ds.child("id_hist_wallet").getValue().toString());
                        history_baru.setNominal_berubah(ds.child("nominal_berubah").getValue().toString());
                        list_history_wallet.add(history_baru);
                    }
                }
                rv_history_wallet.setHasFixedSize(true);
                rv_history_wallet.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                HistoryWalletAdapter adapterhistorywallet = new HistoryWalletAdapter(getApplicationContext(), list_history_wallet);
                rv_history_wallet.setAdapter(adapterhistorywallet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //untuk bayar di paypal
    public void process_payment(){
        double dollar=14084.50704225352;
        String amount=(Double.parseDouble(list_nominal.getSelectedItem().toString().substring(3))/dollar)+"";
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(amount),"USD","TopUp E-Wallet TitipAku",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent move=new Intent(this, PaymentActivity.class);
        move.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        move.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(move,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        double dollar=14084.50704225352;
                        String amount=(Double.parseDouble(list_nominal.getSelectedItem().toString().substring(3))/dollar)+"";
                        String payment_detail = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, DetailPembayaran_PayPal.class).putExtra("DetailPembayaran",payment_detail).putExtra("JumlahPembayaran",amount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(resultCode== Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }
        else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "invalid", Toast.LENGTH_SHORT).show();
        }
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
