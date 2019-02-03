package com.tma.android.contapp.data.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tma.android.contapp.data.Furnizor;
import com.tma.android.contapp.data.Nir;
import com.tma.android.contapp.data.Produs;

import java.util.List;

@Dao
public interface NirDao {

    @Query("SELECT * FROM nir ORDER BY numar")
    List<Nir> loadAllNir();

    @Insert
    void insertNir(Nir nir);

    @Update
    void updateNir(Nir nir);

    @Delete
    void deleteNir(Nir nir);

    @Query("SELECT * FROM nir WHERE cuiFurnizor = :cuiFurnizor")
    List<Nir> loadAllNirByCui(String cuiFurnizor);
}
