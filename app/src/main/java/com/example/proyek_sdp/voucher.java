package com.example.proyek_sdp;

public class voucher {
    private String deskripsi_promo,diskon_promo,mulai_promo,nama_promo,selesai_promo,status_promo;
    public voucher(){}
    public voucher(String deskripsi_promo, String diskon_promo, String mulai_promo, String nama_promo, String selesai_promo, String status_promo) {
        this.deskripsi_promo = deskripsi_promo;
        this.diskon_promo = diskon_promo;
        this.mulai_promo = mulai_promo;
        this.nama_promo = nama_promo;
        this.selesai_promo = selesai_promo;
        this.status_promo = status_promo;
    }

    public String getDeskripsi_promo() {
        return deskripsi_promo;
    }

    public void setDeskripsi_promo(String deskripsi_promo) {
        this.deskripsi_promo = deskripsi_promo;
    }

    public String getDiskon_promo() {
        return diskon_promo;
    }

    public void setDiskon_promo(String diskon_promo) {
        this.diskon_promo = diskon_promo;
    }

    public String getMulai_promo() {
        return mulai_promo;
    }

    public void setMulai_promo(String mulai_promo) {
        this.mulai_promo = mulai_promo;
    }

    public String getNama_promo() {
        return nama_promo;
    }

    public void setNama_promo(String nama_promo) {
        this.nama_promo = nama_promo;
    }

    public String getSelesai_promo() {
        return selesai_promo;
    }

    public void setSelesai_promo(String selesai_promo) {
        this.selesai_promo = selesai_promo;
    }

    public String getStatus_promo() {
        return status_promo;
    }

    public void setStatus_promo(String status_promo) {
        this.status_promo = status_promo;
    }
}
