package com.kluehspies.marian.unlockmanager.persistence;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public abstract class PersistenceHandler<H> extends Trigger implements RewardListener<H> {

    protected IRewardManager mRewardManager;

    public PersistenceHandler(IRewardManager rewardManager) {
        super(rewardManager);
        this.mRewardManager = rewardManager;
    }
}
