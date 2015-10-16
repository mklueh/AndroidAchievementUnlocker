package com.kluehspies.marian.unlockmanager;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 30.05.2015.
 */
public class RewardManagerTest extends ApplicationTestCase<Application> implements RewardListener<Integer> {

    private boolean available = false;
    private boolean unlocked = false;

    private int resourceID = 0;
    private RewardManager<Integer> rewardManager;
    private Trigger unlockTrigger;

    public RewardManagerTest() {
        super(Application.class);
    }

    class SampleTrigger extends Trigger {
        public SampleTrigger(IRewardManager rewardManager) {
            super(rewardManager);
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        available = false;
        unlocked = false;
        rewardManager = new RewardManager<>();
        unlockTrigger = new SampleTrigger(rewardManager);
        rewardManager.bindListener(this, resourceID);
        rewardManager.bindTrigger(unlockTrigger = new Trigger(rewardManager), resourceID);
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

    /**
     * tests if trigger gets unregistered
     */
    public void testUnregisterTrigger(){
        boolean registered = rewardManager.isRegistered(unlockTrigger);
        if (registered) {
            rewardManager.unregisterTrigger(unlockTrigger);
            assertFalse(rewardManager.isRegistered(unlockTrigger));
        }else{
            fail("Trigger was not registered in first place!");
        }
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
