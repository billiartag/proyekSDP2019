package com.example.proyek_sdp;

import java.util.ArrayList;

public class chat {
    private String id,pengirim_chat,penerima_chat,isi_chat,waktu_kirim_chat;

    public chat() { }

    public chat(String id, String pengirim_chat, String penerima_chat, String isi_chat, String waktu_kirim_chat) {
        this.id = id;
        this.pengirim_chat = pengirim_chat;
        this.penerima_chat = penerima_chat;
        this.isi_chat = isi_chat;
        this.waktu_kirim_chat = waktu_kirim_chat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPengirim_chat() {
        return pengirim_chat;
    }

    public void setPengirim_chat(String pengirim_chat) {
        this.pengirim_chat = pengirim_chat;
    }

    public String getPenerima_chat() {
        return penerima_chat;
    }

    public void setPenerima_chat(String penerima_chat) {
        this.penerima_chat = penerima_chat;
    }

    public String getIsi_chat() {
        return isi_chat;
    }

    public void setIsi_chat(String isi_chat) {
        this.isi_chat = isi_chat;
    }

    public String getWaktu_kirim_chat() {
        return waktu_kirim_chat;
    }

    public void setWaktu_kirim_chat(String waktu_kirim_chat) {
        this.waktu_kirim_chat = waktu_kirim_chat;
    }
}

