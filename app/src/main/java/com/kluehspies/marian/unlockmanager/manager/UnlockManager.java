package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.listener.UnlockListener;
import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Marian on 19.01.2015.
 */
public final class UnlockManager implements IUnlockManager {


    private ConcurrentMap<Integer, List<UnlockListener>> resourceListenerBindingMap = new ConcurrentHashMap<>();
    private ConcurrentMap<IUnlockTrigger, List<Integer>> triggerResourceBindingMap = new ConcurrentHashMap<>();
    private List<IUnlockTrigger> triggers = new ArrayList<>(5);

    /**
     * Bind UnlockListener to resource
     *
     * @param unlockListener
     * @param resourceID
     */
    public void bindListener(UnlockListener unlockListener, int resourceID) {
        if (!resourceListenerBindingMap.containsKey(resourceID))
            resourceListenerBindingMap.put(resourceID, new ArrayList<UnlockListener>());
        resourceListenerBindingMap.get(resourceID).add(unlockListener);
    }

    /**
     * Bind UnlockListener to multiple resources
     *
     * @param unlockListener
     * @param resourceIDs
     */
    public void bindListener(UnlockListener unlockListener, int... resourceIDs) {
        for (int resourceID : resourceIDs) bindListener(unlockListener, resourceID);
    }

    /**
     * Bind trigger to a resource
     *
     * @param trigger
     * @param resourceID
     */
    public void bindTrigger(IUnlockTrigger trigger, int resourceID) {
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
    public void bindTrigger(IUnlockTrigger trigger, int... resourceIDs) {
        for (int resourceID : resourceIDs) bindTrigger(trigger, resourceID);
    }

    /**
     * Injects UnlockManager instance into trigger
     *
     * @param trigger
     */
    private void registerTrigger(IUnlockTrigger trigger) {
        trigger.setUnlockManager(this);
        triggers.add(trigger);
    }

    /**
     * Check if trigger is already registered
     *
     * @param trigger
     * @return
     */
    private boolean isRegistered(IUnlockTrigger trigger) {
        for (IUnlockTrigger unlockTrigger : triggers)
            if (unlockTrigger != null && unlockTrigger.equals(trigger))
                return true;
        return false;
    }


    @Override
    public void unlockNotAvailable(IUnlockTrigger trigger) {
        notifyListeners(trigger, Type.NOT_AVAILABLE);
    }

    @Override
    public void unlockAvailable(IUnlockTrigger trigger) {
        notifyListeners(trigger, Type.AVAILABLE);
    }

    @Override
    public void unlockSucceeded(IUnlockTrigger trigger) {
        notifyListeners(trigger, Type.SUCCEEDED);
    }

    @Override
    public void unlockFailed(IUnlockTrigger trigger) {
        notifyListeners(trigger, Type.FAILED);
    }

    /**
     * Notify all listeners,
     * which are matching the associated resIDs of the given trigger
     *
     * @param trigger
     * @param type
     */
    private void notifyListeners(IUnlockTrigger trigger, Type type) {
        List<Integer> resourceIDs = triggerResourceBindingMap.get(trigger);
        for (Integer resourceID : resourceIDs) {
            List<UnlockListener> unlockListeners = resourceListenerBindingMap.get(resourceID);
            if (unlockListeners != null)
                for (UnlockListener listener : unlockListeners)
                    if (listener != null)
                        switch (type) {
                            case SUCCEEDED:
                                listener.unlockSucceeded(resourceID, trigger);
                                break;
                            case FAILED:
                                listener.unlockFailed(resourceID, trigger);
                                break;
                            case AVAILABLE:
                                listener.unlockAvailable(resourceID, trigger);
                                break;
                            case NOT_AVAILABLE:
                                listener.unlockNotAvailable(resourceID, trigger);
                                break;
                        }
        }
    }

    enum Type {
        SUCCEEDED, FAILED, AVAILABLE, NOT_AVAILABLE
    }
}
