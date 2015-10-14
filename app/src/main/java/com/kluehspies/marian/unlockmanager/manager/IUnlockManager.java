package com.kluehspies.marian.unlockmanager.manager;

import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

/**
 * Created by Marian on 28.02.2015.
 */
public interface IUnlockManager {

    void unlockNotAvailable(IUnlockTrigger trigger);

    void unlockAvailable(IUnlockTrigger trigger);

    void unlockSucceeded(IUnlockTrigger trigger);

    void unlockFailed(IUnlockTrigger trigger);

}
