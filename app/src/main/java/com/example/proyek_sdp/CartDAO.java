package com.example.proyek_sdp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM CartClass where email_user=:email")
    List<CartClass> getAllBarang(String email);
    @Insert
    void addNewBarang(CartClass obj);
    @Delete
    void deleteBarang(CartClass obj);
    @Update
    void updateBarang(CartClass obj);
}
