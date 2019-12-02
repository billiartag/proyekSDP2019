package com.example.proyek_sdp;

public class Nego {
    String id_nego;
    String status_nego;
    String id_trans_nego;
    int nominal_nego;
    int sisa_nego;
    String waktu_nego;
    String id_seller;
    String id_user_nego;
    String varian;
    String id_barang_nego;

    public Nego(){}
    public Nego(String id_nego, String status_nego, String id_trans_nego, int nominal_nego, int sisa_nego, String waktu_nego) {
        this.id_nego = id_nego;
        this.status_nego = status_nego;
        this.id_trans_nego = id_trans_nego;
        this.nominal_nego = nominal_nego;
        this.sisa_nego = sisa_nego;
        this.waktu_nego = waktu_nego;
    }

    public String getId_nego() {return id_nego;}
    public void setId_nego(String id_nego) {this.id_nego = id_nego;}
    public String getStatus_nego() {return status_nego;}
    public void setStatus_nego(String status_nego) {this.status_nego = status_nego;}
    public String getId_trans_nego() {return id_trans_nego;}
    public void setId_trans_nego(String id_trans_nego) {this.id_trans_nego = id_trans_nego;}
    public int getNominal_nego() {return nominal_nego;}
    public void setNominal_nego(int nominal_nego) {this.nominal_nego = nominal_nego;}
    public int getSisa_nego() {return sisa_nego;}
    public void setSisa_nego(int sisa_nego) {this.sisa_nego = sisa_nego;}
    public String getWaktu_nego() {return waktu_nego;}
    public void setWaktu_nego(String waktu_nego) {this.waktu_nego = waktu_nego;}
    public String getId_seller() {return id_seller;}
    public void setId_seller(String id_seller) {this.id_seller = id_seller;}
    public String getId_user_nego() {return id_user_nego;}
    public void setId_user_nego(String id_user_nego) {this.id_user_nego = id_user_nego;}
    public String getVarian() {return varian;}
    public void setVarian(String varian) {this.varian = varian;}
    public String getId_barang_nego() {return id_barang_nego;}
    public void setId_barang_nego(String id_barang_nego) {this.id_barang_nego = id_barang_nego;}
}

