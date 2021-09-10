package com.gw.zph.modle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgBean implements Parcelable {
    private String phoneNum;
    private String time;
    private String title;
    private String content;
    private String state;
    private String var1;
    private String var2;

    public MsgBean() {
    }

    protected MsgBean(Parcel in) {
        phoneNum = in.readString();
        time = in.readString();
        title = in.readString();
        content = in.readString();
        state = in.readString();
        var1 = in.readString();
        var2 = in.readString();
    }

    public static final Creator<MsgBean> CREATOR = new Creator<MsgBean>() {
        @Override
        public MsgBean createFromParcel(Parcel in) {
            return new MsgBean(in);
        }

        @Override
        public MsgBean[] newArray(int size) {
            return new MsgBean[size];
        }
    };

    public String getPhoneNum() {
        return phoneNum == null ? "" : phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
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
        dest.writeString(phoneNum);
        dest.writeString(time);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(state);
        dest.writeString(var1);
        dest.writeString(var2);
    }
}
