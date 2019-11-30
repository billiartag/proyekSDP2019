package com.example.proyek_sdp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDAO {

    @Query("SELECT * FROM ReminderClass where email_user=:email")
    List<ReminderClass> getAllReminder(String email);
    @Insert
    void addNewReminder(ReminderClass obj);
    @Delete
    void deleteReminder(ReminderClass obj);
    @Update
    void updateReminder(ReminderClass obj);
}
