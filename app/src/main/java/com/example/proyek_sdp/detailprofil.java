package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class detailprofil extends AppCompatActivity {
    ImageView profil_pict;
    TextView nama;
    TextView rating,follow_detail_profil,tv_text_reviewdaripembeli;
    Button chat;
    ImageButton report;
    boolean difollow=false;
    RecyclerView rv_ratingdanreview;
    DatabaseReference databaseReference_follow;
    DatabaseReference databaseReference_review;
    ArrayList<RatingReviewClass>list_review=new ArrayList<RatingReviewClass>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailprofil);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
        chat=findViewById(R.id.msg);
        nama=findViewById(R.id.username);
        profil_pict=findViewById(R.id.profil_pict);
        rating=findViewById(R.id.rating);
        follow_detail_profil=findViewById(R.id.follow_detail_profil);
        rv_ratingdanreview=findViewById(R.id.rv_ratingdanreview);
        tv_text_reviewdaripembeli=findViewById(R.id.tv_text_reviewdaripembeli);
        //start program
        user x= (user) getIntent().getExtras().getSerializable("user");
        //pengisian recycler view rating dan review
        rating.setText("Rating : "+x.getRating());
        //isi data data didalam layout
        nama.setText("Nama : "+x.getNama());
        //ambil foto profil user
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(x.getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(profil_pict);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profil_pict.setBackgroundResource(R.drawable.default_profil);
            }
        });
        //tekan tombol send message
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(),chat_detail.class);
                Bundle b = new Bundle();
                b.putSerializable("user", x);
                move.putExtras(b);
                startActivity(move);
            }
        });
        //follow
        databaseReference_follow= FirebaseDatabase.getInstance().getReference().child("FollowDatabase");
        databaseReference_follow.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("following").getValue().toString().equals(x.getEmail()) && ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        follow_detail_profil.setText("Unfollow");
                        follow_detail_profil.setTextColor(Color.parseColor("#E53935"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        follow_detail_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(follow_detail_profil.getText().toString().equals("Follow")){
                    String Key = databaseReference_follow.push().getKey();
                    follow_class follow_user=new follow_class();
                    follow_user.setId_user(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    follow_user.setId_follow(Key);
                    follow_user.setFollowing(x.getEmail());
                    databaseReference_follow.child(Key).setValue(follow_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            follow_detail_profil.setText("Unfollow");
                            follow_detail_profil.setTextColor(Color.parseColor("#E53935"));
                            Toast.makeText(detailprofil.this, "Follow Berhasil!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    databaseReference_follow.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count=dataSnapshot.getChildrenCount();
                            for (DataSnapshot ds :dataSnapshot.getChildren()) {
                                if(ds.child("following").getValue().toString().equals(x.getEmail()) && ds.child("id_user").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                    databaseReference_follow.child(ds.getKey()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            follow_detail_profil.setText("Follow");
                                            follow_detail_profil.setTextColor(Color.parseColor("#00ACC1"));
                                            Toast.makeText(detailprofil.this, "Unfollow Berhasil!", Toast.LENGTH_SHORT).show();
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
            }
        });
        //masukkan isi review

        databaseReference_review= FirebaseDatabase.getInstance().getReference().child("RatingAndReviewDatabase");
        databaseReference_review.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_user").getValue().toString().equals(x.getEmail())){
                        RatingReviewClass review_user=new RatingReviewClass();
                        review_user.setId_pemberi_review(ds.child("id_pemberi_review").getValue().toString());
                        review_user.setId(ds.child("id").getValue().toString());
                        review_user.setId_user(ds.child("id_user").getValue().toString());
                        review_user.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                        review_user.setReview(ds.child("review").getValue().toString());
                        review_user.setWaktu(ds.child("waktu").getValue().toString());
                        list_review.add(review_user);
                    }
                }
                rv_ratingdanreview.setHasFixedSize(true);
                rv_ratingdanreview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                ReviewAdapter adapter = new ReviewAdapter(getApplicationContext(), list_review);
                rv_ratingdanreview.setAdapter(adapter);
                if(list_review.size()==0){
                    tv_text_reviewdaripembeli.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_detailprofil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            Intent move=new Intent(getApplicationContext(), home.class);
            move.putExtra("profil","");
            startActivity(move);
            finish();
        }
        else{
            Intent move=new Intent(getApplicationContext(),report.class);
            Bundle b = new Bundle();
            b.putSerializable("user", getIntent().getExtras().getSerializable("user"));
            move.putExtras(b);
            startActivity(move);
        }
        return true;
    }
}
