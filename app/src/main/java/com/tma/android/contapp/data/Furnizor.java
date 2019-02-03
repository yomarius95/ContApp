package com.tma.android.contapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "furnizor")
public class Furnizor  implements Parcelable {

    @NonNull @PrimaryKey()
    private String cui;
    private String nume;
    private String localitate;

    public Furnizor(@NonNull String cui, String nume, String localitate) {
        this.cui = cui;
        this.nume = nume;
        this.localitate = localitate;
    }

    @NonNull
    public String getCui() {
        return cui;
    }

    public String getNume() {
        return nume;
    }

    public String getLocalitate() {
        return localitate;
    }

    protected Furnizor(Parcel in) {
        cui = in.readString();
        nume = in.readString();
        localitate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cui);
        dest.writeString(nume);
        dest.writeString(localitate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Furnizor> CREATOR = new Parcelable.Creator<Furnizor>() {
        @Override
        public Furnizor createFromParcel(Parcel in) {
            return new Furnizor(in);
        }

        @Override
        public Furnizor[] newArray(int size) {
            return new Furnizor[size];
        }
    };
}
