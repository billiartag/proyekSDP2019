package com.example.proyek_sdp;

import android.os.Parcel;
import android.os.Parcelable;

public class barang_nego implements Parcelable {
    private barang barang;
    private Nego nego;

    public barang_nego(){}
    public barang_nego(com.example.proyek_sdp.barang barang, Nego nego) {
        this.barang = barang;
        this.nego = nego;
    }

    protected barang_nego(Parcel in) {
    }

    public static final Creator<barang_nego> CREATOR = new Creator<barang_nego>() {
        @Override
        public barang_nego createFromParcel(Parcel in) {
            return new barang_nego(in);
        }

        @Override
        public barang_nego[] newArray(int size) {
            return new barang_nego[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
