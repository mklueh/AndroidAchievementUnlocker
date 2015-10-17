package com.kluehspies.marian.unlockmanager.trigger;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Andy on 17.10.2015.
 */
public class AndroidAchievementUnlocker {

    private static ConcurrentMap<Class,IRewardManager<?>> rewardManagers = new ConcurrentHashMap<>();

    private static void addRewardManager(IRewardManager rewardManager){
        Class clazz = rewardManager.forClass();
        if (!rewardManagers.containsKey(clazz)){
            rewardManagers.put(clazz,rewardManager);
        }
    }

    private static boolean rewardManagerExists(Class clazz){
        return rewardManagers.containsKey(clazz);
    }

    private static void removeRewardManager(IRewardManager rewardManager){
        Class clazz = rewardManager.forClass();
        if (!rewardManagers.containsKey(clazz)){
            rewardManagers.put(clazz,rewardManager);
        }
    }

    private static IRewardManager getRewardManager(Class clazz){
        return rewardManagers.get(clazz);
    }

    public static <M> void bindTrigger(Trigger<M> trigger,M item){
        Class clazz = item.getClass();
        if (!rewardManagerExists(clazz))
            addRewardManager(new RewardManager<M>(clazz));
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.bindTrigger(trigger,item);
    }

    public static <M> void bindTrigger(Trigger<M> trigger,M... items){
        for (M item : items)
            bindTrigger(trigger,item);
    }

    public static <M> void bindListener(RewardListener<M> trigger,M item){
        Class clazz = item.getClass();
        if (!rewardManagerExists(clazz))
            addRewardManager(new RewardManager<M>(clazz));
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.bindListener(trigger, item);
    }

    public static <M> void bindListener(RewardListener<M> trigger,M... items){
        for (M item : items){
            bindListener(trigger,item);
        }
    }

    static <M> void unlockNotAvailable(Trigger<M> trigger) {
        getRewardManager(trigger.forClass()).unlockNotAvailable(trigger);
    }

    static <M> void unlockAvailable(Trigger<M> trigger) {
        getRewardManager(trigger.forClass()).unlockAvailable(trigger);
    }

    static <M> void unlockSucceeded(Trigger<M> trigger) {
        getRewardManager(trigger.forClass()).unlockSucceeded(trigger);
    }

    static <M> void unlockFailed(Trigger<M> trigger) {
        getRewardManager(trigger.forClass()).unlockFailed(trigger);
    }

    public static <M> void unbindTriggers(Class<M> clazz) {
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindTriggers();
    }

    public static <M> void unbindListeners(Class<M> clazz) {
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindListeners();
    }
}
