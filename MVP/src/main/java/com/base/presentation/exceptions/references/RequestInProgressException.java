package com.base.presentation.exceptions.references;

import com.base.abstraction.commands.Command;
import com.base.abstraction.system.AppResources;
import com.base.presentation.references.Requester;

/**
 * a {@link RuntimeException} that is thrown if an attempt to invoke
 * {@link Requester#onNext(Command)} or {@link Requester#onComplete(Command)} while
 * a request is currently processing
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class RequestInProgressException extends RuntimeException {
    public RequestInProgressException(long requestId) {
        super(AppResources.resourceEntryName((int) requestId));
    }
}
