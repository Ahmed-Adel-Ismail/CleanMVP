package com.appzoneltd.lastmile.customer.features.login;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.abstracts.LastMileViewBinder;
import com.appzoneltd.lastmile.customer.deprecated.utilities.UIManager;
import com.appzoneltd.lastmile.customer.deprecatred.SharedManager;
import com.appzoneltd.lastmile.customer.features.main.host.MainActivity;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.events.Message;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.BindLayout;
import com.base.presentation.base.abstracts.features.Feature;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.receivers.NetworkStateReceiver;
import com.base.presentation.requests.ActionType;
import com.base.presentation.requests.ActivityActionRequest;
import com.base.presentation.requests.ActivityResult;
import com.base.presentation.validators.EmailValidator;
import com.base.presentation.validators.PasswordValidator;
import com.base.usecases.events.ResponseMessage;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;

/**
 * a {@link ViewBinder} for Login Screen
 * <p/>
 * Created by Ahmed Adel on 9/4/2016.
 *
 * @deprecated old implementation
 */
@BindLayout(R.layout.activity_login)
class LoginViewBinder extends LastMileViewBinder<LoginModel> {


    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.username)
    EditText etUserName;
    @BindView(R.id.password)
    EditText etPassword;
    @BindDrawable(R.drawable.error_user)
    Drawable errorUser;
    @BindDrawable(R.drawable.error_lock)
    Drawable errorLock;
    @BindDrawable(R.drawable.selector_username_icon)
    Drawable selectorUsername;
    @BindDrawable(R.drawable.selector_pasword_icon)
    Drawable selectorPassword;
    @BindView(R.id.username_error)
    TextView usernameError;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.login_layout)
    ViewGroup loginLayout;
    @BindView(R.id.activity_login_internet_error)
    ViewGroup activityLoginInternetError;
    @BindView(R.id.activity_login_fields_layout)
    ViewGroup activityLoginFieldsLayout;

    @BindString(R.string.username_error_msg)
    String usernameErrorMsg;
    @BindString(R.string.password_error_msg)
    String passwordErrorMsg;
    @BindString(R.string.username_missing_msg)
    String usernameMissingMsg;
    @BindString(R.string.password_missing_msg)
    String passwordMissingMsg;
    @BindString(R.string.sign_in)
    String singIn;
    @BindView(R.id.password_error)
    TextView passwordError;

    private final int BAD_REQUEST_STATUS_CODE = 400;
    private final int NO_INTERNET_CONNECTION = -1;
    private final CommandExecutor<Long, View> onClickCommandExecutor = new CommandExecutor<>();
    private final CommandExecutor<Long, ResponseMessage> onResponseCommandExecutor;
    private String userName;
    private String password;
    private LoginModel loginModel;


    LoginViewBinder(Feature<LoginModel> feature) {
        super(feature);
        Activity activity = getHostActivity();
        Window window = activity.getWindow();
        window.setBackgroundDrawable(ResourcesCompat.getDrawable(AppResources.getResources(),
                R.drawable.background, null));
        fillSubClassCommands();
        fillOnClickCommandsExecutor();
        onResponseCommandExecutor = createOnResponseCommandExecutor();
        addEventsSubscriber(new NetworkStateReceiver(getFeature()));
    }

    @Override
    public void initializeAfterBindingViews(Bundle savedInstanceState) {
        loginModel = new LoginModel();
        loginModel.initialize(this);

        UIManager.getInstance(getHostActivity()).underline(forgetPassword);
        etUserName.requestFocus();
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    userName = etUserName.getText().toString();
                    userName = userName.toLowerCase();
                    password = etPassword.getText().toString().trim();
                    requestLogin(userName, password);
                    return true;
                }
                return false;
            }
        });
    }


    private void fillSubClassCommands() {
        Command<Message, Void> command;
        command = createOnResumeCommand();
        addCommand((long) R.id.onResume, command);
        command = createOnClickCommand();
        addCommand((long) R.id.onClick, command);
        command = createOnActivityResultCommand();
        addCommand((long) R.id.onActivityResult, command);
        command = createOnResponseCommand();
        addCommand((long) R.id.onRepositoryResponse, command);
        command = createOnNetWorkChangeListener();
        addCommand((long) R.id.onNetworkStateChanged, command);
    }

    private Command<Message, Void> createOnResumeCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                showErrorConnection(getHostActivity().getSystemServices().isNetworkConnected());
                return null;
            }
        };
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

    private Command<Message, Void> createOnActivityResultCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                ActivityResult result = message.getContent();
                if (result.requestCode == SharedManager.CONNECTION_REQUEST_CODE) {
                    retryIfRequired(result);
                }
                return null;
            }

            private void retryIfRequired(ActivityResult result) {
                if (result.isResultCodeOk()) {
                    login();
                }
            }
        };
    }

    private void fillOnClickCommandsExecutor() {
        Command<View, Void> command = createLoginButtonOnClick();
        onClickCommandExecutor.put((long) R.id.login, command);
        command = createOnRetryClickCommand();
        onClickCommandExecutor.put((long) R.id.network_error_retry, command);
    }

    private Command<View, Void> createOnRetryClickCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View message) {
                getHostActivity().recreate();
                return null;
            }
        };
    }

    private CommandExecutor<Long, ResponseMessage> createOnResponseCommandExecutor() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        Command<ResponseMessage, Void> command = createOnLoginResponseCommand();
        commandExecutor.put((long) R.id.requestLogin, command);
        command = createOnUserInfoResponseCommand();
        commandExecutor.put((long) R.id.requestUserInfo, command);
        return commandExecutor;
    }

    private Command<ResponseMessage, Void> createOnLoginResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage msg) {
                changeLoginView(false);
                if (!msg.isSuccessful()) {
                    if (msg.getStatusCode() == NO_INTERNET_CONNECTION) {
                        showErrorConnection(false);
                    } else if (msg.getStatusCode() == BAD_REQUEST_STATUS_CODE) {
                        displayErrorOnFields();
                    } else {
                        Toast.makeText(getHostActivity(), R.string.error_login_msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                return null;
            }
        };
    }

    private Command<ResponseMessage, Void> createOnUserInfoResponseCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage message) {
                changeLoginView(false);
                if (message.isSuccessful()) {
                    startHomeScreen();
                } else {
                    Toast.makeText(getHostActivity(), R.string.error_login_msg,
                            Toast.LENGTH_SHORT).show();
                }
                return null;
            }


        };
    }

    private void startHomeScreen() {
        ActivityActionRequest request = new ActivityActionRequest(ActionType.START_ACTIVITY);
        request.action(MainActivity.class);
        Event event = new EventBuilder(R.id.startActivityAction, request).execute(getHostActivity());
        getFeature().startActivityActionRequest(event);
    }

    private Command<View, Void> createLoginButtonOnClick() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                login();
                return null;
            }
        };
    }

    private Command<Message, Void> createOnResponseCommand() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                long id = message.getId();
                ResponseMessage responseMessage = (ResponseMessage) message;
                onResponseCommandExecutor.execute(id, responseMessage);
                return null;
            }
        };
    }

    private Command<Message, Void> createOnNetWorkChangeListener() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message p) {
                NetworkStateReceiver.ConnectedNetworks connectedNetworks = p.getContent();
                showErrorConnection(connectedNetworks.any);
                return null;
            }
        };
    }

    private void login() {
        setEditTextDefaultDrawables();
        userName = etUserName.getText().toString();
        userName = userName.toLowerCase();
        password = etPassword.getText().toString().trim();
        requestLogin(userName, password);
    }


    private void requestLogin(String username, String pass) {
        changeLoginView(true);
        if (isEmptyFields(userName, password)) {
            changeLoginView(false);
            return;
        }
        if (!isUserNameValid(userName)) {
            displayErrorOnFields();
            changeLoginView(false);
            return;
        } else {
            hideErrorFromFields();
        }
        createLoginEventAndRequest(username, pass);
    }

    private void createLoginEventAndRequest(String userName, String password) {

        loginModel.setUserName(userName);
        loginModel.setPassword(password);

        Event event = new Event.Builder(R.id.requestLogin).build();
        loginModel.execute(event);
    }

    private void displayErrorOnFields() {
        setUserNameErrorMsg(usernameErrorMsg);
        setPasswordErrorMsg(passwordErrorMsg);
        setFocus();
    }

    private void hideErrorFromFields() {
        usernameError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);
    }

    private boolean isEmptyFields(String username, String pass) {
        boolean isEmpty = false;
        if (username == null || username.equals("")) {
            setUserNameErrorMsg(usernameMissingMsg);
            setPasswordErrorMsg(passwordMissingMsg);
            isEmpty = true;
            setFocus();
        } else if (pass == null || pass.equals("")) {
            setPasswordErrorMsg(passwordMissingMsg);
            isEmpty = true;
            setFocus();
        } else if (!isValidPassword(pass)) {
            setPasswordErrorMsg(passwordErrorMsg);
            isEmpty = true;
            setFocus();
        }
        return isEmpty;
    }

    private boolean isUserNameValid(String username) {
        return new EmailValidator().execute(username) || isValidMobileNumber(username);
    }

    private boolean isValidMobileNumber(String username) {
        return (username.matches("[0-9]+"));
    }

    private boolean isValidPassword(String password) {
        return new PasswordValidator().execute(password);
    }

    private void showErrorConnection(boolean network) {
        if (network) {
            activityLoginInternetError.setVisibility(View.GONE);
            activityLoginFieldsLayout.setVisibility(View.VISIBLE);
        } else {
            activityLoginInternetError.setVisibility(View.VISIBLE);
            activityLoginFieldsLayout.setVisibility(View.GONE);
        }
    }


    private void changeLoginView(boolean load) {
        if (load) {
            login.setText("");
            loading.setVisibility(View.VISIBLE);
        } else {
            login.setText(singIn);
            loading.setVisibility(View.GONE);
        }
    }


    private void setUserNameErrorMsg(String msg) {
        usernameError.setVisibility(View.VISIBLE);
        usernameError.setText(msg);
        setFieldResource(errorUser, etUserName);
    }

    private void setFieldResource(Drawable image, EditText editText) {
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds(0, 0, w, h);
        editText.setCompoundDrawables(image, null, null, null);
    }

    private void setFocus() {
        etPassword.clearFocus();
        etPassword.setText("");
    }

    private void setPasswordErrorMsg(String msg) {
        passwordError.setVisibility(View.VISIBLE);
        passwordError.setText(msg);
        setFieldResource(errorLock, etPassword);
    }

    private void setEditTextDefaultDrawables() {
        setFieldResource(selectorUsername, etUserName);
        setFieldResource(selectorPassword, etPassword);
        usernameError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        onClickCommandExecutor.clear();
        onResponseCommandExecutor.clear();
        super.onDestroy();
    }
}
