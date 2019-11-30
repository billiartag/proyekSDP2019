package com.example.proyek_sdp;

public class history_wallet {
    private String id_hist_wallet,id_user_wallet,status_history,waktu_history,nominal_berubah;
    public history_wallet(){}

    public history_wallet(String id_hist_wallet, String id_user_wallet, String status_history, String waktu_history, String nominal_berubah) {
        this.id_hist_wallet = id_hist_wallet;
        this.id_user_wallet = id_user_wallet;
        this.status_history = status_history;
        this.waktu_history = waktu_history;
        this.nominal_berubah = nominal_berubah;
    }

    public String getId_hist_wallet() {
        return id_hist_wallet;
    }

    public void setId_hist_wallet(String id_hist_wallet) {
        this.id_hist_wallet = id_hist_wallet;
    }

    public String getId_user_wallet() {
        return id_user_wallet;
    }

    public void setId_user_wallet(String id_user_wallet) {
        this.id_user_wallet = id_user_wallet;
    }

    public String getStatus_history() {
        return status_history;
    }

    public void setStatus_history(String status_history) {
        this.status_history = status_history;
    }

    public String getWaktu_history() {
        return waktu_history;
    }

    public void setWaktu_history(String waktu_history) {
        this.waktu_history = waktu_history;
    }

    public String getNominal_berubah() {
        return nominal_berubah;
    }

    public void setNominal_berubah(String nominal_berubah) {
        this.nominal_berubah = nominal_berubah;
    }
}
