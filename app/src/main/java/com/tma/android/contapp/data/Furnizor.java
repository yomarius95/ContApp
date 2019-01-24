package com.tma.android.contapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "furnizor")
public class Furnizor {

    @NonNull @PrimaryKey(autoGenerate = false)
    private String cui;
    private String nume;
    private String localitate;

    public Furnizor(String cui, String nume, String localitate) {
        this.cui = cui;
        this.nume = nume;
        this.localitate = localitate;
    }

    public String getCui() {
        return cui;
    }

    public String getNume() {
        return nume;
    }

    public String getLocalitate() {
        return localitate;
    }
}
