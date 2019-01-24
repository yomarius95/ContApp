package com.tma.android.contapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Entity(tableName = "produs")
public class Produs {

    private static final int COTA_TVA_0 = 0;
    private static final int COTA_TVA_5 = 1;
    private static final int COTA_TVA_9 = 2;
    private static final int COTA_TVA_19 = 3;
    private static final int COTA_TVA_20 = 4;
    private static final int COTA_TVA_24 = 5;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nume;
    private int unitateMasura;
    private double cantitate;
    private double pretIntrare;
    private double pretIesire;
    private int categorieTVA;
    private double valoareTotalIntrare;
    private double valoareTotalTVA;
    private double valoareTotalIntrareCuTVA;
    private double valoareTotalIesire;
    private double procentAdaos;

    @Ignore
    public Produs(String nume, int unitateMasura, double cantitate, double pretIntrare, double pretIesire, int categorieTVA) {
        this.nume = nume;
        this.unitateMasura = unitateMasura;
        this.cantitate = cantitate;
        this.pretIntrare = pretIntrare;
        this.pretIesire = pretIesire;
        this.categorieTVA = categorieTVA;
        this.valoareTotalIntrare = calculValoareTotalIntrare();
        this.valoareTotalTVA = calculValoareTotalTVA();
        this.valoareTotalIntrareCuTVA = valoareTotalTVA + valoareTotalIntrare;
        this.valoareTotalIesire = calculValoareTotalIesire();
        this.procentAdaos = calculProcentAdaos();
    }

    public Produs(int id, String nume, int unitateMasura, double cantitate, double pretIntrare, double pretIesire, int categorieTVA, double valoareTotalIntrare, double valoareTotalTVA, double valoareTotalIntrareCuTVA, double valoareTotalIesire, double procentAdaos) {
        this.id = id;
        this.nume = nume;
        this.unitateMasura = unitateMasura;
        this.cantitate = cantitate;
        this.pretIntrare = pretIntrare;
        this.pretIesire = pretIesire;
        this.categorieTVA = categorieTVA;
        this.valoareTotalIntrare = valoareTotalIntrare;
        this.valoareTotalTVA = valoareTotalTVA;
        this.valoareTotalIntrareCuTVA = valoareTotalTVA + valoareTotalIntrare;
        this.valoareTotalIesire = valoareTotalIesire;
        this.procentAdaos = procentAdaos;
    }

    private double calculValoareTotalIntrare() {
        DecimalFormat df = new DecimalFormat("#.000", new DecimalFormatSymbols(Locale.US));

        return Double.parseDouble(df.format(pretIntrare * cantitate));
    }

    private double calculValoareTotalIesire() {
        DecimalFormat df = new DecimalFormat("#.000", new DecimalFormatSymbols(Locale.US));

        return Double.parseDouble(df.format(pretIesire * cantitate));
    }

    private double calculValoareTotalTVA() {
        double procentTVA;

        switch (categorieTVA) {
            case COTA_TVA_0:
                procentTVA = 0;
                break;
            case COTA_TVA_5:
                procentTVA = 0.05;
                break;
            case COTA_TVA_9:
                procentTVA = 0.09;
                break;
            case COTA_TVA_19:
                procentTVA = 0.19;
                break;
            case COTA_TVA_20:
                procentTVA = 0.2;
                break;
            case COTA_TVA_24:
                procentTVA = 0.24;
                break;
            default:
                throw new IllegalArgumentException("Procent TVA invalid");
        }

        DecimalFormat df = new DecimalFormat("#.000", new DecimalFormatSymbols(Locale.US));

        return Double.parseDouble(df.format(valoareTotalIntrare * procentTVA));
    }

    private double calculProcentAdaos() {
        double procentAdaos = (valoareTotalIesire - valoareTotalIntrareCuTVA) / valoareTotalIntrareCuTVA * 100;
        DecimalFormat df = new DecimalFormat("#.00", new DecimalFormatSymbols(Locale.US));

        return Double.parseDouble(df.format(procentAdaos));
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setUnitateMasura(int unitateMasura) {
        this.unitateMasura = unitateMasura;
    }

    public void setCantitate(double cantitate) {
        this.cantitate = cantitate;
    }

    public void setPretIntrare(double pretIntrare) {
        this.pretIntrare = pretIntrare;
    }

    public void setPretIesire(double pretIesire) {
        this.pretIesire = pretIesire;
    }

    public void setCategorieTVA(int categorieTVA) {
        this.categorieTVA = categorieTVA;
    }

    public String getNume() {
        return nume;
    }

    public int getUnitateMasura() {
        return unitateMasura;
    }

    public double getCantitate() {
        return cantitate;
    }

    public double getPretIntrare() {
        return pretIntrare;
    }

    public double getPretIesire() {
        return pretIesire;
    }

    public int getCategorieTVA() {
        return categorieTVA;
    }

    public double getValoareTotalIntrare() {
        return valoareTotalIntrare;
    }

    public double getValoareTotalTVA() {
        return valoareTotalTVA;
    }

    public double getValoareTotalIntrareCuTVA() {
        return valoareTotalIntrareCuTVA;
    }

    public double getValoareTotalIesire() {
        return valoareTotalIesire;
    }

    public double getProcentAdaos() {
        return procentAdaos;
    }

    public int getId() {
        return id;
    }
}