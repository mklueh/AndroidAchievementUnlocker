package com.kluehspies.marian.unlockmanager.persistence;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian Klühspies on 14.10.2015.
 */
public abstract class PersistenceHandler<M> extends Trigger<M> {

    public PersistenceHandler(Class<M> clazz) {
        super(clazz);
    }

    public abstract boolean isUnlocked(M item);
    public abstract void unlock(M item, String triggerName);
    public abstract void lock(M item, String triggerName);
    public abstract String getItemTriggeredFrom(M item);
}
