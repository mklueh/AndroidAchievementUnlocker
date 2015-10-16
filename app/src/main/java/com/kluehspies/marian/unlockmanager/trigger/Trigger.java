package com.kluehspies.marian.unlockmanager.trigger;

import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;

/**
 * Created by Marian on 01.06.2015.
 */
public class Trigger {

    public Trigger(IRewardManager rewardManager){
        this.mRewardManager = rewardManager;
    }

    protected IRewardManager mRewardManager;

    public void unlockNotAvailable() {
        mRewardManager.unlockNotAvailable(this);
    }

    public void unlockAvailable() {
        mRewardManager.unlockAvailable(this);
    }

    public void unlockSucceeded() {
        mRewardManager.unlockSucceeded(this);
    }

    public void unlockFailed() {
        mRewardManager.unlockFailed(this);
    }

}
