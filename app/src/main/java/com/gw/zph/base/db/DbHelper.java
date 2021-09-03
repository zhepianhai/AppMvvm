package com.gw.zph.base.db;


import android.content.Context;

import com.gw.zph.base.db.dao.DaoMaster;
import com.gw.zph.base.db.dao.DaoSession;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;

import org.greenrobot.greendao.AbstractDao;


/**
 * @Description: 数据库操作类
 * 所有数据库操作都可以参考该类实现自己的数据库操作管理类，不同的Dao实现
 * <p>
 * 初始化：在application中进行如下初始化操作：DbHelper.getInstance().init(this);
 * <p>
 * 增：DbHelper.getInstance().author().insert(mAuthorModel);
 * <p>
 * 删：DbHelper.getInstance().author().delete(mAuthorModel);
 * <p>
 * 改：DbHelper.getInstance().author().update(mAuthorModel);
 * <p>
 * 查：DbHelper.getInstance().author().loadAll()
 * <p>
 * <p>
 * 数据库升级
 * <p>
 * 添加一个新类继承DaoMaster.OpenHelper，添加构造函数并实现onUpgrade方法,在onUpgrade方法添加如下代码即可，参数为所有的Dao类：
 * <p>
 * 对应的getAbstractDao方法就行。
 * @author: smartwangbin@163.com
 * @date: 17/1/18 23:18.
 */
public class DbHelper {
    public static final String PART = "PART";
    public static final String ALL = "ALL";
    //内部数据库
    private static final String DB_NAME = "waterregime.db";//数据库名称
    //外部数据库
    public static final String DB_NAME_ADD = "address.db";//数据库名称
    // 问题基础信息库
    public static final String BASIC_PROBLEM_DB_NAME = "basicproblem.db";//数据库名称
    private static DbHelper instance;

    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DaoMaster.DevOpenHelper mHelperAddr;
    private DaoMaster mDaoMasterAddr;
    private DaoSession mDaoSessionAddr;

    private DaoSession mBasicPromblemDaoSession; //获取'问题'基本信息使用
    private DBManager<OffLineLatLngInfo, Long> offLineLatLngInfoDBManager;

    private DBManager<AddressBean, String> mAddresDBManager;


    private DbHelper() {

    }

    public static DbHelper getInstance() {
        if (instance == null) {
            synchronized (DbHelper.class) {
                if (instance == null) {
                    instance = new DbHelper();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public void init(Context context, String dbName) {
        mHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public void initAddr(Context context) {
        mHelperAddr = new DaoMaster.DevOpenHelper(context, DB_NAME_ADD, null);
        mDaoMasterAddr = new DaoMaster(mHelperAddr.getWritableDb());
        mDaoSessionAddr = mDaoMasterAddr.newSession();
    }


    public void initAddr(Context context, String dbName) {
        mHelperAddr = new DaoMaster.DevOpenHelper(context, dbName, null);
        mDaoMasterAddr = new DaoMaster(mHelperAddr.getWritableDb());
        mDaoSessionAddr = mDaoMasterAddr.newSession();
    }
    public DBManager<OffLineLatLngInfo, Long> offLineLatLngInfoLocDBManager() {
        if (offLineLatLngInfoDBManager == null) {
            offLineLatLngInfoDBManager = new DBManager<OffLineLatLngInfo, Long>() {
                @Override
                public AbstractDao<OffLineLatLngInfo, Long> getAbstractDao() {
                    return mDaoSession.getOffLineLatLngInfoDao();
                }
            };
        }
        return offLineLatLngInfoDBManager;
    }





}

