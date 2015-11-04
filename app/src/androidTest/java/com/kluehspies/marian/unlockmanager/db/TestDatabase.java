package com.kluehspies.marian.unlockmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kluehspies.marian.unlockmanager.persistence.Database;

/**
 * Created by Andy on 16.10.2015.
 */
public class TestDatabase extends Database {

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    private static TestDatabase mTestDatabase;

    protected TestDatabase(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public static synchronized TestDatabase getInstance(Context context) {
        if (mTestDatabase == null)
            mTestDatabase = new TestDatabase(context);
        return mTestDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    public void drop(Context context){
        String path = getWritableDatabase().getPath();
        getWritableDatabase().close();
        context.deleteDatabase(path);
    }
}
