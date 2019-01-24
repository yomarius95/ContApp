package com.tma.android.contapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "nir")
public class Nir {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int numar;
    private Date data;
    private String serieAct;
    private int numarAct;
    private Date dataAct;
    private String numeFurnizor;
    private String cuiFurnizor;
    private ArrayList<Produs> listaProduse;

    @Ignore
    public Nir(int numar, Date data, String serieAct, int numarAct, Date dataAct, String numeFurnizor, String cuiFurnizor, ArrayList<Produs> listaProduse) {
        this.numar = numar;
        this.data = data;
        this.serieAct = serieAct;
        this.numarAct = numarAct;
        this.dataAct = dataAct;
        this.numeFurnizor = numeFurnizor;
        this.cuiFurnizor = cuiFurnizor;
        this.listaProduse = listaProduse;
    }

    public Nir(int id, int numar, Date data, String serieAct, int numarAct, Date dataAct, String numeFurnizor, String cuiFurnizor, ArrayList<Produs> listaProduse) {
        this.id = id;
        this.numar = numar;
        this.data = data;
        this.serieAct = serieAct;
        this.numarAct = numarAct;
        this.dataAct = dataAct;
        this.numeFurnizor = numeFurnizor;
        this.cuiFurnizor = cuiFurnizor;
        this.listaProduse = listaProduse;
    }

    public int getId() {
        return id;
    }

    public int getNumar() {
        return numar;
    }

    public Date getData() {
        return data;
    }

    public String getSerieAct() {
        return serieAct;
    }

    public int getNumarAct() {
        return numarAct;
    }

    public Date getDataAct() {
        return dataAct;
    }

    public String getNumeFurnizor() {
        return numeFurnizor;
    }

    public String getCuiFurnizor() {
        return cuiFurnizor;
    }

    public ArrayList<Produs> getListaProduse() {
        return listaProduse;
    }
}
