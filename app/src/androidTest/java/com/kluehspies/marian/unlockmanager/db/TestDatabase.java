package com.kluehspies.marian.unlockmanager.db;

import android.content.Context;

import com.kluehspies.marian.unlockmanager.persistence.Database;

/**
 * Created by Andy on 16.10.2015.
 */
public class TestDatabase extends Database {

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    protected TestDatabase(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public void drop(){
        getWritableDatabase().execSQL(String.format("DROP DATABASE %s",DATABASE_NAME));
    }
}
