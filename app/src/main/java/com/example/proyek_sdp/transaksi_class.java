package com.example.proyek_sdp;

public class transaksi_class {
    private String id_transaksi,id_user_trans,id_seller_trans,waktu_trans,keterangan_trans,status_trans,id_barang_trans,varian_pilihan,total_trans,alamat_pengiriman_trans,id_promo;
    private int jumlah_barang_trans;
    public transaksi_class(){}
    public transaksi_class(String id_transaksi, String id_user_trans, String id_seller_trans, String waktu_trans, String keterangan_trans, String status_trans, String id_barang_trans, String varian_pilihan, String total_trans, String alamat_pengiriman_trans, String id_promo, int jumlah_barang_trans) {
        this.id_transaksi = id_transaksi;
        this.id_user_trans = id_user_trans;
        this.id_seller_trans = id_seller_trans;
        this.waktu_trans = waktu_trans;
        this.keterangan_trans = keterangan_trans;
        this.status_trans = status_trans;
        this.id_barang_trans = id_barang_trans;
        this.varian_pilihan = varian_pilihan;
        this.total_trans = total_trans;
        this.alamat_pengiriman_trans = alamat_pengiriman_trans;
        this.id_promo = id_promo;
        this.jumlah_barang_trans = jumlah_barang_trans;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getId_user_trans() {
        return id_user_trans;
    }

    public void setId_user_trans(String id_user_trans) {
        this.id_user_trans = id_user_trans;
    }

    public String getId_seller_trans() {
        return id_seller_trans;
    }

    public void setId_seller_trans(String id_seller_trans) {
        this.id_seller_trans = id_seller_trans;
    }

    public String getWaktu_trans() {
        return waktu_trans;
    }

    public void setWaktu_trans(String waktu_trans) {
        this.waktu_trans = waktu_trans;
    }

    public String getKeterangan_trans() {
        return keterangan_trans;
    }

    public void setKeterangan_trans(String keterangan_trans) {
        this.keterangan_trans = keterangan_trans;
    }

    public String getStatus_trans() {
        return status_trans;
    }

    public void setStatus_trans(String status_trans) {
        this.status_trans = status_trans;
    }

    public String getId_barang_trans() {
        return id_barang_trans;
    }

    public void setId_barang_trans(String id_barang_trans) {
        this.id_barang_trans = id_barang_trans;
    }

    public String getVarian_pilihan() {
        return varian_pilihan;
    }

    public void setVarian_pilihan(String varian_pilihan) {
        this.varian_pilihan = varian_pilihan;
    }

    public String getTotal_trans() {
        return total_trans;
    }

    public void setTotal_trans(String total_trans) {
        this.total_trans = total_trans;
    }

    public String getAlamat_pengiriman_trans() {
        return alamat_pengiriman_trans;
    }

    public void setAlamat_pengiriman_trans(String alamat_pengiriman_trans) {
        this.alamat_pengiriman_trans = alamat_pengiriman_trans;
    }

    public String getId_promo() {
        return id_promo;
    }

    public void setId_promo(String id_promo) {
        this.id_promo = id_promo;
    }

    public int getJumlah_barang_trans() {
        return jumlah_barang_trans;
    }

    public void setJumlah_barang_trans(int jumlah_barang_trans) {
        this.jumlah_barang_trans = jumlah_barang_trans;
    }
}
