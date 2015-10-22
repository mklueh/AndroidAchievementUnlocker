package com.kluehspies.marian.unlockmanager.listener;

import com.kluehspies.marian.unlockmanager.trigger.ITrigger;

/**
 * Callbacks for any unlock
 *
 *
 * @author Marian
 */
public interface RewardListener {

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void rewardNotAvailable(int resourceID, ITrigger trigger);

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void rewardAvailable(int resourceID, ITrigger trigger);

    void unlockSucceeded(int resourceID, ITrigger trigger);

    void unlockFailed(int resourceID, ITrigger trigger);

}
