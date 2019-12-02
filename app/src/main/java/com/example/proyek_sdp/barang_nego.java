package com.example.proyek_sdp;

public class barang_nego {
    private barang barang;
    private Nego nego;

    public barang_nego(){}
    public barang_nego(com.example.proyek_sdp.barang barang, Nego nego) {
        this.barang = barang;
        this.nego = nego;
    }

    public com.example.proyek_sdp.barang getBarang() {
        return barang;
    }

    public void setBarang(com.example.proyek_sdp.barang barang) {
        this.barang = barang;
    }

    public Nego getNego() {
        return nego;
    }

    public void setNego(Nego nego) {
        this.nego = nego;
    }
}
