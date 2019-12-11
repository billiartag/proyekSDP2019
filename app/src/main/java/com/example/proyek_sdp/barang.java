package com.example.proyek_sdp;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class barang implements Serializable {
    private String id,nama,jenis,deskripsi,idpenjual,waktu_upload,waktu_selesai,waktu_mulai,lokasi,varian,kategori;
    private int maksimal,harga,berat,status;

    public barang() {
    }

    public barang(String id, String nama, String jenis, String deskripsi, String idpenjual, String waktu_upload, String waktu_selesai, String waktu_mulai, String lokasi, String varian, String kategori, int maksimal, int harga, int berat, int status) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.deskripsi = deskripsi;
        this.idpenjual = idpenjual;
        this.waktu_upload = waktu_upload;
        this.waktu_selesai = waktu_selesai;
        this.waktu_mulai = waktu_mulai;
        this.lokasi = lokasi;
        this.varian = varian;
        this.kategori = kategori;
        this.maksimal = maksimal;
        this.harga = harga;
        this.berat = berat;
        this.status = status;
    }

    public static Comparator<barang> sortdescwaktu=new Comparator<barang>() {
        @Override
        public int compare(barang b1, barang b2) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/m/y");
            try {
                Date dateb1 = simpleDateFormat.parse(b1.getWaktu_upload());
                Date dateb2 = simpleDateFormat.parse(b2.getWaktu_upload());
                long temp1 = dateb1.getTime();
                long temp2 = dateb2.getTime();
                return (int) (temp2-temp1);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
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

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getIdpenjual() {
        return idpenjual;
    }

    public void setIdpenjual(String idpenjual) {
        this.idpenjual = idpenjual;
    }

    public String getWaktu_upload() {
        return waktu_upload;
    }

    public void setWaktu_upload(String waktu_upload) {
        this.waktu_upload = waktu_upload;
    }

    public String getWaktu_selesai() {
        return waktu_selesai;
    }

    public void setWaktu_selesai(String waktu_selesai) {
        this.waktu_selesai = waktu_selesai;
    }

    public String getWaktu_mulai() {
        return waktu_mulai;
    }

    public void setWaktu_mulai(String waktu_mulai) {
        this.waktu_mulai = waktu_mulai;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getVarian() {
        return varian;
    }

    public void setVarian(String varian) {
        this.varian = varian;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getMaksimal() {
        return maksimal;
    }

    public void setMaksimal(int maksimal) {
        this.maksimal = maksimal;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        if(nama.length()>11){
            return nama.substring(0,11)+"...";
        }
        else {
            return nama;
        }
    }
}
