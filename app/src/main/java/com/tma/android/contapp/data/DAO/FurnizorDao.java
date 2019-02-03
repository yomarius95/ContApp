package com.tma.android.contapp.data.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tma.android.contapp.data.Furnizor;
import java.util.List;

@Dao
public interface FurnizorDao {

    @Query("SELECT * FROM furnizor ORDER BY nume")
    List<Furnizor> loadAllFurnizori();

    @Insert
    void insertFurnizor(Furnizor furnizor);

    @Update
    void updateFurnizor(Furnizor furnizor);

    @Delete
    void deleteFurnizor(Furnizor furnizor);
}
