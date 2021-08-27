package com.gw.zph.model.login.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${lcarry} on 2017/5/23.
 */

public class ReqUserBean implements Parcelable {

    /**
     * account :
     * password :
     */

    private String account;
    private String password;
    private String pubKey;//公钥

    private String imgCodeId;
    private String code;

    public String getImgCodeId() {
        return imgCodeId == null ? "" : imgCodeId;
    }

    public void setImgCodeId(String imgCodeId) {
        this.imgCodeId = imgCodeId;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPubKey() {
        return pubKey == null ? "" : pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public ReqUserBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.account);
        dest.writeString(this.password);
        dest.writeString(this.pubKey);
        dest.writeString(this.imgCodeId);
        dest.writeString(this.code);
    }

    protected ReqUserBean(Parcel in) {
        this.account = in.readString();
        this.password = in.readString();
        this.pubKey = in.readString();
        this.imgCodeId = in.readString();
        this.code = in.readString();
    }

    public static final Creator<ReqUserBean> CREATOR = new Creator<ReqUserBean>() {
        @Override
        public ReqUserBean createFromParcel(Parcel source) {
            return new ReqUserBean(source);
        }

        @Override
        public ReqUserBean[] newArray(int size) {
            return new ReqUserBean[size];
        }
    };
}
