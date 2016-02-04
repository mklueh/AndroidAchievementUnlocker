package com.kluehspies.marian.unlockmanager;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.kluehspies.marian.unlockmanager.db.TestDatabase;
import com.kluehspies.marian.unlockmanager.persistence.Achievement;
import com.kluehspies.marian.unlockmanager.persistence.TableParams;
import com.kluehspies.marian.unlockmanager.persistence.UnlockDataSource;
import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
import com.kluehspies.marian.unlockmanager.trigger.SharedPreferencesHandler;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

import java.util.UUID;

/**
 * Created by Marian on 30.05.2015.
 */
public class RewardManagerTest extends ApplicationTestCase<Application> implements RewardListener<Integer> {

    private boolean unlocked = false;
    private int resourceID = 0;
    private AndroidAchievementUnlocker androidAchievementUnlocker;
    private Trigger<Integer> unlockTrigger;
    private TestDatabase testDatabase;
    private UnlockDataSource<AchievementImpl> dataSource;
    private TableParams tableParams;

    public RewardManagerTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        tableParams = new TableParams.Builder().setTableName("test").build();
        androidAchievementUnlocker = AndroidAchievementUnlocker.getDefault();
        testDatabase = TestDatabase.getInstance(getContext());
        testDatabase.createTableIfNotExists(tableParams);
        dataSource = new UnlockDataSource<AchievementImpl>(AchievementImpl.class, testDatabase, tableParams) {

            @Override
            protected AchievementImpl createNewDataModelInstance() {
                return new AchievementImpl();
            }
        };
        androidAchievementUnlocker.addPersistenceHandler(dataSource);
        androidAchievementUnlocker.addPersistenceHandler(new SharedPreferencesHandler<>(Integer.class, getContext(), "integer_key"));
        unlocked = false;
        androidAchievementUnlocker.bindListener(this, resourceID);
        unlockTrigger = new Trigger<>(Integer.class);
        androidAchievementUnlocker.bindTrigger(unlockTrigger, resourceID);
    }

    @Override
    protected void tearDown() throws Exception {
        testDatabase.dropTableIfExists(tableParams);
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().apply();
        testDatabase.drop(getContext());
        super.tearDown();
    }

    public void testUnlockSuccess() throws Exception {
        unlockTrigger.unlockSucceeded();
        assertTrue(unlocked);
    }

    public void testUnlockFailed() throws Exception {
        unlockTrigger.unlockFailed();
        assertFalse(unlocked);
    }

    public void testAddAchievement(){
        AchievementImpl achievement = createFakeAchievement();
        dataSource.openDatabase();
        internalAddAchievement(dataSource,achievement);
        boolean equals = dataSource.get().size() == 1;
        dataSource.closeDatabase();
        assertTrue(equals);
    }

    public void testRemoveAchievement(){
        AchievementImpl achievement = createFakeAchievement();
        dataSource.openDatabase();
        internalAddAchievement(dataSource,achievement);
        dataSource.remove(achievement);
        boolean equals = dataSource.get().size() == 0;
        dataSource.closeDatabase();
        assertTrue(equals);
    }

    @NonNull
    private void internalAddAchievement(UnlockDataSource<AchievementImpl> dataSource,AchievementImpl achievement) {
        dataSource.add(achievement);
    }

    @NonNull
    private AchievementImpl createFakeAchievement() {
        String key = UUID.randomUUID().toString();
        AchievementImpl achievement = new AchievementImpl();
        achievement.setKey(key);
        return achievement;
    }

    public void testGeneric(){
        Achievement item = createFakeAchievement();
        Trigger<Achievement> trigger = new Trigger<>(Achievement.class);
        AndroidAchievementUnlocker.getDefault().bindTrigger(trigger,item);
        AndroidAchievementUnlocker.getDefault().bindListener(new RewardListener<Achievement>() {

            @Override
            public void unlockSucceeded(Achievement achievement, Trigger<Achievement> trigger) {

            }

            @Override
            public void unlockFailed(Achievement achievement, Trigger<Achievement> trigger) {

            }
        }, item);
    }

    @Override
    public void unlockSucceeded(Integer item, Trigger<Integer> trigger) {
        unlocked = true;
    }

    @Override
    public void unlockFailed(Integer item, Trigger<Integer> trigger) {
        unlocked = false;
    }
}
