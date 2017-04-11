package com.appzoneltd.lastmile.customer.features.notificationlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.serializers.JsonSetLoader;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.presentation.ViewModel;
import com.entities.Notification;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by Wafaa on 11/14/2016.
 */

public class NotificationFragmentViewModel extends ViewModel {

    int itemPosition;
    Notification notification;
    RecyclerView recyclerView;
    NotificationListAdapter notificationListAdapter;
    @Sync("notification_list")
    TreeSet<Notification> notifications;
    private boolean emptyMsgVisibility;


    public NotificationFragmentViewModel(ViewBinder viewBinder) {
        super(viewBinder);
        notificationListAdapter = new NotificationListAdapter();
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, View> createInvalidateCommands() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnInvalidateEmptyMsgCommand();
        commandExecutor.put((long) R.id.fragment_notification_empty_msg, command);
        command = createOnRecyclerViewInvalidatorCommand();
        commandExecutor.put((long) R.id.notification_list, command);
        command = createOnInvalidateLayoutCommand();
        commandExecutor.put((long) R.id.frame_notification_layout, command);

        return commandExecutor;
    }

    private Command<View, Void> createOnRecyclerViewInvalidatorCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                recyclerView = (RecyclerView) view;
                recyclerView.removeAllViews();
                LinearLayoutManager layoutManager =
                        new LinearLayoutManager(getFeature().getHostActivity());
                recyclerView.setLayoutManager(layoutManager);
                if(notifications.size() > 0){
                    notificationListAdapter.setup(notifications, recyclerView);
                    emptyMsgVisibility = false;
                    invalidateView(R.id.fragment_notification_empty_msg);
                } else {
                    emptyMsgVisibility = true;
                    invalidateView(R.id.fragment_notification_empty_msg);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateLayoutCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                try {
                    notificationListAdapter.invalidate(itemPosition,
                            new LinkedList<>(notifications));
                    emptyMsgVisibility = false;
                    invalidateView(R.id.fragment_notification_empty_msg);
                } catch (NotSavedInPreferencesException ex) {
                    emptyMsgVisibility = true;
                    invalidateView(R.id.fragment_notification_empty_msg);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateEmptyMsgCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View p) {
                TextView emptyMsg = (TextView) p;
                setViewVisibility(emptyMsg);
                return null;
            }

            private void setViewVisibility(View emptyMsg) {
                if (emptyMsg != null) {
                    if (emptyMsgVisibility) {
                        emptyMsg.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        emptyMsg.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }

    private TreeSet<Notification> retrieveListOfNotifications()
            throws NotSavedInPreferencesException {
        TreeSet<Notification> notifications;
        JsonSetLoader<Notification> loader =
                new JsonSetLoader<>(R.string.PREFS_KEY_NOTIFICATION_LIST);
        notifications = loader.execute(Notification.class);
        return notifications;
    }

    @Override
    public void onDestroy() {

    }
}
