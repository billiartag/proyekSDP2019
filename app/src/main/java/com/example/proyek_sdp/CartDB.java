package com.example.proyek_sdp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CartClass.class}, version = 1, exportSchema = false)
public abstract class CartDB extends RoomDatabase {
    public abstract CartDAO cartDAO();
}
