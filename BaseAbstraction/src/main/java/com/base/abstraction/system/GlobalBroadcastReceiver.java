package com.base.abstraction.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.logs.Logger;

import java.util.List;

/**
 * a {@link BroadcastReceiver} that handles global broadcasts per application
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
public class GlobalBroadcastReceiver extends BroadcastReceiver {

    private final List<Executor<Intent>> executors;

    public GlobalBroadcastReceiver(List<Executor<Intent>> executors) {
        this.executors = executors;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionStringResourceName = intent.getAction();
        long actionStringResource = AppResources.stringId(actionStringResourceName);
        for (Executor<Intent> executor : executors) {
            try {
                executor.execute(actionStringResource, intent);
            } catch (CheckedReferenceClearedException e) {
                Logger.getInstance().exception(e);
            } catch (Throwable e) {
                new TestException().execute(e);
            }
        }
    }


}
