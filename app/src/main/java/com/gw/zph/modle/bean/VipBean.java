package com.gw.zph.modle.bean;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Vip
 * */
public class VipBean implements Parcelable {
    private String phone;
    private String type;
    private String num;
    private String state;
    private String var1;
    private String var2;
    public VipBean(){}

    protected VipBean(Parcel in) {
        phone = in.readString();
        type = in.readString();
        num = in.readString();
        state = in.readString();
        var1 = in.readString();
        var2 = in.readString();
    }

    public static final Creator<VipBean> CREATOR = new Creator<VipBean>() {
        @Override
        public VipBean createFromParcel(Parcel in) {
            return new VipBean(in);
        }

        @Override
        public VipBean[] newArray(int size) {
            return new VipBean[size];
        }
    };

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num == null ? "" : num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getState() {
        return state == null ? "" : state;
    }

    public void setState(String state) {
        this.state = state;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(type);
        dest.writeString(num);
        dest.writeString(state);
        dest.writeString(var1);
        dest.writeString(var2);
    }
}
