package com.tma.android.contapp.data;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.tma.android.contapp.data.DAO.FurnizorDao;
import com.tma.android.contapp.data.DAO.NirDao;
import com.tma.android.contapp.data.DAO.ProdusDao;

@Database(entities = {Produs.class, Nir.class, Furnizor.class}, version = 7, exportSchema = false)
@TypeConverters({DateConverter.class, ArrayListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "contaDB";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FurnizorDao furnizorDao();
    public abstract ProdusDao produsDao();
    public abstract NirDao nirDao();
}
