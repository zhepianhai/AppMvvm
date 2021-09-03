package com.gw.zph.base.db.dao;

import android.os.Parcel;
import android.os.Parcelable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity()
public class OffLineLatLngInfo  implements Parcelable{
    @Id(autoincrement = false)
    private Long id;
    @NotNull
    private String persId;
    private String orgId;
    private String groupId;
    private String operateTime;
    private String persName;
    private String mobile;
    private double lon;
    private double lat;
    private String adCode;
    private float speed;
    private String address;


    protected OffLineLatLngInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        persId = in.readString();
        orgId = in.readString();
        groupId = in.readString();
        operateTime = in.readString();
        persName = in.readString();
        mobile = in.readString();
        lon = in.readDouble();
        lat = in.readDouble();
        adCode = in.readString();
        speed=in.readFloat();
        address=in.readString();
    }

    public static final Creator<OffLineLatLngInfo> CREATOR = new Creator<OffLineLatLngInfo>() {
        @Override
        public OffLineLatLngInfo createFromParcel(Parcel in) {
            return new OffLineLatLngInfo(in);
        }

        @Override
        public OffLineLatLngInfo[] newArray(int size) {
            return new OffLineLatLngInfo[size];
        }
    };

    public String getAdCode() {
        return this.adCode;
    }
    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }
    public double getLat() {
        return this.lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return this.lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPersName() {
        return this.persName;
    }
    public void setPersName(String persName) {
        this.persName = persName;
    }
    public String getOperateTime() {
        return this.operateTime;
    }
    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getOrgId() {
        return this.orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getPersId() {
        return this.persId;
    }
    public void setPersId(String persId) {
        this.persId = persId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 81586516)
    public OffLineLatLngInfo(Long id, @NotNull String persId, String orgId, String groupId,
            String operateTime, String persName, String mobile, double lon, double lat, String adCode,
            float speed, String address) {
        this.id = id;
        this.persId = persId;
        this.orgId = orgId;
        this.groupId = groupId;
        this.operateTime = operateTime;
        this.persName = persName;
        this.mobile = mobile;
        this.lon = lon;
        this.lat = lat;
        this.adCode = adCode;
        this.speed = speed;
        this.address = address;
    }
    @Generated(hash = 1885523921)
    public OffLineLatLngInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
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
        dest.writeString(orgId);
        dest.writeString(groupId);
        dest.writeString(operateTime);
        dest.writeString(persName);
        dest.writeString(mobile);
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeString(adCode);
        dest.writeFloat(speed);
        dest.writeString(address);
    }
    public float getSpeed() {
        return this.speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
