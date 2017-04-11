package com.appzoneltd.lastmile.customer.subfeatures.refresh;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;
import com.base.presentation.commands.DelayedCommand;
import com.base.presentation.listeners.OnEventListener;

/**
 * {@link ViewModel} that is responsible for showing and hiding the swipe refresh
 * Created by Wafaa on 11/9/2016.
 */

public abstract class SwipeRefreshViewModel extends ViewModel {

    private OnEventListener eventListener;
    private boolean refreshing;

    public SwipeRefreshViewModel(ViewBinder viewBinder) {
        super(viewBinder);
        eventListener = new OnEventListener(viewBinder.getHostActivity());
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnSwipeRefreshCommand();
        commandExecutor.put(getSwipeRefreshViewId(), command);
        return commandExecutor;
    }

    private DelayedCommand<SwipeRefreshViewModel, SwipeRefreshLayout> createOnSwipeRefreshCommand() {

        return new DelayedCommand<SwipeRefreshViewModel, SwipeRefreshLayout>(this) {
            @Override
            protected void delayedExecute(SwipeRefreshLayout swipeRefreshLayout) {
                swipeRefreshLayout.setOnRefreshListener(eventListener);
                swipeRefreshLayout.setRefreshing(refreshing);
            }

        };
    }

    public abstract long getSwipeRefreshViewId();

    void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }

    @Override
    public void onDestroy() {
        eventListener = null;
    }
}
