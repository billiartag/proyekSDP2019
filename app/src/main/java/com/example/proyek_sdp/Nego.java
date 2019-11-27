package com.example.proyek_sdp;

public class Nego {
    String id_nego;
    String status_nego;
    String id_trans_nego;
    int nominal_nego;
    int sisa_nego;
    String waktu_nego;//atau buat date?

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
}

