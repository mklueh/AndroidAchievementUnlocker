package com.kluehspies.marian.unlockmanager.trigger;

/**
 * Created by Marian Klühspies on 01.06.2015.
 */
public class Trigger<M> {

    private final Class<M> clazz;
    private final String name;

    public Trigger(Class<M> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }

    public Trigger(Class<M> clazz) {
        this(clazz, "");
    }

    public String getName() {
        return name;
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
