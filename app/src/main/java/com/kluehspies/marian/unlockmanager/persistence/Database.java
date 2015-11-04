package com.kluehspies.marian.unlockmanager.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "achievement.db";

    private static Database mDatabase = null;

    protected Database(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    public static synchronized Database getInstance(Context context) {
        if (mDatabase == null) {
            mDatabase = new Database(context, DATABASE_NAME, DATABASE_VERSION);
        }
        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = UnlockTable.TABLE_CREATE;
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
