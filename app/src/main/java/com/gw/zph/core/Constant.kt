package com.gw.zph.core

import com.gw.zph.BuildConfig

//各种url地址
const val API_APP_BASE_URL = ""
const val API_BASE_URL = API_APP_BASE_URL //??
const val API_APP_BASE_URL_RAIN = ""
const val API_APP_GLOBEVIEW_URL=""

//云游九州 多时相服务IP
const val API_APP_YYJZ_DSX_URL = ""


//给网络请求添加的请求头名称
const val URL_NAME = "url_name"
const val URL_NAME_DEFAULT = "default"
const val URL_NAME_EMERGENCY = "emergency"
const val URL_NAME_EMERGENCY_RAIN = "emergency_rain"
const val URL_NAME_EMERGENCY_GLOBEVIEW= "emergency_globeview"
const val URL_NAME_EMERGENCY_YYJZ = "emergency_yyjz"
const val URL_NAME_EMERGENCY_GAODE = "emergency_gaode" //高德服务

const val LIST = "list"
const val ROLEINFOLIST = "roleInfoList"
const val DATA = "data"
const val TOKEN = "accessToken"
const val MSG = "msg"
const val SUCCESSSTR = "success"
const val CODE = "code"
const val SUCCESS = "0"
const val ROW = "allrows"
const val DATA_CAPITAL = "DATA"
const val BLANK = ""
//编译常量
const val FLAVOR_FU_JIAN = "fujian"
const val FLAVOR_JIANG_XI = "jiangxi"
const val FLAVOR_ZHE_JIANG = "zhejiang"
const val IS_FLAVOR_FU_JIAN = FLAVOR_FU_JIAN == BuildConfig.FLAVORS
const val IS_FLAVOR_JIANG_XI = FLAVOR_JIANG_XI == BuildConfig.FLAVORS

//键-常量
const val KEY_CACHE_FILE = "cache_status_file"
const val KEY_LOGIN_MODE = "login_mode"
const val KEY_USER_TOKEN = "token_data"
const val KEY_SELECT_ORG = "org_data"
const val APP_UPDATE_APK = "updateApk/" //TODO(待修改)

//数据传参
const val KEY_DATA = "KEY_DATA"
const val KEY_DATA_SECOND = "KEY_DATA_SECOND"
const val KEY_DATA_THREE = "KEY_DATA_THREE"

const val IN_TYPE = "IN_TYPE"
const val INTENT_DATA_CODE = "Intent_Data_Code"
const val IN_TYPE_LAT = "IN_TYPE_LAT"
const val IN_TYPE_LNG = "IN_TYPE_LNG"
const val IN_TYPE_CODE = "IN_TYPE_CODE"
const val IN_TYPE_NAME = "IN_TYPE_NAME"
const val IN_TYPE_TYPE = "IN_TYPE_TYPE"

const val BUNDLE_DATA = "bundleData"
const val BUNDLE_DATA_RETURN = "bundleReturnData"
const val BUNDLE_FLAG = "bundleFlag"
const val BUNDLE_NAME = "bundleName"
const val BUNDLE_DATA_APPEND = "bundleDataAppend"
const val BUNDLE_DATA_APPEND_ADD = "bundleDataAppendAdd"
