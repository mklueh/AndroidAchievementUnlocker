package com.kluehspies.marian.unlockmanager.persistence;

/**
 * Created by Andreas Schattney on 02.10.2015.
 */
class UnlockTable {

    public static final String TABLE_NAME = "resource";

    /**
     * identifier for the unlock event
     */
    public static final String COLUMN_KEY = "unique_key";

    /**
     * the action the unlock was triggered by
     */
    public static final String COLUMN_ACTION = "action";

    /**
     * State of the Event
     */
    public static final String COLUMN_UNLOCK_STATE = "unlock_state";

    /**
     * Timestamp event added
     */
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
