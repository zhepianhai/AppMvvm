package com.gw.zph.model.login.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${ls} on 2020/2/4.
 */

public class CaptureImageBean implements Parcelable {

    private String img;
    private String imgCodeId;

    protected CaptureImageBean(Parcel in) {
        img = in.readString();
        imgCodeId = in.readString();
    }

    public static final Creator<CaptureImageBean> CREATOR = new Creator<CaptureImageBean>() {
        @Override
        public CaptureImageBean createFromParcel(Parcel in) {
            return new CaptureImageBean(in);
        }

        @Override
        public CaptureImageBean[] newArray(int size) {
            return new CaptureImageBean[size];
        }
    };

    public String getImg() {
        return img == null ? "" : img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgCodeId() {
        return imgCodeId == null ? "" : imgCodeId;
    }

    public void setImgCodeId(String imgCodeId) {
        this.imgCodeId = imgCodeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(imgCodeId);
    }
}
