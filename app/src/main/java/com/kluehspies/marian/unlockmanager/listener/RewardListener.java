package com.kluehspies.marian.unlockmanager.listener;

import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Callbacks for any unlock
 *
 * @author Marian
 */
public interface RewardListener<M> {

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void rewardNotAvailable(M resourceID, Trigger trigger);

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void rewardAvailable(M resourceID, Trigger trigger);

    void unlockSucceeded(M resourceID, Trigger trigger);

    void unlockFailed(M resourceID, Trigger trigger);

}
