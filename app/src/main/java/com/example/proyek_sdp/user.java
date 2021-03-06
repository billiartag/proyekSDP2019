package com.example.proyek_sdp;

import java.io.Serializable;
import java.util.Comparator;

public class user implements Serializable{
    private String id,nama,password,email,birthdate,phone,alamat,firebase_user_id;
    private int saldo,profil_picture,status,verifikasi_ktp;
    private double rating;
    public user(){ }

    public user(String id, String nama, String password, String email, String birthdate, String phone, String alamat, String firebase_user_id, int saldo, int profil_picture, int status, int verifikasi_ktp, double rating) {
        this.id = id;
        this.nama = nama;
        this.password = password;
        this.email = email;
        this.birthdate = birthdate;
        this.phone = phone;
        this.alamat = alamat;
        this.firebase_user_id = firebase_user_id;
        this.saldo = saldo;
        this.profil_picture = profil_picture;
        this.status = status;
        this.verifikasi_ktp = verifikasi_ktp;
        this.rating = rating;
    }
    public static Comparator<user>sortdescrating=new Comparator<user>() {
        @Override
        public int compare(user user, user t1) {
            double rating1=user.getRating();
            double rating2=t1.getRating();
            return (int) (rating2-rating1);
        }
    };
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFirebase_user_id() {
        return firebase_user_id;
    }

    public void setFirebase_user_id(String firebase_user_id) {
        this.firebase_user_id = firebase_user_id;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getProfil_picture() {
        return profil_picture;
    }

    public void setProfil_picture(int profil_picture) {
        this.profil_picture = profil_picture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVerifikasi_ktp() {
        return verifikasi_ktp;
    }

    public void setVerifikasi_ktp(int verifikasi_ktp) {
        this.verifikasi_ktp = verifikasi_ktp;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
