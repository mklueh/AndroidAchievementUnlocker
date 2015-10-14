package com.kluehspies.marian.unlockmanager;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.kluehspies.marian.unlockmanager.listener.UnlockListener;
import com.kluehspies.marian.unlockmanager.manager.UnlockManager;
import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;
import com.kluehspies.marian.unlockmanager.trigger.UnlockTrigger;

/**
 * Created by Marian on 30.05.2015.
 */
public class UnlockManagerTest extends ApplicationTestCase<Application> implements UnlockListener {

    private boolean available = false;
    private boolean unlocked = false;

    private int resourceID = 0;
    private UnlockManager unlockManager;
    private UnlockTrigger unlockTrigger;

    public UnlockManagerTest() {
        super(Application.class);
    }

    class SampleTrigger extends UnlockTrigger {

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        available = false;
        unlocked = false;
        unlockManager = new UnlockManager();
        unlockTrigger = new SampleTrigger();
        unlockManager.bindListener(this, resourceID);
        unlockManager.bindTrigger(unlockTrigger = new UnlockTrigger(), resourceID);
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
    public void unlockNotAvailable(int resourceID, IUnlockTrigger trigger) {
        available = false;
    }

    @Override
    public void unlockAvailable(int resourceID, IUnlockTrigger trigger) {
        available = true;
    }

    @Override
    public void unlockSucceeded(int resourceID, IUnlockTrigger trigger) {
        unlocked = true;
    }

    @Override
    public void unlockFailed(int resourceID, IUnlockTrigger trigger) {
        unlocked = false;
    }
}
