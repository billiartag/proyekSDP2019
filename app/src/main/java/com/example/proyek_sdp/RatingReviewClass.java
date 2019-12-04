package com.example.proyek_sdp;

public class RatingReviewClass {
    private String id,review,id_user,id_pemberi_review,waktu;
    private float rating;
    public RatingReviewClass(){}

    public RatingReviewClass(String id, String review, String id_user, String id_pemberi_review, String waktu, float rating) {
        this.id = id;
        this.review = review;
        this.id_user = id_user;
        this.id_pemberi_review = id_pemberi_review;
        this.waktu = waktu;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_pemberi_review() {
        return id_pemberi_review;
    }

    public void setId_pemberi_review(String id_pemberi_review) {
        this.id_pemberi_review = id_pemberi_review;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
