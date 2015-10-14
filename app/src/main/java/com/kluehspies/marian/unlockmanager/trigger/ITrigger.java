package com.kluehspies.marian.unlockmanager.trigger;


import com.kluehspies.marian.unlockmanager.manager.IRewardManager;

/**
 * Created by Marian on 19.01.2015.
 *
 * Should only be used if inheritance of Trigger is not possible
 */
public interface ITrigger {

    void setUnlockManager(IRewardManager unlockManager);
}
