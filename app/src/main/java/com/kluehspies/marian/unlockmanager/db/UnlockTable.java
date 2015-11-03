package com.kluehspies.marian.unlockmanager.db;

/**
 * Created by Andreas Schattney on 02.10.2015.
 */
public class UnlockTable {

    public static final String TABLE_NAME = "resource";

    public static final String COLUMN_KEY = "unique_key";
    public static final String COLUMN_ACTION = "action";
    public static final String COLUMN_UNLOCK_STATE = "unlock_state";
    public static final String COLUMN_TIMESTAMP = "created_at";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + COLUMN_KEY + " TEXT NOT NULL, "
            + COLUMN_ACTION + " TEXT NOT NULL, "
            + COLUMN_UNLOCK_STATE + " TEXT NOT NULL, "
            + COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + String.format("PRIMARY KEY (%s,%s)",COLUMN_KEY,COLUMN_ACTION)
            + ");";

    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
