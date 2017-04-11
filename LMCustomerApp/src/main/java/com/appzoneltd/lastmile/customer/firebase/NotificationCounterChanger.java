package com.appzoneltd.lastmile.customer.firebase;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.commands.Command;
import com.base.abstraction.system.Preferences;

/**
 * a {@link Command} that decrease notification counter
 * <p>
 * Created by Wafaa on 12/24/2016.
 */

public class NotificationCounterChanger implements Command<Void, Void> {

    @Override
    public Void execute(Void p) {
        Integer counter = Preferences.getInstance().load(R.string.PREFS_KEY_NOTIFICATION_COUNTER, 0);
        if (counter > 0) {
            counter -= 1;
            if (counter >= 0) {
                Preferences.getInstance().save(R.string.PREFS_KEY_NOTIFICATION_COUNTER, counter);
            }
        }
        return null;
    }
}
