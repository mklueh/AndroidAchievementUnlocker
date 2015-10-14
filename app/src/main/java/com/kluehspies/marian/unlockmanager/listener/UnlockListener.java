package com.kluehspies.marian.unlockmanager.listener;

import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

/**
 * Callbacks for any unlock
 *
 * @author Marian
 */
public interface UnlockListener {

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void unlockNotAvailable(int resourceID, IUnlockTrigger trigger);

    /**
     * Could be used for Incentive Ads
     *
     * @param resourceID
     * @param trigger
     */
    void unlockAvailable(int resourceID, IUnlockTrigger trigger);

    void unlockSucceeded(int resourceID, IUnlockTrigger trigger);

    void unlockFailed(int resourceID, IUnlockTrigger trigger);

}
