package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Marian on 19.01.2015.
 */
public final class RewardManager<M> implements IRewardManager {


    private ConcurrentMap<M, List<RewardListener<M>>> resourceListenerBindingMap = new ConcurrentHashMap<>();
    private ConcurrentMap<Trigger, List<M>> triggerResourceBindingMap = new ConcurrentHashMap<>();
    private List<Trigger> triggers = new ArrayList<>(5);

    /**
     * Bind RewardListener to resource
     *
     * @param rewardListener
     * @param resourceID
     */
    public void bindListener(RewardListener rewardListener, M resourceID) {
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
    public void bindListener(RewardListener rewardListener, M... resourceIDs) {
        for (M resourceID : resourceIDs) bindListener(rewardListener, resourceID);
    }

    /**
     * Bind trigger to a resource
     *
     * @param trigger
     * @param resourceID
     */
    public void bindTrigger(Trigger trigger, M resourceID) {
        if (!isRegistered(trigger))
            registerTrigger(trigger);
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
    public void bindTrigger(Trigger trigger, M... resourceIDs) {
        for (M resourceID : resourceIDs)
            bindTrigger(trigger, resourceID);
    }

    /**
     * Injects RewardManager instance into trigger
     *
     * @param trigger
     */
    private void registerTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    /**
     * unregisters a trigger
     * @param trigger
     */
    public void unregisterTrigger(Trigger trigger) {
        if (triggers.contains(trigger)) {
            triggers.remove(trigger);
        }
    }

    /**
     * Check if trigger is already registered
     *
     * @param trigger
     * @return
     */
    public boolean isRegistered(Trigger trigger) {
        for (Trigger unlockTrigger : triggers)
            if (unlockTrigger != null && unlockTrigger.equals(trigger))
                return true;
        return false;
    }

    @Override
    public void unlockNotAvailable(Trigger trigger) {
        notifyListeners(trigger, Type.NOT_AVAILABLE);
    }

    @Override
    public void unlockAvailable(Trigger trigger) {
        notifyListeners(trigger, Type.AVAILABLE);
    }

    @Override
    public void unlockSucceeded(Trigger trigger) {
        notifyListeners(trigger, Type.SUCCEEDED);
    }

    @Override
    public void unlockFailed(Trigger trigger) {
        notifyListeners(trigger, Type.FAILED);
    }

    /**
     * Notify all listeners,
     * which are matching the associated resIDs of the given trigger
     *
     * @param trigger
     * @param type
     */
    private void notifyListeners(Trigger trigger, Type type) {
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
