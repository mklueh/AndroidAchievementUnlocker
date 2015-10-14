package com.kluehspies.marian.unlockmanager.trigger;

import com.kluehspies.marian.unlockmanager.manager.IRewardManager;

/**
 * Created by Marian on 01.06.2015.
 */
public class Trigger implements ITrigger {

    protected IRewardManager unlockManager;

    @Override
    public void setUnlockManager(IRewardManager unlockManager) {
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
