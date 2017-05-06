package com.base.notifications;

import com.base.entities.cached.RemoteMessageData;
import com.base.presentation.notifications.PayloadReader;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

/**
 * Created by Ahmed Adel on 12/19/2016.
 */
public class PayloadReaderTest {

    public PayloadReaderTest() {
    }

    @Test
    public void execute$$PayloadTest_a_notNull() throws Exception {

        String value = "a";

        PayloadTest p = new PayloadTest();
        p.a = value;

        RemoteMessage message = createRemoteMessage(p);
        String messageJson = new Gson().toJson(message);
        System.out.println("(1) " + messageJson);

        p = new PayloadReader<>(PayloadTest.class).execute(message.getData());
        System.out.println(new Gson().toJson(p));

        Assert.assertTrue(p.a.equals(value));

    }

    @Test
    public void execute$$PayloadTest_a_notNull_innerClass_notNull() throws Exception {

        String value = "a";
        String innerValue = "inner A";

        PayloadTest innerPayloadTest = new PayloadTest();
        innerPayloadTest.a = innerValue;

        PayloadTest p = new PayloadTest();
        p.a = value;
        p.innerClass = innerPayloadTest;

        RemoteMessage message = createRemoteMessage(p);
        String messageJson = new Gson().toJson(message);
        System.out.println("(2) " + messageJson);

        p = new PayloadReader<>(PayloadTest.class).execute(message.getData());
        System.out.println(new Gson().toJson(p));

        Assert.assertTrue(p.a.equals(value) && p.innerClass.a.equals(innerValue));
    }

    private static RemoteMessage createRemoteMessage(PayloadTest p) {
        RemoteMessageData data = new RemoteMessageData();
        String payloadString = new Gson().toJson(p);
        System.out.println(payloadString);

        data.put("payload", payloadString);

        RemoteMessage message = new RemoteMessage();
        message.setData(data);

        return message;
    }

    private static class PayloadTest implements Serializable {
        String a;
        PayloadTest innerClass;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }
}
