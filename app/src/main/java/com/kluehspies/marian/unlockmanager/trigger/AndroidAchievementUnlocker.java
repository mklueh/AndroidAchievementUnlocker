package com.kluehspies.marian.unlockmanager.trigger;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;
import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Andreas Schattney on 17.10.2015.
 */
public class AndroidAchievementUnlocker {

    private static AndroidAchievementUnlocker defaultInstance;

    public static synchronized AndroidAchievementUnlocker getDefault(){
        if (defaultInstance == null)
            defaultInstance = new AndroidAchievementUnlocker();
        return defaultInstance;
    }

    public void setPersistenceHandler(PersistenceHandler<?> persistenceHandler){
        if (!persistenceHandlers.containsKey(persistenceHandler.forClass())){
            persistenceHandlers.put(persistenceHandler.forClass(), persistenceHandler);
        }
    }

    private AndroidAchievementUnlocker(){}

    private ConcurrentMap<Class,IRewardManager<?>> rewardManagers = new ConcurrentHashMap<>();
    private ConcurrentMap<Class,PersistenceHandler<?>> persistenceHandlers = new ConcurrentHashMap<>();

    public <M> void triggerCurrentUnlockState(M item) {
        getRewardManager(item.getClass()).triggerCurrentUnlockState(item);
    }

    private void addRewardManager(IRewardManager rewardManager){
        Class clazz = rewardManager.forClass();
        if (!rewardManagerExists(clazz)){
            rewardManager.bindPersistenceHandler(persistenceHandlers.get(clazz));
            rewardManagers.put(clazz, rewardManager);
        }
    }

    private boolean rewardManagerExists(Class clazz){
        return rewardManagers.containsKey(clazz);
    }

    private void removeRewardManager(IRewardManager rewardManager){
        Class clazz = rewardManager.forClass();
        if (rewardManagers.containsKey(clazz)){
            rewardManagers.remove(clazz);
        }
    }

    private <M> IRewardManager<M> getRewardManager(Class clazz){
        IRewardManager<M> rewardManager = (IRewardManager<M>) rewardManagers.get(clazz);
        return rewardManager;
    }

    public <M> void bindTrigger(Trigger<M> trigger,M item){
        Class clazz = item.getClass();
        if (!rewardManagerExists(clazz))
            addRewardManager(new RewardManager<M>(clazz));
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.bindTrigger(trigger, item);

    }

    public <M> void bindTrigger(Trigger<M> trigger,M... items){
        for (M item : items)
            bindTrigger(trigger,item);
    }

    public <M> void bindListener(RewardListener<M> trigger,M item){
        Class clazz = item.getClass();
        if (!rewardManagerExists(clazz))
            addRewardManager(new RewardManager<M>(clazz));
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.bindListener(trigger, item);
    }

    public <M> void bindListener(RewardListener<M> trigger,M... items){
        for (M item : items){
            bindListener(trigger,item);
        }
    }

     <M> void unlockSucceeded(Trigger<M> trigger) {
        IRewardManager<M> rewardManager = getRewardManager(trigger.forClass());
        rewardManager.unlockSucceeded(trigger);
    }

     <M> void unlockFailed(Trigger<M> trigger) {
        IRewardManager<M> rewardManager = getRewardManager(trigger.forClass());
        rewardManager.unlockFailed(trigger);
    }

    public <M> void unbindTriggers(Class<M> clazz) {
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindTriggers();
    }

    public <M> void unbindListeners(Class<M> clazz) {
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindListeners();
    }

    public void unbindAll(Class clazz){
        if (rewardManagers.containsKey(clazz)){
            IRewardManager rewardManager = getRewardManager(clazz);
            rewardManager.unbindTriggers();
            rewardManager.unbindListeners();
            rewardManagers.remove(clazz);
        }
    }

    public void unbindAll(Class... clazzes){
        for (Class clazz : clazzes)
            unbindAll(clazz);
    }

    public <M> void unbindTrigger(Trigger<M> trigger) {
        Class clazz = trigger.forClass();
        IRewardManager<M> rewardManager = getRewardManager(clazz);
        rewardManager.unbindTrigger(trigger);
    }

    public <M> void unbindTriggers(Class<M>... clazzes) {
        for (Class<M> clazz : clazzes){
          unbindTriggers(clazz);
        }
    }

    public <M> void unbindListeners(Class<M>... clazzes) {
        for (Class<M> clazz : clazzes){
            unbindListeners(clazz);
        }
    }

    public void unbindTriggers() {
        for (Map.Entry<Class,IRewardManager<?>> entry : rewardManagers.entrySet())
            entry.getValue().unbindTriggers();
        rewardManagers.clear();
    }

    public void unbindListeners() {
        for (Map.Entry<Class,IRewardManager<?>> entry : rewardManagers.entrySet())
            entry.getValue().unbindListeners();
        rewardManagers.clear();
    }
}
