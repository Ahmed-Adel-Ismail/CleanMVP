package com.base.usecases.requesters.server.websocket;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ahmed Adel on 1/19/2017.
 */
public class SocketMessageTest {

    public SocketMessageTest() {
    }


    @Test
    public void getContentObject_from_A$return_A() throws Exception {

        A a = new A();
        a.value = "a";
        a.index = 1;

        SocketMessage s = new SocketMessage();
        s.setContent(new Gson().toJson(a));

        A a2 = s.parseContent(A.class);
        Assert.assertTrue(a2 != null && a2.index == 1 && a2.value.equals("a"));


    }

    static class A {
        String value;
        int index;
    }


}