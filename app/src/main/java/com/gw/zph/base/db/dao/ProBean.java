package com.gw.zph.base.db.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity()
public class ProBean implements Parcelable {
    @Id(autoincrement = false)
    private Long id;
    @NotNull
    private String persId;
    private String content;
    private String per;
    private String time;
    private String state;
    private String var1;
    private String var2;
    private String var3;
    public ProBean(){}


    protected ProBean(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        persId = in.readString();
        content = in.readString();
        per = in.readString();
        time = in.readString();
        var1 = in.readString();
        var2 = in.readString();
        var3 = in.readString();
        state = in.readString();
    }


    @Generated(hash = 1318526918)
    public ProBean(Long id, @NotNull String persId, String content, String per,
            String time, String state, String var1, String var2, String var3) {
        this.id = id;
        this.persId = persId;
        this.content = content;
        this.per = per;
        this.time = time;
        this.state = state;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(persId);
        dest.writeString(content);
        dest.writeString(per);
        dest.writeString(time);
        dest.writeString(var1);
        dest.writeString(var2);
        dest.writeString(var3);
        dest.writeString(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getPersId() {
        return this.persId;
    }


    public void setPersId(String persId) {
        this.persId = persId;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getPer() {
        return this.per;
    }


    public void setPer(String per) {
        this.per = per;
    }


    public String getTime() {
        return this.time;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getVar1() {
        return this.var1;
    }


    public void setVar1(String var1) {
        this.var1 = var1;
    }


    public String getVar2() {
        return this.var2;
    }


    public void setVar2(String var2) {
        this.var2 = var2;
    }


    public String getVar3() {
        return this.var3;
    }


    public void setVar3(String var3) {
        this.var3 = var3;
    }


    public String getState() {
        return this.state;
    }


    public void setState(String state) {
        this.state = state;
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
}
