package com.example.proyek_sdp;

import java.io.Serializable;

public class user{
    private String nama,password,username,email,phone,birthdate,tipe_user;
    private int rating;
    private int profil_picture;

    public user() {}

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getTipe_user() {
        return tipe_user;
    }

    public void setTipe_user(String tipe_user) {
        this.tipe_user = tipe_user;
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

    public user(String nama, String password, String username, String email, String phone, String birthdate, String tipe_user, int rating, int profil_picture) {
        this.nama = nama;
        this.password = password;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.tipe_user = tipe_user;
        this.rating = rating;
        this.profil_picture = profil_picture;
    }
}
