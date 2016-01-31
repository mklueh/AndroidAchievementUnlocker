package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Marian Klühspies on 19.01.2015.
 */
public final class RewardManager<M> implements IRewardManager<M> {

    private ConcurrentMap<Trigger<M>, List<M>> triggerResourceBindingMap = new ConcurrentHashMap<>();
    private ConcurrentMap<M, List<RewardListener<M>>> resourceListenerBindingMap = new ConcurrentHashMap<>();
    private Class clazz;
    private PersistenceHandler<M> persistenceHandler;

    public RewardManager(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * Bind RewardListener to resource
     *
     * @param rewardListener
     * @paraM item
     */
    public void bindListener(RewardListener<M> rewardListener, M item) {
        if (!resourceListenerBindingMap.containsKey(item))
            resourceListenerBindingMap.put(item, new ArrayList<RewardListener<M>>());
        resourceListenerBindingMap.get(item).add(rewardListener);
    }

    /**
     * Bind RewardListener to multiple resources
     *
     * @param rewardListener
     * @paraM items
     */
    public void bindListener(RewardListener<M> rewardListener, M... resourceIDs) {
        for (M item : resourceIDs) bindListener(rewardListener, item);
    }

    @Override
    public void unbindTriggers() {
        for (Map.Entry<Trigger<M>, List<M>> triggerEntry : triggerResourceBindingMap.entrySet()) {
            if (triggerEntry.getValue() != null)
                triggerEntry.getValue().clear();
        }
        triggerResourceBindingMap.clear();
    }

    @Override
    public void unbindListeners() {
        for (Map.Entry<M, List<RewardListener<M>>> triggerEntry : resourceListenerBindingMap.entrySet()) {
            if (triggerEntry.getValue() != null)
                triggerEntry.getValue().clear();
        }
        resourceListenerBindingMap.clear();
    }

    @Override
    public void unbindTrigger(Trigger<M> trigger) {
        if (triggerResourceBindingMap.containsKey(trigger)) {
            triggerResourceBindingMap.get(trigger).clear();
            triggerResourceBindingMap.remove(trigger);
        }
    }

    @Override
    public void unbindListener(RewardListener<M> rewardListener, M item) {
        if (resourceListenerBindingMap.containsKey(item)) {
            List<RewardListener<M>> rewardListeners = resourceListenerBindingMap.get(item);
            if (rewardListeners.contains(rewardListener))
                rewardListeners.remove(rewardListener);
            if (rewardListeners.size() == 0)
                resourceListenerBindingMap.remove(item);
        }
    }

    @Override
    public void unbindListener(RewardListener<M> rewardListener, M... items) {
        for (M item : items) {
            unbindListener(rewardListener, item);
        }
    }

    @Override
    public void unbindListeners(M item) {
        if (resourceListenerBindingMap.containsKey(item)) {
            List<RewardListener<M>> rewardListeners = resourceListenerBindingMap.get(item);
            rewardListeners.clear();
            resourceListenerBindingMap.remove(item);
        }
    }

    @Override
    public void bindPersistenceHandler(PersistenceHandler<M> persistenceHandler) {
        this.persistenceHandler = persistenceHandler;
    }

    @Override
    public void triggerCurrentUnlockState(M item) {
        if (persistenceHandler != null) {
            Type type = persistenceHandler.isUnlocked(item) ? Type.SUCCEEDED : Type.FAILED;
            notifyRewardListeners(item, type, persistenceHandler);
        }
    }

    /**
     * Bind trigger to a resource
     *
     * @param trigger
     * @paraM item
     */
    public void bindTrigger(Trigger<M> trigger, M item) {
        if (!triggerResourceBindingMap.containsKey(trigger))
            triggerResourceBindingMap.put(trigger, new ArrayList<M>());
        triggerResourceBindingMap.get(trigger).add(item);
    }


    /**
     * Bind trigger to multiple resources
     *
     * @param trigger
     * @paraM items
     */
    public void bindTrigger(Trigger<M> trigger, M... resourceIDs) {
        for (M item : resourceIDs)
            bindTrigger(trigger, item);
    }

    @Override
    public void unlockSucceeded(Trigger<M> trigger) {
        notifyListeners(trigger, Type.SUCCEEDED);
    }

    @Override
    public void unlockFailed(Trigger<M> trigger) {
        notifyListeners(trigger, Type.FAILED);
    }

    @Override
    public Class forClass() {
        return this.clazz;
    }

    /**
     * Notify all listeners,
     * which are matching the associated resIDs of the given trigger
     *
     * @param trigger
     * @param type
     */
    private void notifyListeners(Trigger<M> trigger, Type type) {
        List<M> resourceIDs = triggerResourceBindingMap.get(trigger);
        for (M item : resourceIDs) {
            if (!persistenceHandler.wasUnlockedPreviously(item)) {
                notifyPersistenceHandler(item, type, trigger);
                notifyRewardListeners(item, type, trigger);
            }
        }
    }

    private void notifyRewardListeners(M item, Type type, Trigger<M> trigger) {
        List<RewardListener<M>> rewardListeners = resourceListenerBindingMap.get(item);
        if (rewardListeners != null)
            for (RewardListener<M> listener : rewardListeners)
                if (listener != null)
                    switch (type) {
                        case SUCCEEDED:
                            listener.unlockSucceeded(item, trigger);
                            break;
                        case FAILED:
                            listener.unlockFailed(item, trigger);
                            break;
                    }
    }

    private void notifyPersistenceHandler(M item, Type type, Trigger<M> trigger) {
        if (persistenceHandler != null) {
            switch (type) {
                case SUCCEEDED:
                    persistenceHandler.unlockSucceeded(item, trigger);
                    break;
                case FAILED:
                    persistenceHandler.unlockFailed(item, trigger);
                    break;
            }
        }
    }

    @Override
    public boolean hasValidPersistenceHandler(){
        return persistenceHandler != null;
    }

    enum Type {
        SUCCEEDED, FAILED
    }
}
