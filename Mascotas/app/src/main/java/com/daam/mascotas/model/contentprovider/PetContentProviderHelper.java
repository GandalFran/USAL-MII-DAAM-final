package com.daam.mascotas.model.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daam.mascotas.bean.Pet;

import java.util.List;

public class PetContentProviderHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "PETS_TABLE";

    private Context context;
    private List<Pet> pets;

    public PetContentProviderHelper(Context context, SQLiteDatabase.CursorFactory factory, int version, List<Pet> pets) {
        super(context, TABLE_NAME, factory, version);
        this.pets = pets;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
