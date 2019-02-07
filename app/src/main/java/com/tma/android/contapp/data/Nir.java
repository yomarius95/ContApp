package com.tma.android.contapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "nir", indices = {@Index("cuiFurnizor")}, foreignKeys = @ForeignKey(onDelete = CASCADE,
                                                                                entity = Furnizor.class,
                                                                                parentColumns = "cui",
                                                                                childColumns = "cuiFurnizor"))
public class Nir implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int numar;
    private String data;
    private String serieAct;
    private int numarAct;
    private String dataAct;
    private String numeFurnizor;
    private String cuiFurnizor;
    private ArrayList<Produs> listaProduse;

    @Ignore
    public Nir(int numar, String data, String serieAct, int numarAct, String dataAct, String numeFurnizor, String cuiFurnizor, ArrayList<Produs> listaProduse) {
        this.numar = numar;
        this.data = data;
        this.serieAct = serieAct;
        this.numarAct = numarAct;
        this.dataAct = dataAct;
        this.numeFurnizor = numeFurnizor;
        this.cuiFurnizor = cuiFurnizor;
        this.listaProduse = listaProduse;
    }

    public Nir(int id, int numar, String data, String serieAct, int numarAct, String dataAct, String numeFurnizor, String cuiFurnizor, ArrayList<Produs> listaProduse) {
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

    protected Nir(Parcel in) {
        id = in.readInt();
        numar = in.readInt();
        data = in.readString();
        serieAct = in.readString();
        numarAct = in.readInt();
        dataAct = in.readString();
        numeFurnizor = in.readString();
        cuiFurnizor = in.readString();
        if (in.readByte() == 0x01) {
            listaProduse = new ArrayList<>();
            in.readList(listaProduse, Produs.class.getClassLoader());
        } else {
            listaProduse = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(numar);
        dest.writeString(data);
        dest.writeString(serieAct);
        dest.writeInt(numarAct);
        dest.writeString(dataAct);
        dest.writeString(numeFurnizor);
        dest.writeString(cuiFurnizor);
        if (listaProduse == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(listaProduse);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Nir> CREATOR = new Parcelable.Creator<Nir>() {
        @Override
        public Nir createFromParcel(Parcel in) {
            return new Nir(in);
        }

        @Override
        public Nir[] newArray(int size) {
            return new Nir[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getNumar() {
        return numar;
    }

    public String getData() {
        return data;
    }

    public String getSerieAct() {
        return serieAct;
    }

    public int getNumarAct() {
        return numarAct;
    }

    public String getDataAct() {
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
