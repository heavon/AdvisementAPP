package com.example.heavon.vo;

/**
 * Created by Yadong on 16/3/5.
 */
public class User {
    int uid;
    String name;
    String password;
    String email;
    String phone;
    String introduction;
    String avatar;

    public User(){

    }

    public User(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public User(int uid, String name, String password, String email, String phone, String introduction, String avatar) {
        this.uid = uid;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.introduction = introduction;
        this.avatar = avatar;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
