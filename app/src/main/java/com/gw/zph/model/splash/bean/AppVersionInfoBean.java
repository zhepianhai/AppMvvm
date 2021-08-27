package com.gw.zph.model.splash.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${lcarry} on 2017/7/6.
 */

public class AppVersionInfoBean implements Parcelable {

    /**
     * id : 1077757085828509696
     * version : 0.0.38
     * updateContent : 123
     * storePath : http://219.142.62.135:8080/version/download/1077757085828509696
     * appName : 河湖长制督查
     * fileName : ff83e2b34ec982e5ba174275e5caa337.apk
     * forceUpdate : 0
     * pubDate : 1545792287000
     */

    private String id;
    private String version;
    private String updateContent;
    private String storePath;
    private String appName;
    private String fileName;
    private int forceUpdate;
    private String pubDate;

    public AppVersionInfoBean() {
    }

    protected AppVersionInfoBean(Parcel in) {
        id = in.readString();
        version = in.readString();
        updateContent = in.readString();
        storePath = in.readString();
        appName = in.readString();
        fileName = in.readString();
        forceUpdate = in.readInt();
        pubDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(version);
        dest.writeString(updateContent);
        dest.writeString(storePath);
        dest.writeString(appName);
        dest.writeString(fileName);
        dest.writeInt(forceUpdate);
        dest.writeString(pubDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppVersionInfoBean> CREATOR = new Creator<AppVersionInfoBean>() {
        @Override
        public AppVersionInfoBean createFromParcel(Parcel in) {
            return new AppVersionInfoBean(in);
        }

        @Override
        public AppVersionInfoBean[] newArray(int size) {
            return new AppVersionInfoBean[size];
        }
    };

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version == null ? "" : version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateContent() {
        return updateContent == null ? "" : updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getStorePath() {
        return storePath == null ? "" : storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public String getAppName() {
        return appName == null ? "" : appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getPubDate() {
        return pubDate == null ? "" : pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "AppVersionInfoBean{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", updateContent='" + updateContent + '\'' +
                ", storePath='" + storePath + '\'' +
                ", appName='" + appName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", forceUpdate=" + forceUpdate +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}
