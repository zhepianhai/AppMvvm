package com.gw.safty.common.utils;

public class KeyConst {
    public static final String USER_ID = "userId";// 上报人编码(可多选，中间用英文的隔开)
    public static final String USER_NAME = "userName";// 上报人
    public static final String AD_CODE = "adCode";// 行政区编码
    public static final String AD_NAME = "adName";// 行政区编码对应名称
    public static final String AD_FULL_NAME = "adFullName";// 行政区编码全部名字
    public static final String AD_WHOLE_NAME = "adWholeName";// 行政区编码全部名字
    public static final String OBJECT_TYPE = "objectType";// REA:河段；LKS：湖片
    public static final String OBJECT_ID = "objectId";// 对象id
    public static final String EV_TYPE = "evType";// 问题类型大类(可以多选，格式为：1，2，3)
    public static final String EVENT_TYPES = "eventTypes";// 问题类型小类(可以多选，格式为：11，12，13)
    public static final String KEY_WORD = "keyWord";// 关键词
    public static final String FIND_TS_START = "findTsStart";// 上报问题时间从(格式：yyyy-MM-ddHH:mm:ss，如：2018-07-0813:14:14)
    public static final String FIND_TS_END = "findTsEnd";// 上报问题时间到(格式：yyyy-MM-ddHH:mm:ss，如：2018-07-0813:14:14)
    public static final String STATUS = "status";// 处理状态（新增的数据是0，已提交是1，已经下发是2，已上传文件是3，超期是4，已销号是9）（针对巡河与四乱，已完结代表已销号9是销号其他是没销号）
    public static final String BASIN = "basin";// 流域（流域对应的省级行政区编码的前两位，中间用英文的隔开，如太湖局为：313335）
    public static final String REPO_GROUP = "repoGroup";// 上报人分组
    public static final String ORDER_BY = "orderBy";// 上报人分组

    //地下水
    public static final String SORT_CONDITION = "SORT_CONDITION";
    public static final String SORT_SORTORD = "SORT_SORTORD";
    public static final String HYDRO_SELECT = "HYDRO_SELECT";
    //地下水
    /**
     * 水闸相关条件
     */
    /**
     * 水闸类型
     */
    public static final String sluiceType="SLUICE_TYPE";
    /**
     * 工程规模
     */
    public static final String engineeringScale="ENGINEERING_SCALE";
    /**
     * 建设情况
     */
    public static final String constructeSituate="CONSTRUCT_SITUATE";
    /**
     * 排序条件
     */
    public static final String sortCriteria="SORT_CRITERIA";
    /**
     * 排序方式
     */
    public static final String sortOrder="SORT_ORDER";

    /**
     * 筛选中新增状态
     */
    public static final String reservoirState="RESERVOIR_STATE";

    /**
     * 水毁中
     */
    public static final String WATER_WTDSTSTATE = "WATER_WTDSTSTATE";

    /**
     * 用于 取水证状态 筛选的键
     */
    public static final String FILTER_LICENCE_STATE = "FILTER_LICENCE_STATE";

    /**
     * 新人饮中添加督查对象
     */
    public static final String IS_PKX = "IS_PKX";
    /**
     * 新人饮中问题过滤
     */
    public static final String VILL_TYPE = "VILL_TYPE";

    /**
     * 新人饮中问题过滤
     */
    public static final String MFDP_inspPblmType = "MFDP_inspPblmType";
    public static final String MFDP_CheckPoints = "MFDP_CheckPoints";
    public static final String MFDP_inspPblmCate = "MFDP_inspPblmCate";
    public static final String MFDP_ifCasePblm = "MFDP_ifCasePblm";
    /**
     * 筛选中是否贫困县
     */
    public static final String poorCountry="poorCountry_STATE";
    /**
     * 30超洪
     */
    public static final String EFP_TYPE="EFP_TYPE";


    /**
     * 质量监督相关
     * */
    public static final String QSS_ATT="qss_att";
    public static final String QSS_PASI="qss_pasi";
    public static final String QSS_TYPE="qss_type";
    public static final String QSS_SGGTN="qss_pblmSggtn";
    public static final String QSS_PBLM_ATT="qss_pblmAtt";
}
