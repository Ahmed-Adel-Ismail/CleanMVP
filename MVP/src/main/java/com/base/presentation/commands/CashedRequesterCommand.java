package com.base.presentation.commands;

import com.base.abstraction.observer.Observable;

/**
 * Created by Wafaa on 11/1/2016.
 */

public abstract class CashedRequesterCommand extends RequesterCommand{


    public CashedRequesterCommand(Observable hostObservable, long requestId) {
        super(hostObservable, requestId);
    }

    @Override
    final boolean isSuccessfulResponse() {
        return super.isSuccessfulResponse() && isDataCashed();
    }

    public abstract boolean isDataCashed();

    @Override
    public long getActorAddress() {
        return 0;
    }
}
