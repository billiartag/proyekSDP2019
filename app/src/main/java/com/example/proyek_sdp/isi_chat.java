package com.example.proyek_sdp;

public class isi_chat {
    public String nama;
    public String isi;

    public isi_chat(String nama, String isi) {
        this.nama = nama;
        this.isi = isi;
    }

    @Override
    public String toString() {
        return ""+nama+":"+isi;
    }
}
