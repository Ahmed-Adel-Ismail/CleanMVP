package com.base.cached;


import com.base.entities.annotations.Authorization;
import com.base.entities.auth.AuthClient;
import com.base.entities.auth.AuthEntity;
import com.base.entities.auth.AuthGrantTypePassword;

import org.junit.Assert;
import org.junit.Test;


public class AuthEntityTest {


    private static String FAKE_CLIENT = "fakeClient";

    public AuthEntityTest() {


    }


    @Test
    public void newFakeAuthEntity$$initSuccessful() throws Exception {
        FakeEntity e = new FakeEntity();
        System.out.print("fake auth data : " + e.getClient_id() + " - " + e.getGrant_type());
        Assert.assertTrue(e.getClient_id().equals(FAKE_CLIENT)
                && e.getClient_secret().equals(FAKE_CLIENT)
                && e.getGrant_type().equals("password"));

    }


    @Authorization(client = AuthClientFake.class, grantType = AuthGrantTypePassword.class)
    static class FakeEntity extends AuthEntity {


    }

    static class AuthClientFake implements AuthClient {

        @Override
        public String getValue() {
            return FAKE_CLIENT;
        }
    }

}