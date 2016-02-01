package com.kluehspies.marian.unlockmanager.persistence;

import android.content.Context;

/**
 * Created by Andreas Schattney on 01.02.2016.
 */
public class DbFactory {

    public static Database create(Context context, TableParams params){
        Database database = Database.getInstance(context);
        database.createTableIfNotExists(params);
        return database;
    }

}
