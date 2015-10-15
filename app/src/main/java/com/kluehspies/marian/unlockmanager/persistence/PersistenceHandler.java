package com.kluehspies.marian.unlockmanager.persistence;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.ITrigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class PersistenceHandler implements RewardListener {
    @Override
    public void rewardNotAvailable(int resourceID, ITrigger trigger) {

    }
    @Override
    public void rewardAvailable(int resourceID, ITrigger trigger) {

    }

    @Override
    public void unlockSucceeded(int resourceID, ITrigger trigger) {

    }

    @Override
    public void unlockFailed(int resourceID, ITrigger trigger) {

    }
}
