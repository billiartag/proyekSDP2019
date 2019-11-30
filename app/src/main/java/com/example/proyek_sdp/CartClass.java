package com.example.proyek_sdp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CartClass implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cart")
    private int id_cart;
    @ColumnInfo(name = "id_barang_cart")
    private  String id_barang_cart;
    @ColumnInfo(name = "nama_barang_cart")
    private  String nama_barang_cart;
    @ColumnInfo(name = "waktu_selesai_cart")
    private  String waktu_selesai_cart;
    @ColumnInfo(name = "harga_barang")
    private int harga_barang;
    @ColumnInfo(name = "jumlah_barang")
    private int jumlah_barang;
    @ColumnInfo(name = "jumlah_maks_barang")
    private int jumlah_maks_barang;

    public CartClass(String id_barang_cart, String nama_barang_cart, String waktu_selesai_cart, int harga_barang, int jumlah_barang, int jumlah_maks_barang) {
        this.id_barang_cart = id_barang_cart;
        this.nama_barang_cart = nama_barang_cart;
        this.waktu_selesai_cart = waktu_selesai_cart;
        this.harga_barang = harga_barang;
        this.jumlah_barang = jumlah_barang;
        this.jumlah_maks_barang = jumlah_maks_barang;
    }

    protected CartClass(Parcel in) {
        id_cart = in.readInt();
        id_barang_cart = in.readString();
        nama_barang_cart = in.readString();
        waktu_selesai_cart = in.readString();
        harga_barang = in.readInt();
        jumlah_barang = in.readInt();
        jumlah_maks_barang = in.readInt();
    }

    public static final Creator<CartClass> CREATOR = new Creator<CartClass>() {
        @Override
        public CartClass createFromParcel(Parcel in) {
            return new CartClass(in);
        }

        @Override
        public CartClass[] newArray(int size) {
            return new CartClass[size];
        }
    };

    public int getId_cart() {
        return id_cart;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public String getId_barang_cart() {
        return id_barang_cart;
    }

    public void setId_barang_cart(String id_barang_cart) {
        this.id_barang_cart = id_barang_cart;
    }

    public String getNama_barang_cart() {
        return nama_barang_cart;
    }

    public void setNama_barang_cart(String nama_barang_cart) {
        this.nama_barang_cart = nama_barang_cart;
    }

    public String getWaktu_selesai_cart() {
        return waktu_selesai_cart;
    }

    public void setWaktu_selesai_cart(String waktu_selesai_cart) {
        this.waktu_selesai_cart = waktu_selesai_cart;
    }

    public int getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(int harga_barang) {
        this.harga_barang = harga_barang;
    }

    public int getJumlah_barang() {
        return jumlah_barang;
    }

    public void setJumlah_barang(int jumlah_barang) {
        this.jumlah_barang = jumlah_barang;
    }

    public int getJumlah_maks_barang() {
        return jumlah_maks_barang;
    }

    public void setJumlah_maks_barang(int jumlah_maks_barang) {
        this.jumlah_maks_barang = jumlah_maks_barang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_cart);
        parcel.writeString(id_barang_cart);
        parcel.writeString(nama_barang_cart);
        parcel.writeString(waktu_selesai_cart);
        parcel.writeInt(harga_barang);
        parcel.writeInt(jumlah_barang);
        parcel.writeInt(jumlah_maks_barang);
    }
}
