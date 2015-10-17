package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 28.02.2015.
 */
public interface IRewardManager<M> {

    Class forClass();
    void unlockNotAvailable(Trigger<M> trigger);
    void unlockAvailable(Trigger<M> trigger);
    void unlockSucceeded(Trigger<M> trigger);
    void unlockFailed(Trigger<M> trigger);
    void bindTrigger(Trigger<M> trigger, M item);
    void bindTrigger(Trigger<M> trigger, M... item);
    void bindListener(RewardListener<M> listener, M item);
    void bindListener(RewardListener<M> listener, M... item);
    void unbindTriggers();
    void unbindListeners();
}
