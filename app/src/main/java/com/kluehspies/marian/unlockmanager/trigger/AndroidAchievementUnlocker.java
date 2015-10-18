package com.kluehspies.marian.unlockmanager.trigger;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;
import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Andy on 17.10.2015.
 */
public class AndroidAchievementUnlocker {

    private static ConcurrentMap<Class,IRewardManager<?>> rewardManagers = new ConcurrentHashMap<>();

    public static <M> void bindPersistenceHandler(PersistenceHandler<M> persistenceHandler){
        Class clazz = persistenceHandler.forClass();
        if (!rewardManagerExists(clazz))
            addRewardManager(new RewardManager<M>(clazz));
        IRewardManager<M> rewardManager = getRewardManager(persistenceHandler.forClass());
        rewardManager.bindPersistenceHandler(persistenceHandler);
    }

    public static <M> void triggerUnlockIfAvailable(M resourceID) {
        getRewardManager(resourceID.getClass()).triggerUnlockIfAvailable(resourceID);
    }

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
        if (rewardManagers.containsKey(clazz)){
            rewardManagers.remove(clazz);
        }
    }

    private static <M> IRewardManager<M> getRewardManager(Class clazz){
        IRewardManager<M> rewardManager = (IRewardManager<M>) rewardManagers.get(clazz);
        return rewardManager;
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
        IRewardManager<M> rewardManager = getRewardManager(trigger.forClass());
        rewardManager.unlockNotAvailable(trigger);
    }

    static <M> void unlockAvailable(Trigger<M> trigger) {
        IRewardManager<M> rewardManager = getRewardManager(trigger.forClass());
        rewardManager.unlockAvailable(trigger);
    }

    static <M> void unlockSucceeded(Trigger<M> trigger) {
        IRewardManager<M> rewardManager = getRewardManager(trigger.forClass());
        rewardManager.unlockSucceeded(trigger);
    }

    static <M> void unlockFailed(Trigger<M> trigger) {
        IRewardManager<M> rewardManager = getRewardManager(trigger.forClass());
        rewardManager.unlockFailed(trigger);
    }

    public static <M> void unbindTriggers(Class<M> clazz) {
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindTriggers();
    }

    public static <M> void unbindListeners(Class<M> clazz) {
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindListeners();
    }

    public static void unbindAll(Class clazz){
        if (rewardManagers.containsKey(clazz)){
            IRewardManager rewardManager = getRewardManager(clazz);
            rewardManager.unbindTriggers();
            rewardManager.unbindListeners();
            rewardManagers.remove(clazz);
        }
    }

    public static void unbindAll(Class... clazzes){
        for (Class clazz : clazzes)
            unbindAll(clazz);
    }

    public static <M> void unbindTrigger(Trigger<M> trigger) {
        Class clazz = trigger.forClass();
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindTrigger(trigger);
    }

    public static <M> void unbindTriggers(Class<M>... clazzes) {
        for (Class<M> clazz : clazzes){
          unbindTriggers(clazz);
        }
    }

    public static <M> void unbindListeners(Class<M>... clazzes) {
        for (Class<M> clazz : clazzes){
            unbindListeners(clazz);
        }
    }

    public static void unbindTriggers() {
        for (Map.Entry<Class,IRewardManager<?>> entry : rewardManagers.entrySet())
            entry.getValue().unbindTriggers();
        rewardManagers.clear();
    }

    public static void unbindListeners() {
        for (Map.Entry<Class,IRewardManager<?>> entry : rewardManagers.entrySet())
            entry.getValue().unbindListeners();
        rewardManagers.clear();
    }
}
