package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.trigger.ITrigger;

/**
 * Created by Marian on 28.02.2015.
 */
public interface IRewardManager {

    void unlockNotAvailable(ITrigger trigger);

    void unlockAvailable(ITrigger trigger);

    void unlockSucceeded(ITrigger trigger);

    void unlockFailed(ITrigger trigger);

}
