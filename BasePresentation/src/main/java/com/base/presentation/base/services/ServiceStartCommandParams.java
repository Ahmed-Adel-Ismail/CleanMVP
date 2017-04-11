package com.base.presentation.base.services;

import android.content.Intent;

/**
 * a parameters Object that is passed when {@link AbstractService#onStartCommand(Intent, int, int)}
 * is invoked
 * <p>
 * Created by Ahmed Adel on 1/16/2017.
 */
public class ServiceStartCommandParams {

    private Intent intent;
    private int flags;
    private int startId;
    private AbstractService service;

    public ServiceStartCommandParams(AbstractService service) {
        this.service = service;
    }

    void setFlags(int flags) {
        this.flags = flags;
    }

    void setIntent(Intent intent) {
        this.intent = intent;
    }

    void setStartId(int startId) {
        this.startId = startId;
    }

    public int getFlags() {
        return flags;
    }

    public Intent getIntent() {
        return intent;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractService> T getService() {
        return (T) service;
    }

    public int getStartId() {
        return startId;
    }
}
