package com.example.proyek_sdp;

import java.io.Serializable;
import java.util.Date;

public class barang implements Serializable {
    private String id,nama,jenis,deskripsi,idpenjual,waktu_upload,waktu_selesai,waktu_mulai,lokasi,varian,kategori;
    private int maksimal,harga;

    public barang() {
    }

    public barang(String id, String nama, String jenis, String deskripsi, String idpenjual, String waktu_upload, String waktu_selesai, String waktu_mulai, String lokasi, String varian, String kategori, int maksimal, int harga) {
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
    }

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

    @Override
    public String toString() {
        return nama+"\n"+"Deskripsi : "+deskripsi.substring(0,deskripsi.length()/2)+"..."+"\n";
    }
}
