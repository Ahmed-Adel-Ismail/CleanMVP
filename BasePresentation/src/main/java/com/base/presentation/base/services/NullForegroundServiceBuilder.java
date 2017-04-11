package com.base.presentation.base.services;

import android.app.Notification;

import com.base.presentation.annotations.interfaces.Id;

/**
 * an Object that represents {@code null} {@link ForegroundServiceBuilder}
 * <p>
 * Created by Ahmed Adel on 1/17/2017.
 */
@Id(FakeService.NOTIFICATION_ID)
public class NullForegroundServiceBuilder extends ForegroundServiceBuilder {

    @Override
    public Notification execute(AbstractService<?> service) {
        return null;
    }
}
