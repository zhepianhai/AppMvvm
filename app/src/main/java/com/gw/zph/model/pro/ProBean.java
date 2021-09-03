package com.gw.zph.model.pro;

import android.os.Parcel;
import android.os.Parcelable;

public class ProBean implements Parcelable {
    private String content;
    private String per;
    private String time;
    public ProBean(){}

    protected ProBean(Parcel in) {
        content = in.readString();
        per = in.readString();
        time = in.readString();
    }

    public static final Creator<ProBean> CREATOR = new Creator<ProBean>() {
        @Override
        public ProBean createFromParcel(Parcel in) {
            return new ProBean(in);
        }

        @Override
        public ProBean[] newArray(int size) {
            return new ProBean[size];
        }
    };

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPer() {
        return per == null ? "" : per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(per);
        dest.writeString(time);
    }
}
