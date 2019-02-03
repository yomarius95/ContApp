package com.tma.android.contapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Entity(tableName = "produs", indices = {@Index("cuiFurnizor")}, foreignKeys = @ForeignKey(entity = Furnizor.class,
                                                        parentColumns = "cui",
                                                        childColumns = "cuiFurnizor"))
public class Produs implements Parcelable {

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
    private String cuiFurnizor;

    @Ignore
    public Produs(String nume, int unitateMasura, double cantitate, double pretIntrare, double pretIesire, int categorieTVA, String cuiFurnizor) {
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
        this.cuiFurnizor = cuiFurnizor;
    }

    @Ignore
    public Produs(int id, String nume, int unitateMasura, double cantitate, double pretIntrare, double pretIesire, int categorieTVA, String cuiFurnizor) {
        this.id = id;
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
        this.cuiFurnizor = cuiFurnizor;
    }

    public Produs(int id, String nume, int unitateMasura, double cantitate, double pretIntrare, double pretIesire, int categorieTVA, double valoareTotalIntrare, double valoareTotalTVA, double valoareTotalIntrareCuTVA, double valoareTotalIesire, double procentAdaos, String cuiFurnizor) {
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
        this.cuiFurnizor = cuiFurnizor;
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

    protected Produs(Parcel in) {
        id = in.readInt();
        nume = in.readString();
        unitateMasura = in.readInt();
        cantitate = in.readDouble();
        pretIntrare = in.readDouble();
        pretIesire = in.readDouble();
        categorieTVA = in.readInt();
        valoareTotalIntrare = in.readDouble();
        valoareTotalTVA = in.readDouble();
        valoareTotalIntrareCuTVA = in.readDouble();
        valoareTotalIesire = in.readDouble();
        procentAdaos = in.readDouble();
        cuiFurnizor = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nume);
        dest.writeInt(unitateMasura);
        dest.writeDouble(cantitate);
        dest.writeDouble(pretIntrare);
        dest.writeDouble(pretIesire);
        dest.writeInt(categorieTVA);
        dest.writeDouble(valoareTotalIntrare);
        dest.writeDouble(valoareTotalTVA);
        dest.writeDouble(valoareTotalIntrareCuTVA);
        dest.writeDouble(valoareTotalIesire);
        dest.writeDouble(procentAdaos);
        dest.writeString(cuiFurnizor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Produs> CREATOR = new Parcelable.Creator<Produs>() {
        @Override
        public Produs createFromParcel(Parcel in) {
            return new Produs(in);
        }

        @Override
        public Produs[] newArray(int size) {
            return new Produs[size];
        }
    };

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

    public String getCuiFurnizor() {
        return cuiFurnizor;
    }

    public void setCuiFurnizor(String cuiFurnizor) {
        this.cuiFurnizor = cuiFurnizor;
    }
}