package com.example.proyek_sdp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private ArrayList<RatingReviewClass>list_review=new ArrayList<RatingReviewClass>();

    public ReviewAdapter(Context context, ArrayList<RatingReviewClass> list_review) {
        this.context = context;
        this.list_review = list_review;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_review_layout,parent,false);
        ReviewViewHolder holder=new ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        //isi foto pemberi review
        FirebaseStorage.getInstance().getReference().child("profil_picture").child(list_review.get(position).getId_pemberi_review()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.profil_picture_review_show);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.profil_picture_review_show.setBackgroundResource(R.drawable.default_profil);
            }
        });
        holder.ratingBar_show.setRating(list_review.get(position).getRating());
        holder.isireview_pembeli_show.setText("Review : \n"+list_review.get(position).getReview());
        holder.waktu_review_show.setText("Waktu : "+list_review.get(position).getWaktu());
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue().toString().equals(list_review.get(position).getId_pemberi_review())){
                        holder.tv_pemberi_review_show.setText("Oleh : "+ds.child("nama").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_review.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profil_picture_review_show;
        RatingBar ratingBar_show;
        TextView tv_pemberi_review_show,waktu_review_show,isireview_pembeli_show;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_picture_review_show=itemView.findViewById(R.id.profil_picture_review_show);
            ratingBar_show=itemView.findViewById(R.id.ratingBar_show);
            tv_pemberi_review_show=itemView.findViewById(R.id.tv_pemberi_review_show);
            waktu_review_show=itemView.findViewById(R.id.waktu_review_show);
            isireview_pembeli_show=itemView.findViewById(R.id.isireview_pembeli_show);
        }
    }
}
