package com.kluehspies.marian.unlockmanager;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;
import com.kluehspies.marian.unlockmanager.trigger.ITrigger;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 30.05.2015.
 */
public class RewardManagerTest extends ApplicationTestCase<Application> implements RewardListener {

    private boolean available = false;
    private boolean unlocked = false;

    private int resourceID = 0;
    private RewardManager unlockManager;
    private Trigger unlockTrigger;

    public RewardManagerTest() {
        super(Application.class);
    }

    class SampleTrigger extends Trigger {

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        available = false;
        unlocked = false;
        unlockManager = new RewardManager();
        unlockTrigger = new SampleTrigger();
        unlockManager.bindListener(this, resourceID);
        unlockManager.bindTrigger(unlockTrigger = new Trigger(), resourceID);
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

    @Override
    public void rewardNotAvailable(int resourceID, ITrigger trigger) {
        available = false;
    }

    @Override
    public void rewardAvailable(int resourceID, ITrigger trigger) {
        available = true;
    }

    @Override
    public void unlockSucceeded(int resourceID, ITrigger trigger) {
        unlocked = true;
    }

    @Override
    public void unlockFailed(int resourceID, ITrigger trigger) {
        unlocked = false;
    }
}
