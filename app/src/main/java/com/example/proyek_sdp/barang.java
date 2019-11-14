package com.example.proyek_sdp;

import java.io.Serializable;
import java.util.Date;

public class barang implements Serializable {
    private String nama;
    private int harga;
    private String deskripsi;
    private String durasi;
    private String tipe;
    private int max_barang;
    private int gambar;
    private String pemilik;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getMax_barang() {
        return max_barang;
    }

    public void setMax_barang(int max_barang) {
        this.max_barang = max_barang;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }


    public barang(String nama, int harga, String deskripsi, String durasi, String tipe, int max_barang, int gambar, String pemilik) {

        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.durasi = durasi;
        this.tipe = tipe;
        this.max_barang = max_barang;
        this.gambar = gambar;
        this.pemilik = pemilik;
    }

    @Override
    public String toString() {
        return nama+"\n"+"Deskripsi : "+deskripsi.substring(0,deskripsi.length()/2)+"..."+"\n";
    }
}
