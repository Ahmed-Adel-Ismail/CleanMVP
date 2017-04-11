package com.base.cached;

import java.util.HashMap;
import java.util.Map;

/**
 * the Object that holds the data for push notification's Remote Message
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
public class RemoteMessageData extends HashMap<String, String> {

    private static final String KEY_TITLE = "notification_title";
    private static final String KEY_BODY = "notification_body";
    private static final String KEY_ITEM_TITLE = "notification_item_title";
    private static final String KEY_ITEM_BODY = "notification_item_body";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TIME = "time";
    private static final String KEY_PAYLOAD = "payload";

    public RemoteMessageData() {

    }

    public RemoteMessageData(Map<String, String> copy) {
        super(copy);
    }

    public String getBody() {
        return get(KEY_BODY);
    }

    public String getItemBody() {
        return get(KEY_ITEM_BODY);
    }

    public String getItemTitle() {
        return get(KEY_ITEM_TITLE);
    }

    public String getTitle() {
        return get(KEY_TITLE);
    }

    public String getPayload() {
        return get(KEY_PAYLOAD);
    }

    public Long getTime() {
        Long result = null;
        String value = get(KEY_TIME);
        if (value != null) {
            result = Long.valueOf(value);
        }
        return result;
    }

    public String getType() {
        return get(KEY_TYPE);
    }

}
