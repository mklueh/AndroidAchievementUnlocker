package com.kluehspies.marian.unlockmanager.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 15.10.2015.
 */
public class AchievementDataSource {

    private final Database database;
    private SQLiteDatabase sqLiteDatabase;

    public static final String STATE_UNLOCKED = "UNLOCKED";
    public static final String STATE_LOCKED = "LOCKED";

    public AchievementDataSource(Database database){
        this.database = database;
    }

    public void openDatabase(){
        sqLiteDatabase = database.getWritableDatabase();
    }

    public boolean addAchievement(Achievement achievement){
        return addAchievement(achievement,null);
    }

    public boolean addAchievement(Achievement achievement,String state){
        ContentValues cv = new ContentValues();
        cv.put(AchievementTable.COLUMN_KEY,achievement.getKey());
        cv.put(AchievementTable.COLUMN_ACTION,achievement.getAction());
        cv.put(AchievementTable.COLUMN_UNLOCK_STATE, (state == null) ? STATE_LOCKED : state);
        return sqLiteDatabase.insert(AchievementTable.TABLE_NAME, null, cv) > 0;
    }

    public boolean updateAchievement(Achievement achievement, String state){
        ContentValues cv = new ContentValues();
        cv.put(AchievementTable.COLUMN_UNLOCK_STATE,state);
        return sqLiteDatabase.update(
                AchievementTable.TABLE_NAME,
                cv,
                String.format("%s = ? AND %s = ?", AchievementTable.COLUMN_KEY, AchievementTable.COLUMN_ACTION),
                new String[]{achievement.getKey(), achievement.getAction()}
        ) > 0;
    }

    public boolean removeAchievement(Achievement achievement){
        return sqLiteDatabase.delete(
                AchievementTable.TABLE_NAME,
                String.format("%s = ? AND %s = ?", AchievementTable.COLUMN_KEY, AchievementTable.COLUMN_ACTION),
                new String[]{achievement.getKey(), achievement.getAction()}
        ) > 0;
    }

    public boolean isAchievementLocked(Achievement achievement){
        return getAchievementStatus(achievement).equals(STATE_LOCKED);
    }

    public boolean isAchievementUnlocked(Achievement achievement){
        return getAchievementStatus(achievement).equals(STATE_UNLOCKED);
    }

    private String getAchievementStatus(Achievement achievement){
        String status = null;
        Cursor cursor = sqLiteDatabase.query(
                AchievementTable.TABLE_NAME,
                new String[]{AchievementTable.COLUMN_UNLOCK_STATE},
                String.format("%s = ? AND %s = ?", AchievementTable.COLUMN_KEY, AchievementTable.COLUMN_ACTION),
                new String[]{achievement.getKey(), achievement.getAction()},
                null,null,null
        );
        if (cursor.moveToFirst()){
            status = cursor.getString(0);
        }
        cursor.close();
        return status;
    }

    public List<Achievement> getAchievements(){
        return internalGetAchievements(null);
    }

    public List<Achievement> getLockedAchievements(){
        return internalGetAchievements(STATE_LOCKED);
    }

    public List<Achievement> getUnlockedAchievements(){
        return internalGetAchievements(STATE_UNLOCKED);
    }

    private List<Achievement> internalGetAchievements(String withState){
        List<Achievement> achievements = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(
                AchievementTable.TABLE_NAME,
                null,
                String.format("%s = ?", AchievementTable.COLUMN_UNLOCK_STATE),
                (withState != null) ? new String[]{withState} : null,
                null,null,null
        );
        if (cursor.moveToFirst()){
            do{
                String key = cursor.getString(cursor.getColumnIndex(AchievementTable.COLUMN_KEY));
                String action = cursor.getString(cursor.getColumnIndex(AchievementTable.COLUMN_ACTION));
                String state = cursor.getString(cursor.getColumnIndex(AchievementTable.COLUMN_UNLOCK_STATE));
                achievements.add(new Achievement(key,action,state));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return achievements;
    }

    public void closeDatabase(){
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

}
