package com.daam.mascotas.model.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daam.mascotas.bean.Pet;

import java.util.List;

public class PetContentProvider extends ContentProvider {


    private final static String AUTHORITY_NAME = "net.gandalfran";

    private final static int CODE_ALL_ENTRIES = 1;
    private final static int CODE_SINGLE_ENTRY = 2;

    public static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY_NAME, PetContentProviderHelper.TABLE_NAME));

    private static final  String MIME_SINGLE_ENTRY = String.format("vnd.android.cursor.item/vnd.%s%s", AUTHORITY_NAME, PetContentProviderHelper.TABLE_NAME);
    private static final  String MIME_MULTIPLE_ENTRY = String.format("vnd.android.cursor.dir/vnd.%s%s", AUTHORITY_NAME, PetContentProviderHelper.TABLE_NAME);

    private static final UriMatcher um = new UriMatcher(UriMatcher.NO_MATCH);

    private final Context context;
    private final PetContentProviderHelper provider;

    static {
        um.addURI(AUTHORITY_NAME, PetContentProviderHelper.TABLE_NAME, CODE_ALL_ENTRIES);
        um.addURI(AUTHORITY_NAME, String.format("%s/#", PetContentProviderHelper.TABLE_NAME), CODE_SINGLE_ENTRY);
    }

    public PetContentProvider(){
        this.provider = null;
        this.context = null;
    }
    public PetContentProvider(Context context, List<Pet> pets){
        this.context = context;
        this.provider = new PetContentProviderHelper(context, null, 1, pets);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
