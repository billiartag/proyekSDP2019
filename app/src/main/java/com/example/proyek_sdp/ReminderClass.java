package com.example.proyek_sdp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ReminderClass {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_reminder")
    private Integer id_reminder;

    @ColumnInfo(name = "nama_barang")
    private String nama_barang;

    @ColumnInfo(name = "jumlah_barang")
    private Integer jumlah_barang;

    @ColumnInfo(name = "is_checked")
    private Boolean is_checked;

    public ReminderClass(String nama_barang, Integer jumlah_barang, Boolean is_checked) {
        this.nama_barang = nama_barang;
        this.jumlah_barang = jumlah_barang;
        this.is_checked = is_checked;
    }

    public Integer getId_reminder() {
        return id_reminder;
    }

    public void setId_reminder(Integer id_reminder) {
        this.id_reminder = id_reminder;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public Integer getJumlah_barang() {
        return jumlah_barang;
    }

    public void setJumlah_barang(Integer jumlah_barang) {
        this.jumlah_barang = jumlah_barang;
    }

    public Boolean getIs_checked() {
        return is_checked;
    }

    public void setIs_checked(Boolean is_checked) {
        this.is_checked = is_checked;
    }
}
