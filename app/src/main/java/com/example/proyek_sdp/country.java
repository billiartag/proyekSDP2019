package com.example.proyek_sdp;

public class country {
    private String iso;
    private String code;
    private String name;

    country(String iso, String code, String name) {
        this.iso = iso;
        this.code = code;
        this.name = name;
    }

    public String toString() {
        return iso + " - " + code + " - " + name.toUpperCase();
    }
}
