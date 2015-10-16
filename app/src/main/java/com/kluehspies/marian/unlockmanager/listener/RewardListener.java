package com.kluehspies.marian.unlockmanager.listener;

import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Callbacks for any unlock
 *
 * @author Marian
 */
public interface RewardListener<H> {

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void rewardNotAvailable(H resourceID, Trigger trigger);

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void rewardAvailable(H resourceID, Trigger trigger);

    void unlockSucceeded(H resourceID, Trigger trigger);

    void unlockFailed(H resourceID, Trigger trigger);

}
