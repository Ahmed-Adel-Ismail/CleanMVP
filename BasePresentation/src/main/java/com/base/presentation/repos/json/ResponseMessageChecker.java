package com.base.presentation.repos.json;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.failures.Failure;
import com.base.abstraction.exceptions.failures.OAuth2Failure;
import com.base.abstraction.failures.UnhandledFailureException;
import com.base.abstraction.reflections.Initializer;
import com.base.usecases.callbacks.CallbackDispatcher;
import com.base.usecases.events.ResponseMessage;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * a class that validates the {@code Status Code} of the response from server, if it is
 * {@code 200}, then it will notify the {@link CallbackDispatcher}, else it will throw a
 * {@link Failure} notice that any {@link Failure} that is thrown from this class
 * should have a no-args default constructor
 * <p>
 * Created by Ahmed Adel on 11/1/2016.
 */
class ResponseMessageChecker implements Command<CallbackDispatcher, Void> {

    private static SparseArray<Class<? extends Failure>> failures;
    private ResponseMessage responseMessage;


    static {
        failures = new SparseArray<>();
        failures.put(HTTP_OK, null);
        failures.put(HTTP_UNAUTHORIZED, OAuth2Failure.class);
    }


    ResponseMessageChecker(@NonNull ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public Void execute(CallbackDispatcher callbackDispatcher) {
        int statusCode = responseMessage.getStatusCode();
        Class<? extends Failure> failure = failures.get(statusCode);
        if (failure != null) {
            throwFailureException(failure);
        } else {
            notifyCallback(callbackDispatcher);
        }
        return null;
    }

    private void throwFailureException(Class<? extends Failure> error) {
        try {
            throw new Initializer<Failure>().execute(error);
        } catch (Failure failure) {
            throw failure;
        } catch (Throwable e) {
            throw new UnhandledFailureException(e);
        }
    }

    private void notifyCallback(CallbackDispatcher callbackDispatcher) {
        Event.Builder eBuilder = new Event.Builder(responseMessage.getId());
        Event responseEvent = eBuilder.message(responseMessage).build();
        callbackDispatcher.notifyCallback(responseEvent);
    }

}
