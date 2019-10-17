package com.example.proyek_sdp;

import java.io.Serializable;

public class user implements Serializable {
    private String nama;
    private int rating;
    private int profil_picture;

    public user(String nama, int rating, int profil_picture) {
        this.nama = nama;
        this.rating = rating;
        this.profil_picture = profil_picture;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getProfil_picture() {
        return profil_picture;
    }

    public void setProfil_picture(int profil_picture) {
        this.profil_picture = profil_picture;
    }
}
