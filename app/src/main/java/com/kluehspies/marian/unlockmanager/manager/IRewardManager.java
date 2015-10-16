package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 28.02.2015.
 */
public interface IRewardManager {

    void unlockNotAvailable(Trigger trigger);

    void unlockAvailable(Trigger trigger);

    void unlockSucceeded(Trigger trigger);

    void unlockFailed(Trigger trigger);

}
