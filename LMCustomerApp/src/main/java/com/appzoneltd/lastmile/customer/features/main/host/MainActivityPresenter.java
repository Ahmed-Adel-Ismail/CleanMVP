package com.appzoneltd.lastmile.customer.features.main.host;

import android.support.annotation.NonNull;
import android.view.Menu;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.annotations.MenuGroup;
import com.appzoneltd.lastmile.customer.features.main.models.MainModel;
import com.appzoneltd.lastmile.customer.firebase.NotificationCounterChanger;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.models.NullModel;

/**
 * Created by Wafaa on 12/1/2016.
 */

public class MainActivityPresenter extends
        Presenter<MainActivityPresenter, MainActivityViewModel, MainModel> {

    private final CommandExecutor<Long, Message> onNotificationReceivedExecutor;

    public MainActivityPresenter(MainActivityViewModel viewModel) {
        super(viewModel);
        onNotificationReceivedExecutor = new MainNotificationHandlerExecutor(this , getViewModel());
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnCreateMenuCommand();
        commandExecutor.put((long) R.id.onCreateOptionsMenu, command);
        command = createOnNotificationReceivedCommand();
        commandExecutor.put((long) R.id.onNotificationReceived, command);
        return commandExecutor;
    }

    @NonNull
    private Command<Message, Void> createOnCreateMenuCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Menu menu = message.getContent();
                setMenuGroupVisibility(menu);
                return null;
            }

            private void setMenuGroupVisibility(Menu menu) {
                Class<?> klass = getHostActivity().getClass();
                boolean visible = klass.isAnnotationPresent(MenuGroup.class);
                menu.setGroupVisible(R.id.pickup_menu_group, visible);
            }
        };
    }

    private Command<Message, Void> createOnNotificationReceivedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                onNotificationReceivedExecutor.execute(message.getId(), message);
                return null;
            }
        };
    }

}
