package com.kluehspies.marian.example;

import android.content.ContentValues;
import android.database.Cursor;

import com.kluehspies.marian.unlockmanager.persistence.Database;
import com.kluehspies.marian.unlockmanager.persistence.TableParams;
import com.kluehspies.marian.unlockmanager.persistence.UnlockDataSource;

/**
 * Created by Andreas Schattney on 04.02.2016.
 */
public class UserAchievementDataSource extends UnlockDataSource<UserAchievement> {

    public static final String COLUMN_USER = "user";

    public UserAchievementDataSource(Database database, TableParams params) {
        super(UserAchievement.class, database, params);
    }

    @Override
    protected UserAchievement createNewDataModelInstance() {
        return new UserAchievement();
    }

    @Override
    protected void onBindValues(ContentValues contentValues, UserAchievement item) {
        super.onBindValues(contentValues, item);
        contentValues.put(COLUMN_USER, item.getUser());
    }

    @Override
    protected void onBindModel(Cursor cursor, UserAchievement item) {
        super.onBindModel(cursor, item);
        item.setUser(cursor.getString(cursor.getColumnIndex(COLUMN_USER)));
    }
}
