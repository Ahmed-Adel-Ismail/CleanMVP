package com.appzoneltd.lastmile.customer.subfeatures.notificationcommands;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.serializers.JsonSetLoader;
import com.base.abstraction.serializers.JsonListSaver;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by Wafaa on 12/14/2016.
 */

public class NotificationsItemDeletion<T extends Serializable> implements Command<Class<T>, Void> {

    private T notification;

    public NotificationsItemDeletion(T notification) {
        this.notification = notification;
    }

    @Override
    public Void execute(Class<T> p) {
        TreeSet<T> treeSet = new JsonSetLoader<T>
                (R.string.PREFS_KEY_NOTIFICATION_LIST).execute(p);
        treeSet.remove(notification);
        new JsonListSaver<T>(R.string.PREFS_KEY_NOTIFICATION_LIST).execute(treeSet);
        return null;
    }


}
