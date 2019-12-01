package com.example.proyek_sdp;

public class report_class {
    private String id_report,user_pelapor,user_dilapor,masalah_report,keterangan_report,waktu_pelaporan;
    public  report_class(){}

    public report_class(String id_report, String user_pelapor, String user_dilapor, String masalah_report, String keterangan_report, String waktu_pelaporan) {

        this.id_report = id_report;
        this.user_pelapor = user_pelapor;
        this.user_dilapor = user_dilapor;
        this.masalah_report = masalah_report;
        this.keterangan_report = keterangan_report;
        this.waktu_pelaporan = waktu_pelaporan;
    }
    public String getId_report() {
        return id_report;
    }

    public void setId_report(String id_report) {
        this.id_report = id_report;
    }

    public String getUser_pelapor() {
        return user_pelapor;
    }

    public void setUser_pelapor(String user_pelapor) {
        this.user_pelapor = user_pelapor;
    }

    public String getUser_dilapor() {
        return user_dilapor;
    }

    public void setUser_dilapor(String user_dilapor) {
        this.user_dilapor = user_dilapor;
    }

    public String getMasalah_report() {
        return masalah_report;
    }

    public void setMasalah_report(String masalah_report) {
        this.masalah_report = masalah_report;
    }

    public String getKeterangan_report() {
        return keterangan_report;
    }

    public void setKeterangan_report(String keterangan_report) {
        this.keterangan_report = keterangan_report;
    }

    public String getWaktu_pelaporan() {
        return waktu_pelaporan;
    }

    public void setWaktu_pelaporan(String waktu_pelaporan) {
        this.waktu_pelaporan = waktu_pelaporan;
    }
}
