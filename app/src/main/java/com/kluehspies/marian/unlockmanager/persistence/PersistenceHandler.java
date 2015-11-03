package com.kluehspies.marian.unlockmanager.persistence;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian Klühspies on 14.10.2015.
 */
public abstract class PersistenceHandler<M> extends Trigger<M> implements RewardListener<M> {
    public PersistenceHandler(Class clazz) {
        super(clazz);
    }
    public abstract void triggerUnlockIfAvailable(M item);
    public abstract boolean isUnlocked(M item);
}
