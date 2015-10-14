package com.kluehspies.marian.unlockmanager.trigger;

import com.kluehspies.marian.unlockmanager.manager.IUnlockManager;

/**
 * Created by Marian on 01.06.2015.
 */
public class UnlockTrigger implements IUnlockTrigger {

    protected IUnlockManager unlockManager;

    @Override
    public void setUnlockManager(IUnlockManager unlockManager) {
        this.unlockManager = unlockManager;
    }

    public void unlockNotAvailable() {
        unlockManager.unlockNotAvailable(this);
    }

    public void unlockAvailable() {
        unlockManager.unlockAvailable(this);
    }

    public void unlockSucceeded() {
        unlockManager.unlockSucceeded(this);
    }

    public void unlockFailed() {
        unlockManager.unlockFailed(this);
    }
}
