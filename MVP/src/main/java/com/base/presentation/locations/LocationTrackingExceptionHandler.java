package com.base.presentation.locations;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.exceptions.locations.LocationTrackingException;
import com.base.presentation.interfaces.ActivityHosted;

/**
 * the default {@link Command} that handles {@link LocationTracking} failure
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
public class LocationTrackingExceptionHandler implements Command<Throwable, Void> {

    private CheckedReference<AbstractActivity> activityReference;

    public LocationTrackingExceptionHandler(ActivityHosted activityHosted) {
        AbstractActivity activity = activityHosted.getHostActivity();
        this.activityReference = new CheckedReference<>(activity);
    }

    @Override
    public Void execute(Throwable throwable) {
        try {
            doExecute(throwable);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().error(getClass(), "activity cleared before execute() finishes");
        } catch (Throwable e) {
            new TestException().execute(e);
        }
        return null;
    }

    private void doExecute(Throwable throwable) throws Throwable {
        if (throwable instanceof LocationTrackingException) {
            ((LocationTrackingException) throwable).execute(activityReference.get());
        } else if (throwable instanceof CheckedReferenceClearedException) {
            Logger.getInstance().exception(throwable);
            throw throwable;
        } else {
            throw throwable;
        }
    }


}
