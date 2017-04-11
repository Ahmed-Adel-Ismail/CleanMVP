package com.appzoneltd.lastmile.customer.subfeatures.refresh;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Message;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.models.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Presenter} that is responsible for swipeToRefresh .
 * <br>
 * keep track of requests ids that is added to a {@code list}
 * when request fired and removed in responses by listening to {@code onRepositoryRequest},
 * {@code onRepositoryResponse} and {@code onRefresh}.
 * Created by Wafaa on 11/6/2016.
 */

public class SwipeRefreshPresenter<M extends Model>
        extends Presenter<SwipeRefreshPresenter<M>, SwipeRefreshViewModel, M> {


    private final Set<Long> registeredRequests;

    public SwipeRefreshPresenter(SwipeRefreshViewModel viewModel) {
        super(viewModel);
        registeredRequests = new HashSet<>();
        getViewModel().invalidateViews();
    }

    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnRepositoryRequestCommand();
        commandExecutor.put((long) R.id.onRepositoryRequest, command);
        command = createOnRepositoryResponse();
        commandExecutor.put((long) R.id.onRepositoryResponse, command);
        command = createOnRefreshCommand();
        commandExecutor.put((long) R.id.onRefresh, command);
        command = createOnPauseCommand();
        commandExecutor.put((long) R.id.onPause, command);
        command = createOnResumeCommand();
        commandExecutor.put((long) R.id.onResume, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                updateRefreshingState();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnRepositoryRequestCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                registeredRequests.add(message.getId());
                updateRefreshingState();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnRefreshCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                updateRefreshingState();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnRepositoryResponse() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                registeredRequests.remove(message.getId());
                updateRefreshingState();
                return null;
            }
        };
    }

    private void updateRefreshingState() {
        getViewModel().setRefreshing(!registeredRequests.isEmpty());
        getViewModel().invalidateViews();
    }

    private Command<Message, Void> createOnPauseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                getViewModel().setRefreshing(false);
                getViewModel().invalidateViews();
                return null;
            }
        };
    }

    @Override
    public void onDestroy() {
        registeredRequests.clear();
    }

}
