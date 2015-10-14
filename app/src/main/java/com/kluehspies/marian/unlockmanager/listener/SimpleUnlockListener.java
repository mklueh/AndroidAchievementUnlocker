package com.kluehspies.marian.unlockmanager.listener;


import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

/**
 * Created by Marian on 01.06.2015.
 *
 * Overwrite only those methods you need
 */
public abstract class SimpleUnlockListener implements UnlockListener {

    @Override
    public void unlockNotAvailable(int resourceID, IUnlockTrigger trigger) {

    }

    @Override
    public void unlockAvailable(int resourceID, IUnlockTrigger trigger) {

    }

    @Override
    public void unlockSucceeded(int resourceID, IUnlockTrigger trigger) {

    }

    @Override
    public void unlockFailed(int resourceID, IUnlockTrigger trigger) {

    }
}
