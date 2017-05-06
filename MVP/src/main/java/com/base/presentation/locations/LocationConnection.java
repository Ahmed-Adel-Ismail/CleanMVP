package com.base.presentation.locations;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.RxCommand;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.interfaces.Clearable;
import com.base.presentation.locations.google.GoogleLocationConnection;

/**
 * a class that indicates an open connection to location updates, you can start
 * connection by invoking {@link #execute(LocationConnection.Callbacks)}, then you should
 * stop this connection manually by invoking {@link #clear()}
 * <p>
 * this is a proxy interface to {@link GoogleLocationConnection}
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
public interface LocationConnection extends
        Command<LocationConnection.Callbacks, LocationConnection>,
        Clearable {

    /**
     * start a connection to location tracking service, you should stop this connection
     * manually by invoking {@link #clear()}
     *
     * @param callbacks the {@link LocationConnection.Callbacks}
     * @return {@code this} instance after connecting
     * @throws CheckedReferenceClearedException if the {@link Context} hosting this class is not
     *                                          available any more
     */
    @Override
    LocationConnection execute(@NonNull LocationConnection.Callbacks callbacks)
            throws CheckedReferenceClearedException;

    /**
     * a class that holds the callbacks for {@link GoogleLocationConnection} which is used by
     * {@link LocationConnection} ... notice that this class instance is cleared once
     * {@link LocationConnection#execute(LocationConnection.Callbacks)} is invoked, it is
     * not usable after invoking this method
     * <p>
     * Created by Ahmed Adel on 12/22/2016.
     */
    class Callbacks implements Clearable {

        private RxCommand<Location> onLocationChanged;
        private RxCommand<Throwable> onError;


        /**
         * initialize a {@link LocationConnection.Callbacks} instance
         *
         * @param onLocationChanged the {@link RxCommand} that will be executed when the location
         *                          is changed
         * @param onError           the {@link RxCommand} that will be invoked if an error happened
         */
        public Callbacks(@NonNull RxCommand<Location> onLocationChanged,
                         RxCommand<Throwable> onError) {
            this.onError = onError;
            this.onLocationChanged = onLocationChanged;
        }


        public RxCommand<Throwable> getOnError() {
            return onError;
        }

        public RxCommand<Location> getOnLocationChanged() {
            return onLocationChanged;
        }

        @Override
        public void clear() {
            onLocationChanged = null;
            onError = null;
        }
    }

}
