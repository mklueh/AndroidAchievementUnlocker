package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Marian on 19.01.2015.
 */
public final class RewardManager<M> implements IRewardManager<M> {

    private ConcurrentMap<Trigger<M>, List<M>> triggerResourceBindingMap = new ConcurrentHashMap<>();
    private ConcurrentMap<M, List<RewardListener<M>>> resourceListenerBindingMap = new ConcurrentHashMap<>();


    private Class clazz;
    public RewardManager(Class clazz){
        this.clazz = clazz;
    }

    /**
     * Bind RewardListener to resource
     *
     * @param rewardListener
     * @param resourceID
     */
    public void bindListener(RewardListener<M> rewardListener, M resourceID) {
        if (!resourceListenerBindingMap.containsKey(resourceID))
            resourceListenerBindingMap.put(resourceID, new ArrayList<RewardListener<M>>());
        resourceListenerBindingMap.get(resourceID).add(rewardListener);
    }

    /**
     * Bind RewardListener to multiple resources
     *
     * @param rewardListener
     * @param resourceIDs
     */
    public void bindListener(RewardListener<M> rewardListener, M... resourceIDs) {
        for (M resourceID : resourceIDs) bindListener(rewardListener, resourceID);
    }

    @Override
    public void unbindTriggers() {
        for (Map.Entry<Trigger<M>,List<M>> triggerEntry : triggerResourceBindingMap.entrySet()){
            if (triggerEntry.getValue() != null)
                triggerEntry.getValue().clear();
        }
        triggerResourceBindingMap.clear();
    }

    @Override
    public void unbindListeners() {
        for (Map.Entry<M,List<RewardListener<M>>> triggerEntry : resourceListenerBindingMap.entrySet()){
            if (triggerEntry.getValue() != null)
                triggerEntry.getValue().clear();
        }
        resourceListenerBindingMap.clear();
    }

    @Override
    public void unbindTrigger(Trigger<M> trigger) {
        if (triggerResourceBindingMap.containsKey(trigger)){
            triggerResourceBindingMap.get(trigger).clear();
            triggerResourceBindingMap.remove(trigger);
        }
    }

    @Override
    public void unbindListener(RewardListener<M> rewardListener, M item) {
        if (resourceListenerBindingMap.containsKey(item)){
            List<RewardListener<M>> rewardListeners = resourceListenerBindingMap.get(item);
            if (rewardListeners.contains(rewardListener))
                rewardListeners.remove(rewardListener);
            if (rewardListeners.size() == 0)
                resourceListenerBindingMap.remove(item);
        }
    }

    @Override
    public void unbindListener(RewardListener<M> rewardListener, M... items) {
        for (M item : items){
            unbindListener(rewardListener,item);
        }
    }

    @Override
    public void unbindListeners(M item) {
        if (resourceListenerBindingMap.containsKey(item)){
            List<RewardListener<M>> rewardListeners = resourceListenerBindingMap.get(item);
            rewardListeners.clear();
            resourceListenerBindingMap.remove(item);
        }
    }

    /**
     * Bind trigger to a resource
     *
     * @param trigger
     * @param resourceID
     */
    public void bindTrigger(Trigger<M> trigger, M resourceID) {
        if (!triggerResourceBindingMap.containsKey(trigger))
            triggerResourceBindingMap.put(trigger, new ArrayList<M>());
        triggerResourceBindingMap.get(trigger).add(resourceID);
    }


    /**
     * Bind trigger to multiple resources
     *
     * @param trigger
     * @param resourceIDs
     */
    public void bindTrigger(Trigger<M> trigger, M... resourceIDs) {
        for (M resourceID : resourceIDs)
            bindTrigger(trigger, resourceID);
    }

    @Override
    public void unlockNotAvailable(Trigger<M> trigger) {
        notifyListeners(trigger, Type.NOT_AVAILABLE);
    }

    @Override
    public void unlockAvailable(Trigger<M> trigger) {
        notifyListeners(trigger, Type.AVAILABLE);
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
        for (M resourceID : resourceIDs) {
            List<RewardListener<M>> rewardListeners = resourceListenerBindingMap.get(resourceID);
            if (rewardListeners != null)
                for (RewardListener<M> listener : rewardListeners)
                    if (listener != null)
                        switch (type) {
                            case SUCCEEDED:
                                listener.unlockSucceeded(resourceID, trigger);
                                break;
                            case FAILED:
                                listener.unlockFailed(resourceID, trigger);
                                break;
                            case AVAILABLE:
                                listener.rewardAvailable(resourceID, trigger);
                                break;
                            case NOT_AVAILABLE:
                                listener.rewardNotAvailable(resourceID, trigger);
                                break;
                        }
        }
    }

    enum Type {
        SUCCEEDED, FAILED, AVAILABLE, NOT_AVAILABLE
    }
}
