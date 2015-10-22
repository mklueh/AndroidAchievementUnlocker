package com.kluehspies.marian.unlockmanager;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.kluehspies.marian.unlockmanager.db.Achievement;
import com.kluehspies.marian.unlockmanager.db.AchievementDataSource;
import com.kluehspies.marian.unlockmanager.db.TestDatabase;
import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

import java.util.UUID;

/**
 * Created by Marian on 30.05.2015.
 */
public class RewardManagerTest extends ApplicationTestCase<Application> implements RewardListener<Integer> {

    private boolean available = false;
    private boolean unlocked = false;

    private int resourceID = 0;
    private RewardManager<Integer> rewardManager;
    private Trigger<Integer> unlockTrigger;
    private TestDatabase testDatabase;

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
        testDatabase = (TestDatabase) TestDatabase.getInstance(getContext());
        available = false;
        unlocked = false;
        rewardManager = new RewardManager<>(Integer.class);
        unlockTrigger = new SampleTrigger<>(Integer.class);
        rewardManager.bindListener(this, resourceID);
        rewardManager.bindTrigger(unlockTrigger = new Trigger<>(Integer.class), resourceID);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testDatabase.drop();
    }

    public void testUnlockSuccess() throws Exception {
        unlockTrigger.unlockSucceeded();
        assertTrue(unlocked);
    }

    public void testUnlockFailed() throws Exception {
        unlockTrigger.unlockFailed();
        assertFalse(unlocked);
    }

    public void testUnlockNotAvailabe() throws Exception {
        unlockTrigger.unlockNotAvailable();
        assertFalse(available);
    }

    public void testUnlockAvailable() throws Exception {
        unlockTrigger.unlockAvailable();
        assertTrue(available);
    }

    public void testAddAchievement(){
        AchievementDataSource dataSource = new AchievementDataSource(testDatabase);
        Achievement achievement = createFakeAchievement();
        dataSource.openDatabase();
        internalAddAchievement(dataSource,achievement);
        boolean equals = dataSource.getAchievements().size() == 1;
        dataSource.closeDatabase();
        assertTrue(equals);
    }

    public void testRemoveAchievement(){
        Achievement achievement = createFakeAchievement();
        AchievementDataSource dataSource = new AchievementDataSource(testDatabase);
        dataSource.openDatabase();
        internalAddAchievement(dataSource,achievement);
        dataSource.removeAchievement(achievement);
        boolean equals = dataSource.getAchievements().size() == 0;
        dataSource.closeDatabase();
        assertTrue(equals);
    }

    @NonNull
    private void internalAddAchievement(AchievementDataSource dataSource,Achievement achievement) {
        dataSource.addAchievement(achievement);
    }

    @NonNull
    private Achievement createFakeAchievement() {
        String key = UUID.randomUUID().toString();
        String action = UUID.randomUUID().toString();
        return new Achievement(key,action);
    }

    public void testGeneric(){
        Achievement item = createFakeAchievement();
        Trigger<Achievement> trigger = new Trigger<>(Achievement.class);
        AndroidAchievementUnlocker.bindTrigger(trigger,item);
        AndroidAchievementUnlocker.bindListener(new RewardListener<Achievement>() {
            @Override
            public void rewardNotAvailable(Achievement achievement, Trigger<Achievement> trigger) {

            }

            @Override
            public void rewardAvailable(Achievement achievement, Trigger<Achievement> trigger) {

            }

            @Override
            public void unlockSucceeded(Achievement achievement, Trigger<Achievement> trigger) {

            }

            @Override
            public void unlockFailed(Achievement achievement, Trigger<Achievement> trigger) {

            }
        },item);
    }

    @Override
    public void rewardNotAvailable(Integer resourceID, Trigger trigger) {
        available = false;
    }

    @Override
    public void rewardAvailable(Integer resourceID, Trigger trigger) {
        available = true;
    }

    @Override
    public void unlockSucceeded(Integer resourceID, Trigger trigger) {
        unlocked = true;
    }

    @Override
    public void unlockFailed(Integer resourceID, Trigger trigger) {
        unlocked = false;
    }
}
