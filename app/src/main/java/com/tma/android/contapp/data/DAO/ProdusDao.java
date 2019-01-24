package com.tma.android.contapp.data.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tma.android.contapp.data.Produs;

import java.util.ArrayList;

@Dao
public interface ProdusDao {

    @Query("SELECT * FROM produs ORDER BY nume")
    ArrayList<Produs> loadAllProduse();

    @Insert
    void insertProdus(Produs produs);

    @Update
    void updateProdus(Produs produs);

    @Delete
    void deleteProdus(Produs produs);
}
