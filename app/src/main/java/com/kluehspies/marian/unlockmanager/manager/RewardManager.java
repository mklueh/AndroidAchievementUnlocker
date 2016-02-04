package com.kluehspies.marian.unlockmanager.manager;

import android.support.annotation.NonNull;

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

    public static final String TRIGGERED_FROM_CODE = "TRIGGERED_FROM_CODE";
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
        Type type = persistenceHandler.isUnlocked(item) ? Type.SUCCEEDED : Type.FAILED;
        item = persistenceHandler.get(item);
        notifyRewardListeners(item, type, createFromCodeTrigger(item));
    }

    @NonNull
    private Trigger<M> createFromCodeTrigger(M item) {
        return new Trigger<>((Class<M>) item.getClass(), TRIGGERED_FROM_CODE);
    }

    @Override
    public void triggerUnlock(M item) {
        if (!persistenceHandler.isUnlocked(item)) {
            item = persistenceHandler.unlock(item, TRIGGERED_FROM_CODE);
            notifyRewardListeners(item, Type.SUCCEEDED, createFromCodeTrigger(item));
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
            if (!persistenceHandler.isUnlocked(item)) {
                item = notifyPersistenceHandler(item, type, trigger.getName());
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

    private M notifyPersistenceHandler(M item, Type type, String triggerName) {
        switch (type) {
            case SUCCEEDED:
                return persistenceHandler.unlock(item, triggerName);
            case FAILED:
                return persistenceHandler.lock(item, triggerName);
            default:
                throw new RuntimeException(String.format("Unknown Type: %s", type.toString()));
        }
    }

    enum Type {
        SUCCEEDED, FAILED
    }
}
