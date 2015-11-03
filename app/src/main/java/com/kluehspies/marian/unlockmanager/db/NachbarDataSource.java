package com.kluehspies.marian.unlockmanager.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Andy on 03.11.2015.
 */
public class NachbarDataSource extends UnlockDataSource<NachbarModel> {

    public NachbarDataSource(Database database) {
        super(NachbarModel.class, database);
    }

    @Override
    protected NachbarModel getNewInstance() {
        return new NachbarModel();
    }

    @Override
    protected ContentValues onBindValues(ContentValues contentValues, NachbarModel item) {
        return super.onBindValues(contentValues, item);
    }

    @Override
    protected NachbarModel onBindModel(Cursor cursor, NachbarModel item) {
        return super.onBindModel(cursor, item);
    }
}
