package com.kluehspies.marian.unlockmanager.trigger;

/**
 * Created by Marian Klühspies on 01.06.2015.
 */
public class Trigger<M> {

    private final Class<M> clazz;

    public Trigger(Class<M> clazz) {
        this.clazz = clazz;
    }

    public Trigger(Class<M> clazz, M... items) {
        this(clazz);
        AndroidAchievementUnlocker.getDefault().bindTrigger(this, items);
    }

    public Class forClass() {
        return clazz;
    }

    public void unlockSucceeded() {
        AndroidAchievementUnlocker.getDefault().unlockSucceeded(this);
    }

    public void unlockFailed() {
        AndroidAchievementUnlocker.getDefault().unlockFailed(this);
    }

}
