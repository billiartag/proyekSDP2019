package com.example.proyek_sdp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ReminderClass.class}, version = 1, exportSchema = false)
public abstract class ReminderDB extends RoomDatabase {
    public abstract ReminderDAO ReminderDAO();
}
