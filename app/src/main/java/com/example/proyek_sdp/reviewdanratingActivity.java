package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class reviewdanratingActivity extends AppCompatActivity {
    EditText edratingdanreview;
    Button btnsubmit_ratingdanreview;
    String iduser;
    RatingBar ratingBar;
    ImageView gambaruser_reviewdanrating;
    TextView tvjastip_ratingdanreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewdanrating);
        edratingdanreview=findViewById(R.id.edratingdanreview);
        btnsubmit_ratingdanreview=findViewById(R.id.btnsubmit_ratingdanreview);
        gambaruser_reviewdanrating=findViewById(R.id.gambar_user_reviewdanrating);
        tvjastip_ratingdanreview=findViewById(R.id.tvjastip_ratingdanreview);

        //start program
        DatabaseReference databaseReference_rating= FirebaseDatabase.getInstance().getReference().child("RatingAndReviewDatabase");
        DatabaseReference databaseReference_user= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        iduser=getIntent().getExtras().getString("iduser");
        //cari foto nya user
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(iduser).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (getApplicationContext()!=null){
                    Glide.with(getApplicationContext()).load(uri).into(gambaruser_reviewdanrating);
                }
            }
        });
        //cari nama user untuk di cetak
        databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (iduser.equals(ds.child("email").getValue().toString())){
                        tvjastip_ratingdanreview.setText("Nama Jasa Titip : "+ds.child("nama").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //function agar dapat memilih rating dan sekaligus mengubah gambarnya
        ratingBar=findViewById(R.id.ratingBar);
        btnsubmit_ratingdanreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count=dataSnapshot.getChildrenCount();
                        for (DataSnapshot ds :dataSnapshot.getChildren()) {
                            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                                String Key = databaseReference_rating.push().getKey();
                                RatingReviewClass reviewbaru=new RatingReviewClass();
                                reviewbaru.setId(Key);
                                reviewbaru.setId_pemberi_review(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                reviewbaru.setId_user(iduser);
                                reviewbaru.setRating(ratingBar.getRating());
                                reviewbaru.setReview(edratingdanreview.getText().toString());
                                Date dt = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                String time1 = sdf.format(dt);
                                SimpleDateFormat sdf2 = new SimpleDateFormat("d/MM/yyyy");
                                String time2 = sdf2.format(dt);
                                reviewbaru.setWaktu(time2+" - "+time1);
                                databaseReference_rating.child(Key).setValue(reviewbaru);
                                Intent move=new Intent(getApplicationContext(),home.class);
                                startActivity(move);
                                finish();
                                Toast.makeText(reviewdanratingActivity.this, "Terima Kasih Telah Memberi Review dan Rating :)", Toast.LENGTH_SHORT).show();
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
