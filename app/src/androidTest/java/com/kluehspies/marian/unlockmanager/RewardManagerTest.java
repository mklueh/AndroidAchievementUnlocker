package com.kluehspies.marian.unlockmanager;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.kluehspies.marian.unlockmanager.persistence.Achievement;
import com.kluehspies.marian.unlockmanager.db.TestDatabase;
import com.kluehspies.marian.unlockmanager.persistence.UnlockDataSource;
import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
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
    private UnlockDataSource<Achievement> dataSource;

    public RewardManagerTest() {
        super(Application.class);
    }

    class SampleTrigger<M> extends Trigger<M> {

        public SampleTrigger(Class clazz) {
            super(clazz);
        }

        public SampleTrigger(Class clazz, M... items) {
            super(clazz, items);
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        androidAchievementUnlocker = AndroidAchievementUnlocker.getDefault();
        testDatabase = TestDatabase.getInstance(getContext());
        dataSource = new UnlockDataSource<Achievement>(Achievement.class,testDatabase) {
            @Override
            protected String getTableName() {
                return null;
            }

            @Override
            protected Achievement getNewInstance() {
                return new Achievement();
            }
        };
        unlocked = false;
        unlockTrigger = new SampleTrigger<>(Integer.class);
        androidAchievementUnlocker.bindListener(this, resourceID);
        androidAchievementUnlocker.bindTrigger(unlockTrigger = new Trigger<>(Integer.class), resourceID);
    }

    @Override
    protected void tearDown() throws Exception {
        testDatabase.drop();
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
        Achievement achievement = createFakeAchievement();
        dataSource.openDatabase();
        internalAddAchievement(dataSource,achievement);
        boolean equals = dataSource.get().size() == 1;
        dataSource.closeDatabase();
        assertTrue(equals);
    }

    public void testRemoveAchievement(){
        Achievement achievement = createFakeAchievement();
        dataSource.openDatabase();
        internalAddAchievement(dataSource,achievement);
        dataSource.remove(achievement);
        boolean equals = dataSource.get().size() == 0;
        dataSource.closeDatabase();
        assertTrue(equals);
    }

    @NonNull
    private void internalAddAchievement(UnlockDataSource<Achievement> dataSource,Achievement achievement) {
        dataSource.add(achievement);
    }

    @NonNull
    private Achievement createFakeAchievement() {
        String key = UUID.randomUUID().toString();
        String action = UUID.randomUUID().toString();
        Achievement achievement = new Achievement();
        achievement.setKey(key);
        achievement.setAction(action);
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
        },item);
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
