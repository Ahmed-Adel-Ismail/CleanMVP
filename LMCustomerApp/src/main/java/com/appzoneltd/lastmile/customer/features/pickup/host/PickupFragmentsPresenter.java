package com.appzoneltd.lastmile.customer.features.pickup.host;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.annotations.MenuGroup;
import com.appzoneltd.lastmile.customer.features.main.models.PickupLocationModel;
import com.appzoneltd.lastmile.customer.features.pickup.models.PickupModel;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.NotificationsItemDeletion;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingDialogHandler;
import com.appzoneltd.lastmile.customer.subfeatures.notificationcommands.RatingParamsGenerator;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.serializers.StringJsonParser;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.exceptions.OnBackPressException;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.usecases.events.ResponseMessage;
import com.entities.Notification;
import com.entities.cached.Rating;
import com.entities.cached.RatingRequestParams;

import java.io.Serializable;

/**
 * A {@link Presenter} to control the Fragments add & remove process
 * <p/>
 * Created by Wafaa on 9/25/2016.
 */
class PickupFragmentsPresenter
        extends Presenter<PickupFragmentsPresenter, PickupFragmentsViewModel, PickupModel> {


    private PickupState pickupState;
    private final CommandExecutor<Long, MenuItem> onMenuItemSelectedExecutor;
    private final CommandExecutor<Long, Message> onNotificationReceivedExecutor;

    PickupFragmentsPresenter(PickupFragmentsViewModel viewModel) {
        super(viewModel);
        Intent intent = getHostActivity().getIntent();
        if (intent != null) {
            initialize(intent);
        } else {
            logNullIntentError();
        }
        onMenuItemSelectedExecutor = createOnMenuItemSelectedCommandExecutor();
        onNotificationReceivedExecutor = createOnNotificationExecutor();
    }

    private void initialize(Intent intent) {
        exchangePickupModelIfRequired(intent);
        boolean pickupNow = intent.getBooleanExtra(AppResources.string(R.string.INTENT_KEY_PICKUP_NOW_ACTIVITY), true);
        pickupState = PickupState.getState(!pickupNow, getViewModel(), getModel());
        updatePickupLocation(intent);

    }


    private CommandExecutor<Long, MenuItem> createOnMenuItemSelectedCommandExecutor() {
        CommandExecutor<Long, MenuItem> commandExecutor = new CommandExecutor<>();
        Command<MenuItem, Void> command = createOnCancelItemSelectedCommand();
        commandExecutor.put((long) R.id.pickup_cancel_request, command);
        return commandExecutor;
    }

    private Command<MenuItem, Void> createOnCancelItemSelectedCommand() {
        return new Command<MenuItem, Void>() {
            @Override
            public Void execute(MenuItem p) {
                getHostActivity().finish();
                return null;
            }
        };
    }


    private void exchangePickupModelIfRequired(Intent intent) {
        Serializable s = intent.getSerializableExtra(AppResources.string(R.string.INTENT_KEY_PICKUP_DATA));
        if (s != null) {
            getHostActivity().exchangeModel((PickupModel) s);
        }
    }

    private void updatePickupLocation(Intent intent) {
        PickupLocationModel model;
        model = (PickupLocationModel) intent.getSerializableExtra(AppResources.string(R.string.INTENT_KEY_PICKUP_LOCATION_MODEL));
        if (model != null) {
            getLocationValuesAndSetInModel(model);
        } else {
            logNullPickupLocationModelError();
        }
    }

    private CommandExecutor<Long, Message> createOnNotificationExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnRatingNotification();
        commandExecutor.put((long) R.id.onNotifiedDriverRating, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnRatingNotification() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Notification notification = message.getContent();
                getModel().notification = notification;
                Rating rating = extractPayload(notification);
                Future<RatingRequestParams> future = new RatingDialogHandler()
                        .execute(getHostActivity());
                if (future != null) {
                    future.onComplete(createOnCompleteCommand(rating));
                }
                return null;
            }

            private Command<RatingRequestParams, Void> createOnCompleteCommand(final Rating rating) {
                return new Command<RatingRequestParams, Void>() {
                    @Override
                    public Void execute(RatingRequestParams message) {
                        if (message != null) {
                            getModel().rating = new RatingParamsGenerator(rating)
                                    .execute(message);
                            Event event = new Event.Builder(R.id.requestRating).build();
                            getModel().execute(event);
                        }
                        return null;
                    }
                };
            }

            private Rating extractPayload(Notification notification) {
                return new StringJsonParser<>(Rating.class).execute(notification.getPayload());
            }
        };
    }

    private void getLocationValuesAndSetInModel(PickupLocationModel model) {
        getModel().setPickupFormattedAddress(model.getFormattedAddress());
        getModel().setPickupDisplayedAddress(model.getDisplayedAddress());
        getModel().setPickupLatitude(model.getPickupLatitude());
        getModel().setPickupLongitude(model.getPickupLongitude());
    }

    private void logNullPickupLocationModelError() {
        SystemLogger.getInstance().error(getClass(), "getLocationValuesAndSetInModel() " +
                "no PickupLocationModel found in extras");
    }


    private void logNullIntentError() {
        SystemLogger.getInstance().error(getClass(), "null intent @ " +
                getClass().getSimpleName());
    }


    @NonNull
    protected CommandExecutor<Long, View> createOnClickCommandExecutor() {
        CommandExecutor<Long, View> commandExecutor = new CommandExecutor<>();
        Command<View, Void> command = createOnNextCommand();
        commandExecutor.put((long) R.id.pickup_scheduled_next_btn, command);
        commandExecutor.put((long) R.id.next_recipient_details, command);
        commandExecutor.put((long) R.id.pickup_next_review, command);
        commandExecutor.put((long) R.id.pickup_review_submit_request_btn, command);
        return commandExecutor;
    }

    private Command<View, Void> createOnNextCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (getSystemServices().isNetworkConnected()) {
                    updatePickupState();
                } else {
                    App.getInstance().getActorSystem()
                            .get((long) R.id.addressActivity)
                            .execute(createShowToastEvent());
                }
                return null;
            }

            private Event createShowToastEvent() {
                return new Event.Builder(R.id.showToast).message(new Message.Builder()
                        .content(R.string.connection_error_msg).build()).build();
            }

            private void updatePickupState() {
                if (pickupState.hasNext()) {
                    pickupState = pickupState.next();
                }
            }
        };
    }


    @Override
    protected CommandExecutor<Long, Message> createCommandExecutor() {
        CommandExecutor<Long, Message> commandExecutor = new CommandExecutor<>();
        Command<Message, Void> command = createOnDialogButtonClickedCommand();
        commandExecutor.put((long) R.id.onDialogSubmitPickupRequest, command);
        command = createOnBackPressedCommand();
        commandExecutor.put((long) R.id.onBackPressed, command);
        command = createOnCreateMenuCommand();
        commandExecutor.put((long) R.id.onCreateOptionsMenu, command);
        command = createOnMenuItemSelectedCommand();
        commandExecutor.put((long) R.id.onOptionItemSelected, command);
        command = createOnNotificationReceived();
        commandExecutor.put((long) R.id.onNotificationReceived, command);
        return commandExecutor;
    }

    private Command<Message, Void> createOnDialogButtonClickedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                if (id == R.id.onDialogPositiveClick) {
                    notifyFragmentWithClick();
                    requestCreatePickupRequest();
                }
                return null;
            }

            private void requestCreatePickupRequest() {
                Event event = new Event.Builder(R.id.requestSubmitPickupRequest).build();
                getModel().execute(event);
            }

            private void notifyFragmentWithClick() {
                Event event = new Event.Builder(R.id.onDialogPositiveClick).build();
                notifyObservers(event);
            }
        };
    }

    private Command<Message, Void> createOnBackPressedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                if (pickupState.hasBack()) {
                    pickupState = pickupState.back();
                    throw new OnBackPressException();
                } else {
                    sendModelBackToBeReusedLater();
                }
                return null;
            }

            private void sendModelBackToBeReusedLater() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.FINISH);
                request.codeCancel();
                request.extra(R.string.INTENT_KEY_PICKUP_DATA, getModel());
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(getHostActivity()));
            }
        };
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

    private Command<Message, Void> createOnMenuItemSelectedCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                MenuItem item = message.getContent();
                onMenuItemSelectedExecutor.execute(id, item);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnNotificationReceived() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                Message msg = message.getContent();
                onNotificationReceivedExecutor.execute(message.getId(), msg);
                return null;
            }
        };
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnCreateRequestResponse();
        commandExecutor.put((long) R.id.requestCreatePickupRequest, command);
        commandExecutor.put((long) R.id.requestRating, createOnRatingResponseCommand());
        return super.createResponseCommands();
    }

    private Command<ResponseMessage, Void> createOnCreateRequestResponse() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage p) {
                if (p.isSuccessful()) {
                    getHostActivity().finish();
                } else {
                    logFailureAndShowToast(p);
                }
                return null;
            }

            private void logFailureAndShowToast(ResponseMessage p) {
                SystemLogger.getInstance().error(PickupFragmentsPresenter.class,
                        "requestCreatePickupRequest failed : " + p.getContent());

                Toast.makeText(getHostActivity(),
                        AppResources.string(R.string.failed_to_submit_request),
                        Toast.LENGTH_SHORT).show();
            }

        };
    }

    private Command<ResponseMessage, Void> createOnRatingResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                if (message.isSuccessful()) {
                    new NotificationsItemDeletion<>(getModel().notification).execute(Notification.class);
                    notifyToShowToast(R.string.rating_sucess_response_msg);
                } else {
                    notifyToShowToast(R.string.rating_failed_response_msg);
                }
                finishActivity();
                return null;
            }

            private void notifyToShowToast(int msgResource) {
                Event event = new Event.Builder(R.id.showToast).message(new Message.Builder()
                        .content(msgResource).build()).build();
                App.getInstance().getActorSystem()
                        .get((long) R.id.addressActivity)
                        .execute(event);
            }

            private void finishActivity() {
                ActivityActionRequest request = new ActivityActionRequest(ActionType.FINISH);
                EventBuilder eventBuilder = new EventBuilder(R.id.startActivityAction, request);
                getFeature().startActivityActionRequest(eventBuilder.execute(PickupFragmentsPresenter.this));
            }
        };
    }

    @Override
    public void onDestroy() {
        pickupState.clear();
        pickupState = null;
    }
}
