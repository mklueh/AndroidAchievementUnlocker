package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.ITrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Marian on 19.01.2015.
 */
public final class RewardManager implements IRewardManager {


    private ConcurrentMap<Integer, List<RewardListener>> resourceListenerBindingMap = new ConcurrentHashMap<>();
    private ConcurrentMap<ITrigger, List<Integer>> triggerResourceBindingMap = new ConcurrentHashMap<>();
    private List<ITrigger> triggers = new ArrayList<>(5);

    /**
     * Bind RewardListener to resource
     *
     * @param rewardListener
     * @param resourceID
     */
    public void bindListener(RewardListener rewardListener, int resourceID) {
        if (!resourceListenerBindingMap.containsKey(resourceID))
            resourceListenerBindingMap.put(resourceID, new ArrayList<RewardListener>());
        resourceListenerBindingMap.get(resourceID).add(rewardListener);
    }

    /**
     * Bind RewardListener to multiple resources
     *
     * @param rewardListener
     * @param resourceIDs
     */
    public void bindListener(RewardListener rewardListener, int... resourceIDs) {
        for (int resourceID : resourceIDs) bindListener(rewardListener, resourceID);
    }

    /**
     * Bind trigger to a resource
     *
     * @param trigger
     * @param resourceID
     */
    public void bindTrigger(ITrigger trigger, int resourceID) {
        if (!isRegistered(trigger))
            registerTrigger(trigger);
        if (!triggerResourceBindingMap.containsKey(trigger))
            triggerResourceBindingMap.put(trigger, new ArrayList<Integer>());
        triggerResourceBindingMap.get(trigger).add(resourceID);
    }


    /**
     * Bind trigger to multiple resources
     *
     * @param trigger
     * @param resourceIDs
     */
    public void bindTrigger(ITrigger trigger, int... resourceIDs) {
        for (int resourceID : resourceIDs) bindTrigger(trigger, resourceID);
    }

    /**
     * Injects RewardManager instance into trigger
     *
     * @param trigger
     */
    private void registerTrigger(ITrigger trigger) {
        trigger.setUnlockManager(this);
        triggers.add(trigger);
    }

    /**
     * unregisters a trigger
     * @param trigger
     */
    public void unregisterTrigger(ITrigger trigger) {
        if (triggers.contains(trigger)) {
            trigger.setUnlockManager(null);
            triggers.remove(trigger);
        }
    }

    /**
     * Check if trigger is already registered
     *
     * @param trigger
     * @return
     */
    public boolean isRegistered(ITrigger trigger) {
        for (ITrigger unlockTrigger : triggers)
            if (unlockTrigger != null && unlockTrigger.equals(trigger))
                return true;
        return false;
    }


    @Override
    public void unlockNotAvailable(ITrigger trigger) {
        notifyListeners(trigger, Type.NOT_AVAILABLE);
    }

    @Override
    public void unlockAvailable(ITrigger trigger) {
        notifyListeners(trigger, Type.AVAILABLE);
    }

    @Override
    public void unlockSucceeded(ITrigger trigger) {
        notifyListeners(trigger, Type.SUCCEEDED);
    }

    @Override
    public void unlockFailed(ITrigger trigger) {
        notifyListeners(trigger, Type.FAILED);
    }

    /**
     * Notify all listeners,
     * which are matching the associated resIDs of the given trigger
     *
     * @param trigger
     * @param type
     */
    private void notifyListeners(ITrigger trigger, Type type) {
        List<Integer> resourceIDs = triggerResourceBindingMap.get(trigger);
        for (Integer resourceID : resourceIDs) {
            List<RewardListener> rewardListeners = resourceListenerBindingMap.get(resourceID);
            if (rewardListeners != null)
                for (RewardListener listener : rewardListeners)
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
