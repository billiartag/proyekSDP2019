package com.example.proyek_sdp;

import java.util.ArrayList;

public class chat {
    private String username;
    private String pengirim;
    private ArrayList<isi_chat>isi;

    public chat(String username, String pengirim) {
        this.username = username;
        this.pengirim = pengirim;
    }


    public String getUsername() {
        return username;
    }

    public String getPengirim() {
        return pengirim;
    }

    public ArrayList<isi_chat> getIsi() {
        return isi;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public void setIsi(ArrayList<isi_chat> isi) {
        this.isi = isi;
    }

    @Override
    public String toString() {
        return pengirim;
    }
}

