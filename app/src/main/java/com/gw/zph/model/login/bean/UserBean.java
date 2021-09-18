package com.gw.zph.model.login.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class UserBean implements Parcelable {

    private String username;

    private String psd;

    private String phone;


    private String persid;

    private String status;

    private String sex;

    private String usertype;

    private String var1;

    private String var2;


    public UserBean() {
    }

    protected UserBean(Parcel in) {
        username = in.readString();
        psd = in.readString();
        phone = in.readString();
        persid = in.readString();
        status = in.readString();
        sex = in.readString();
        usertype = in.readString();
        var1 = in.readString();
        var2 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(psd);
        dest.writeString(phone);
        dest.writeString(persid);
        dest.writeString(status);
        dest.writeString(sex);
        dest.writeString(usertype);
        dest.writeString(var1);
        dest.writeString(var2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPsd() {
        return psd == null ? "" : psd;
    }

    public void setPsd(String psd) {
        this.psd = psd;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPersid() {
        return persid == null ? "" : persid;
    }

    public void setPersid(String persid) {
        this.persid = persid;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsertype() {
        return usertype == null ? "" : usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getVar1() {
        return var1 == null ? "" : var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2 == null ? "" : var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }
}