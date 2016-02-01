package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian Klühspies on 28.02.2015.
 */
public interface IRewardManager<M> {

    Class forClass();
    void unlockSucceeded(Trigger<M> trigger);
    void unlockFailed(Trigger<M> trigger);
    void bindTrigger(Trigger<M> trigger, M item);
    void bindTrigger(Trigger<M> trigger, M... item);
    void bindListener(RewardListener<M> listener, M item);
    void bindListener(RewardListener<M> listener, M... item);
    void unbindTriggers();
    void unbindListeners();
    void unbindTrigger(Trigger<M> trigger);
    void unbindListener(RewardListener<M> rewardListener, M item);
    void unbindListener(RewardListener<M> rewardListener, M... items);
    void unbindListeners(M item);
    void bindPersistenceHandler(PersistenceHandler<M> persistenceHandler);
    void triggerCurrentUnlockState(M item);
}
