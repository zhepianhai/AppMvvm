package com.gw.zph.base.db;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "ADDRESS_BEAN", createInDb = false)
public class AddressBean {
    @Id(autoincrement = false)
    @NotNull
    @Property(nameInDb = "AD_CODE")
    private String adCode;
    @NotNull
    @Property(nameInDb = "AD_GRAD")
    private String adGrad;
    @NotNull
    @Property(nameInDb = "AD_NAME")
    private String adName;
    @Property(nameInDb = "IS_POVERYT")
    private String provinceAdCode;
    @Property(nameInDb = "AD_FCODE")
    private String upCode;
    @Property(nameInDb = "AD_FULL_NAME")
    private String wholeName;
    @Transient
    private boolean checked;

    public String getAdCode() {
        return adCode == null ? "" : adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAdGrad() {
        return adGrad == null ? "" : adGrad;
    }

    public void setAdGrad(String adGrad) {
        this.adGrad = adGrad;
    }

    public String getAdName() {
        return adName == null ? "" : adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getProvinceAdCode() {
        return provinceAdCode == null ? "" : provinceAdCode;
    }

    public void setProvinceAdCode(String provinceAdCode) {
        this.provinceAdCode = provinceAdCode;
    }

    public String getUpCode() {
        return upCode == null ? "" : upCode;
    }

    public void setUpCode(String upCode) {
        this.upCode = upCode;
    }

    public String getWholeName() {
        return wholeName == null ? "" : wholeName;
    }

    public void setWholeName(String wholeName) {
        this.wholeName = wholeName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Generated(hash = 1406751518)
    public AddressBean(@NotNull String adCode, @NotNull String adGrad,
                       @NotNull String adName, String provinceAdCode, String upCode,
                       String wholeName) {
        this.adCode = adCode;
        this.adGrad = adGrad;
        this.adName = adName;
        this.provinceAdCode = provinceAdCode;
        this.upCode = upCode;
        this.wholeName = wholeName;
    }

    @Generated(hash = 30780671)
    public AddressBean() {
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "adCode='" + adCode + '\'' +
                ", adGrad='" + adGrad + '\'' +
                ", adName='" + adName + '\'' +
                ", provinceAdCode='" + provinceAdCode + '\'' +
                ", upCode='" + upCode + '\'' +
                ", wholeName='" + wholeName + '\'' +
                ", checked=" + checked +
                '}';
    }
}
