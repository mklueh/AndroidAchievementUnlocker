package com.kluehspies.marian.unlockmanager.trigger;

import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Marian on 01.06.2015.
 */
public class Trigger<M> {

    private final Class clazz;

    public Trigger(Class clazz){
        this.clazz = clazz;
    }

    public Trigger(Class clazz,M... items){
        this(clazz);
        AndroidAchievementUnlocker.bindTrigger(this,items);
    }

    public Class forClass() {
        return clazz;
    }

    public void unlockNotAvailable() {
        AndroidAchievementUnlocker.unlockNotAvailable(this);
    }

    public void unlockAvailable() {
        AndroidAchievementUnlocker.unlockAvailable(this);
    }

    public void unlockSucceeded() {
        AndroidAchievementUnlocker.unlockSucceeded(this);
    }

    public void unlockFailed() {
        AndroidAchievementUnlocker.unlockFailed(this);
    }

}
