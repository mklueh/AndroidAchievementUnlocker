package com.kluehspies.marian.unlockmanager.listener;

import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Callbacks for any unlock
 *
 *
 * @author Marian Klühspies
 */
public interface RewardListener<M> {
    void unlockSucceeded(M item, Trigger<M> trigger);
    void unlockFailed(M item, Trigger<M> trigger);
}
