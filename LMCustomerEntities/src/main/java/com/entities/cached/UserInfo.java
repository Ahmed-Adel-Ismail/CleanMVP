package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 8/16/2016.
 */
public class UserInfo implements Serializable {

    private long userId;
    private long roleId;
    private String roleName;
    private String username;
    private String email;
    private String phone;
    private int pageSize;


    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public static UserInfo createFakeUser() {
        UserInfo fakeUser = new UserInfo();
        fakeUser.phone = "01001964020";
        fakeUser.username = "ali.sultan";
        fakeUser.userId = 821788068;
        return fakeUser;

    }

}
