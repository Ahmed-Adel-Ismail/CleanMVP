package com.base.presentation.locations.google;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.locations.LocationConnection;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * a class that holds the implementation of tracking a location, connecting and disconnecting from
 * the google services
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public class GoogleLocationConnection implements LocationConnection {


    private final CheckedReference<Context> contextReference;
    private GoogleApiConnection googleApiConnection;
    private Disposable disposable;

    public GoogleLocationConnection(Context context) {
        contextReference = new CheckedReference<>(context);
        Logger.getInstance().error(getClass(), "<init> : " + context.getClass().getSimpleName());
    }

    @Override
    public LocationConnection execute(@NonNull LocationConnection.Callbacks callbacks)
            throws CheckedReferenceClearedException {
        Logger.getInstance().error(getClass(), "execute() : start");
        clear();
        Subject<Location> locationSubject = PublishSubject.create();
        disposable = createDisposable(locationSubject, callbacks);
        callbacks.clear();
        googleApiConnection = new GoogleApiConnection(locationSubject);
        googleApiConnection.execute(contextReference.get());
        Logger.getInstance().error(getClass(), "execute() : end");
        return this;
    }


    @NonNull
    private Disposable createDisposable(Subject<Location> subject, LocationConnection.Callbacks cb) {
        return subject.subscribe(cb.getOnLocationChanged(), cb.getOnError());
    }

    @Override
    public void clear() {
        Logger.getInstance().error(getClass(), "clear() : start");
        clearDisposable();
        clearGoogleApiConnection();
        Logger.getInstance().error(getClass(), "clear() : end");
    }

    private void clearDisposable() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
                Logger.getInstance().error(getClass(), "clearDisposable() : disposed");
            } else {
                Logger.getInstance().error(getClass(), "clearDisposable() : no disposable found");
            }
            disposable = null;
        }
    }

    private void clearGoogleApiConnection() {
        if (googleApiConnection != null) {
            googleApiConnection.clear();
            googleApiConnection = null;
        }
    }


}
