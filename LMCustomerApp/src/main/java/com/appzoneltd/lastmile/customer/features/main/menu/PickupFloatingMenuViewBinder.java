package com.appzoneltd.lastmile.customer.features.main.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.subfeatures.menus.FloatingMenuViewModel;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Message;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.models.NullModel;

import butterknife.BindView;

/**
 * The floating menu {@link ViewBinder} for
 * pickup options in Home screen
 * <p/>
 * Created by Ahmed Adel on 9/5/2016.
 */
@BindLayout(R.layout.activity_floating_menu)
class PickupFloatingMenuViewBinder extends LastMileViewBinder<NullModel> {


    @BindView(R.id.floating_menu)
    LinearLayout floatingMenuLayout;
    @BindView(R.id.floating_menu_parent)
    FrameLayout floatingMenuItemLayout;

    private final CommandExecutor<Long, View> onClickCommandExecutor = new CommandExecutor<>();

    @Override
    public void onDestroy() {
        onClickCommandExecutor.clear();
        super.onDestroy();
    }

    public PickupFloatingMenuViewBinder(Feature<NullModel> feature) {
        super(feature);
        fillSubClassCommands();
        fillOnClickCommandsExecutor();
    }

    private void fillSubClassCommands() {
        Command<Message, Void> command = createOnClickCommand();
        addCommand((long) R.id.onClick, command);
    }

    @NonNull
    private Command<Message, Void> createOnClickCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                View view = message.getContent();
                onClickCommandExecutor.execute(id, view);
                return null;
            }
        };
    }

    private void fillOnClickCommandsExecutor() {
        Command<View, Void> command = createOutsideFloatingMenuOnClickCommand();
        onClickCommandExecutor.put((long) R.id.floating_menu_parent, command);
    }

    private Command<View, Void> createOutsideFloatingMenuOnClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                Activity activity = getHostActivity();
                if (activity != null) {
                    activity.finish();
                }
                return null;
            }
        };
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        FloatingMenuViewModel floatingMenuViewModel = new FloatingMenuViewModel(this, floatingMenuLayout);
        addEventsSubscriber(new PickupFloatingMenuPresenter(this, floatingMenuViewModel));
    }


}
