package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class report extends AppCompatActivity {
    Button submit_report;
    ImageView img_profil_report;
    TextView nama_profil_report;
    EditText masalah_report;
    EditText penjelasan_report;
    DatabaseReference databaseReference_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
        submit_report=findViewById(R.id.submit_report);
        img_profil_report=findViewById(R.id.img_profil_report);
        nama_profil_report=findViewById(R.id.nama_profil_report);
        masalah_report=findViewById(R.id.masalah_report);
        penjelasan_report=findViewById(R.id.penjelasan_report);
        //startprogram
        user x= (user) getIntent().getExtras().getSerializable("user");
        nama_profil_report.setText("Nama : "+x.getNama());
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(x.getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(img_profil_report);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                img_profil_report.setBackgroundResource(R.drawable.default_profil);
            }
        });
        submit_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean berhasil=true;
                if(masalah_report.getText().toString().trim().equals("")){
                    masalah_report.setError("Masalah Harus Diisi!");
                    berhasil=false;
                }
                if(penjelasan_report.getText().toString().trim().equals("")){
                    penjelasan_report.setError("Penjelasan Harus Diisi!");
                    berhasil=false;
                }
                if(berhasil){
                    databaseReference_report= FirebaseDatabase.getInstance().getReference().child("ReportDatabase");
                    String Key = databaseReference_report.push().getKey();

                    Date dt = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String time1 = sdf.format(dt);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("d/MM/yyyy");
                    String time2 = sdf2.format(dt);

                    report_class report_user=new report_class();
                    report_user.setId_report(Key);
                    report_user.setUser_pelapor(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    report_user.setUser_dilapor(x.getEmail());
                    report_user.setMasalah_report(masalah_report.getText().toString());
                    report_user.setKeterangan_report(penjelasan_report.getText().toString());
                    report_user.setWaktu_pelaporan(time2+" "+time1);

                    databaseReference_report.child(Key).setValue(report_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Report telah dikirim", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
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
            finish();
        }
        return true;
    }
}
