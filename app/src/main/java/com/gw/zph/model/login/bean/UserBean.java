package com.gw.zph.model.login.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class UserBean implements Parcelable {
    /**
     * dpnm :
     * dppost :
     * idnm :
     * plst :
     * school :
     * specialty :
     * imgurl :
     * guid : 82C66BD251437F29E0530DC3010ACF31
     * persName : 李力
     * pwd :
     * sex :
     * telnumb :
     * faxnumb :
     * mobilenumb : 18010463320
     * email :
     * bornDate :
     * orgId : 1209
     * orgNm :
     * admDuty :
     * collTime :
     * remark :
     * permission :
     * roleIds :
     * allNode : [{"id":"001008001001","pid":"001008001","pnm":"第1组","sttm":"","entm":"","count":0,"onLineCount":0},{"id":"002011001001","pid":"002011001","pnm":"北京地区","sttm":"","entm":"","count":0,"onLineCount":0},{"id":"002011001002","pid":"002011001","pnm":"天津地区测试","sttm":1551369600000,"entm":1551369600000,"count":0,"onLineCount":0},{"id":"003001001001","pid":"003001001","pnm":"三级","sttm":"","entm":"","count":0,"onLineCount":0}]
     */
    private String jurisdiction;
    private String dpnm;
    private String dppost;
    private String idnm;
    private String plst;
    private String school;
    private String specialty;
    private String imgurl;
    private String guid;
    private String persName;
    private String pwd;
    private String sex;
    private String telnumb;
    private String faxnumb;
    private String mobilenumb;
    private String email;
    private String bornDate;
    private String orgId;
    private String orgNm;
    private String admDuty;
    private String collTime;
    private String remark;
    private String permission;
    private String roleIds;
    private String token;
    private String accessToken;
    private String persType;

    public String getAccessToken() {
        return accessToken == null ? "" : accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private List<AllNodeBean> allNode;
    private List<AllOrgBean> allOrg;

    public List<AllOrgBean> getAllOrg() {
        if (allOrg == null) {
            return new ArrayList<>();
        }
        return allOrg;
    }

    public void setAllOrg(List<AllOrgBean> allOrg) {
        this.allOrg = allOrg;
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        dpnm = in.readString();
        dppost = in.readString();
        idnm = in.readString();
        plst = in.readString();
        school = in.readString();
        specialty = in.readString();
        imgurl = in.readString();
        guid = in.readString();
        persName = in.readString();
        pwd = in.readString();
        sex = in.readString();
        telnumb = in.readString();
        faxnumb = in.readString();
        mobilenumb = in.readString();
        email = in.readString();
        bornDate = in.readString();
        orgId = in.readString();
        orgNm = in.readString();
        admDuty = in.readString();
        collTime = in.readString();
        remark = in.readString();
        permission = in.readString();
        roleIds = in.readString();
        token = in.readString();
        jurisdiction = in.readString();
        accessToken = in.readString();
        persType = in.readString();
        allNode = in.createTypedArrayList(AllNodeBean.CREATOR);
        allOrg = in.createTypedArrayList(AllOrgBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dpnm);
        dest.writeString(dppost);
        dest.writeString(idnm);
        dest.writeString(plst);
        dest.writeString(school);
        dest.writeString(specialty);
        dest.writeString(imgurl);
        dest.writeString(guid);
        dest.writeString(persName);
        dest.writeString(pwd);
        dest.writeString(sex);
        dest.writeString(telnumb);
        dest.writeString(faxnumb);
        dest.writeString(mobilenumb);
        dest.writeString(email);
        dest.writeString(bornDate);
        dest.writeString(orgId);
        dest.writeString(orgNm);
        dest.writeString(admDuty);
        dest.writeString(collTime);
        dest.writeString(remark);
        dest.writeString(permission);
        dest.writeString(roleIds);
        dest.writeString(token);
        dest.writeString(jurisdiction);
        dest.writeString(accessToken);
        dest.writeString(persType);
        dest.writeTypedList(allNode);
        dest.writeTypedList(allOrg);
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

    public String getDpnm() {
        return dpnm == null ? "" : dpnm;
    }

    public void setDpnm(String dpnm) {
        this.dpnm = dpnm;
    }

    public String getDppost() {
        return dppost == null ? "" : dppost;
    }

    public void setDppost(String dppost) {
        this.dppost = dppost;
    }

    public String getIdnm() {
        return idnm == null ? "" : idnm;
    }

    public void setIdnm(String idnm) {
        this.idnm = idnm;
    }

    public String getPlst() {
        return plst == null ? "" : plst;
    }

    public void setPlst(String plst) {
        this.plst = plst;
    }

    public String getSchool() {
        return school == null ? "" : school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSpecialty() {
        return specialty == null ? "" : specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getImgurl() {
        return imgurl == null ? "" : imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getGuid() {
        return guid == null ? "" : guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPersName() {
        return persName == null ? "" : persName;
    }

    public void setPersName(String persName) {
        this.persName = persName;
    }

    public String getPwd() {
        return pwd == null ? "" : pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelnumb() {
        return telnumb == null ? "" : telnumb;
    }

    public void setTelnumb(String telnumb) {
        this.telnumb = telnumb;
    }

    public String getFaxnumb() {
        return faxnumb == null ? "" : faxnumb;
    }

    public void setFaxnumb(String faxnumb) {
        this.faxnumb = faxnumb;
    }

    public String getMobilenumb() {
        return mobilenumb == null ? "" : mobilenumb;
    }

    public void setMobilenumb(String mobilenumb) {
        this.mobilenumb = mobilenumb;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBornDate() {
        return bornDate == null ? "" : bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getOrgId() {
        return orgId == null ? "" : orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgNm() {
        return orgNm == null ? "" : orgNm;
    }

    public void setOrgNm(String orgNm) {
        this.orgNm = orgNm;
    }

    public String getAdmDuty() {
        return admDuty == null ? "" : admDuty;
    }

    public void setAdmDuty(String admDuty) {
        this.admDuty = admDuty;
    }

    public String getCollTime() {
        return collTime == null ? "" : collTime;
    }

    public void setCollTime(String collTime) {
        this.collTime = collTime;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getJurisdiction() {
        return jurisdiction == null ? "" : jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getPermission() {
        return permission == null ? "" : permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRoleIds() {
        return roleIds == null ? "" : roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public List<AllNodeBean> getAllNode() {
        if (allNode == null) {
            return new ArrayList<>();
        }
        return allNode;
    }

    public void setAllNode(List<AllNodeBean> allNode) {
        this.allNode = allNode;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPersType() {
        return persType == null ? "" : persType;
    }

    public void setPersType(String persType) {
        this.persType = persType;
    }

    public static class AllNodeBean implements Parcelable {
        /**
         * id : 001008001001
         * pid : 001008001
         * pnm : 第1组
         * sttm :
         * entm :
         * count : 0
         * onLineCount : 0
         */

        private String id;
        private String pid;
        private String pnm;
        private String sttm;
        private String entm;
        private int count;
        private int onLineCount;

        protected AllNodeBean(Parcel in) {
            id = in.readString();
            pid = in.readString();
            pnm = in.readString();
            sttm = in.readString();
            entm = in.readString();
            count = in.readInt();
            onLineCount = in.readInt();
        }

        public static final Creator<AllNodeBean> CREATOR = new Creator<AllNodeBean>() {
            @Override
            public AllNodeBean createFromParcel(Parcel in) {
                return new AllNodeBean(in);
            }

            @Override
            public AllNodeBean[] newArray(int size) {
                return new AllNodeBean[size];
            }
        };

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid == null ? "" : pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPnm() {
            return pnm == null ? "" : pnm;
        }

        public void setPnm(String pnm) {
            this.pnm = pnm;
        }

        public String getSttm() {
            return sttm == null ? "" : sttm;
        }

        public void setSttm(String sttm) {
            this.sttm = sttm;
        }

        public String getEntm() {
            return entm == null ? "" : entm;
        }

        public void setEntm(String entm) {
            this.entm = entm;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getOnLineCount() {
            return onLineCount;
        }

        public void setOnLineCount(int onLineCount) {
            this.onLineCount = onLineCount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(pid);
            parcel.writeString(pnm);
            parcel.writeString(sttm);
            parcel.writeString(entm);
            parcel.writeInt(count);
            parcel.writeInt(onLineCount);
        }
    }

    public static class AllOrgBean implements Parcelable {

        /**
         * norgId : null
         * norgPid : null
         * orgId : 014
         * orgPid : 1
         * orgNm : 福建省
         * rlcode : 350000000000
         */

        private String norgId;
        private String norgPid;
        private String orgId;
        private String orgPid;
        private String orgNm;
        private String rlcode;
        private boolean flag = false; //

        public AllOrgBean() {
        }

        protected AllOrgBean(Parcel in) {
            norgId = in.readString();
            norgPid = in.readString();
            orgId = in.readString();
            orgPid = in.readString();
            orgNm = in.readString();
            rlcode = in.readString();
            flag = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(norgId);
            dest.writeString(norgPid);
            dest.writeString(orgId);
            dest.writeString(orgPid);
            dest.writeString(orgNm);
            dest.writeString(rlcode);
            dest.writeByte((byte) (flag ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<AllOrgBean> CREATOR = new Creator<AllOrgBean>() {
            @Override
            public AllOrgBean createFromParcel(Parcel in) {
                return new AllOrgBean(in);
            }

            @Override
            public AllOrgBean[] newArray(int size) {
                return new AllOrgBean[size];
            }
        };

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getNorgId() {
            return norgId == null ? "" : norgId;
        }

        public void setNorgId(String norgId) {
            this.norgId = norgId;
        }

        public String getNorgPid() {
            return norgPid == null ? "" : norgPid;
        }

        public void setNorgPid(String norgPid) {
            this.norgPid = norgPid;
        }

        public String getOrgId() {
            return orgId == null ? "" : orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getOrgPid() {
            return orgPid == null ? "" : orgPid;
        }

        public void setOrgPid(String orgPid) {
            this.orgPid = orgPid;
        }

        public String getOrgNm() {
            return orgNm == null ? "" : orgNm;
        }

        public void setOrgNm(String orgNm) {
            this.orgNm = orgNm;
        }

        public String getRlcode() {
            return rlcode == null ? "" : rlcode;
        }

        public void setRlcode(String rlcode) {
            this.rlcode = rlcode;
        }


    }

    /**
     * guid : 0706DCA39CD94F85963EC9B527A1AECA
     * persName : 许国锋
     * pwd : 13398610161
     * sex : ""
     * telnumb : ""
     * faxnumb : ""
     * mobilenumb : 13398610161
     * email : ""
     * bornDate : ""
     * orgId : 100
     * orgNm : ""
     * admDuty : ""
     * collTime : ""
     * remark : ""
     */
    @Override
    public String toString() {
        return "UserBeanNewNew{" +
                "jurisdiction='" + jurisdiction + '\'' +
                ", dpnm='" + dpnm + '\'' +
                ", dppost='" + dppost + '\'' +
                ", idnm='" + idnm + '\'' +
                ", plst='" + plst + '\'' +
                ", school='" + school + '\'' +
                ", specialty='" + specialty + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", guid='" + guid + '\'' +
                ", persName='" + persName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", sex='" + sex + '\'' +
                ", telnumb='" + telnumb + '\'' +
                ", faxnumb='" + faxnumb + '\'' +
                ", mobilenumb='" + mobilenumb + '\'' +
                ", email='" + email + '\'' +
                ", bornDate='" + bornDate + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgNm='" + orgNm + '\'' +
                ", admDuty='" + admDuty + '\'' +
                ", collTime='" + collTime + '\'' +
                ", remark='" + remark + '\'' +
                ", permission='" + permission + '\'' +
                ", roleIds='" + roleIds + '\'' +
                ", token='" + token + '\'' +
                ", allNode=" + allNode +
                '}';
    }
}