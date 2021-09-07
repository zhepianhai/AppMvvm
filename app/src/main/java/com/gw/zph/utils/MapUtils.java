package com.gw.zph.utils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.gw.zph.R;
import com.gw.zph.ui.home.map.TrackedActivity;

import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    /**
     * 获取政区编码，直辖市的处理方法为取前两位
     * 省级的正常处理
     * 11 北京
     * 12 天津
     * 31 上海
     * 50 重庆
     * 81 香港
     * 82  澳门 特殊处理
     */
    public static final String getCityCode(String rgncd) {
        int adRgncd = getCityAdCode(rgncd);
        if (adRgncd == 11 || adRgncd == 12 || adRgncd == 31 || adRgncd == 50 || adRgncd == 81 || adRgncd == 82) {
            return getRgncd12(adRgncd + "");
        }
        String result = rgncd;
        if (rgncd.length() == 0) {
            return result;
        }
        if (result.length() < 4) {
            return getRgncd12(rgncd);
        }
        return getRgncd12(result);
    }
    /**
     * 取政区前两位
     */
    public static final int getCityAdCode(String rgncd) {
        String result = rgncd;
        if (rgncd.length() == 0) {
            return 0;
        }
        if (result.length() < 2) {
            return JSDateUtil.getDataIntByObj(rgncd);
        }
        result = result.substring(0, 2);
        return JSDateUtil.getDataIntByObj(result);
    }
    /**
     * 获取12位政区编码
     */

    public static final String getRgncd12(String rgncd) {
        if (rgncd == null) {
            return "110000000000";
        }
        String result = rgncd;
        if (rgncd.length() == 0) {
            return result;
        }
        if (rgncd.length() < 12) {
            for (int i = 0; i < 12 - rgncd.length(); ++i) {
                result += "0";
            }
        }
        return result;
    }



    /**
     * 添加人员轨迹
     * 同时根据墨卡托坐标计算缩放级别
     */
    public static void addTracker(AMap mAMap, List<LatLng> item,TrackedActivity activity) {
        ThreadPoolUtil.execute(() -> {
            try {
                if (null != item&& item.size() > 0) {
                    double lat_min = 0, lat_max = 0, lon_min = 0, lon_max = 0;
                    double lat0 = item.get(0).latitude;
                    double lon0 = item.get(0).longitude;
                    lat_min = lat0;
                    lat_max = lat0;
                    lon_min = lon0;
                    lon_max = lon0;

                    float distance = 0f;
                    for (int i = 0; i < item.size() - 1; ++i) {
                        if (i == 0) {
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_star);
                            MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 1.0f)
                                    .position(item.get(0))
                                    .snippet("")
                                    .icon(icon)
                                    .draggable(false).period(10);
                            mAMap.addMarker(markerOption);
                        }
                        if (i == item.size() - 2) {
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_end);
                            MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 1.0f)
                                    .position(item.get(item.size() - 1))
                                    .snippet("")
                                    .icon(icon)
                                    .draggable(false).period(10);
                            mAMap.addMarker(markerOption);
                        }
                        mAMap.addPolyline(MapUtils.GetPolylineTextureOptions()
                                .add(item.get(i), item.get(i + 1))
                        );
                        distance += AMapUtils.calculateLineDistance(item.get(i), item.get(i + 1));

                    }
                    activity.refreshCurTexture(distance, item);
                    //计算缩放级别
                    float mapLevel = Constants.MAP_SCALE;
                    double lat_span = lat_max - lat_min;
                    double lon_span = lon_max - lon_min;
                    if (lat_span > 0 && lat_span > lon_span) {
                        mapLevel = (int) (Math.log10(255.0 / lat_span) / Math.log10(2)) + 2;
                    } else if (lon_span > 0 && lon_span > lat_span) {
                        mapLevel = (int) (Math.log10(255.0 / lon_span) / Math.log10(2)) + 2;
                    }
                    if (mapLevel > 16) {
                        mapLevel = 16;
                    }
                    LatLng center = new LatLng((lat_max + lat_min) / 2, (lon_max + lon_min) / 2);
                    if (center.longitude == 0 || center.latitude == 0) {
                        return;
                    }
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                            new CameraPosition(
                                    center
                                    , mapLevel, 0, 0));
                    mAMap.moveCamera(mCameraUpdate);//缩放地图到指定的缩放级别

                }
            } catch (Exception e) {
            }
        });
    }


    /**
     * 地图纹理图片设置
     */
    public static PolylineOptions GetPolylineTextureOptions() {
        try {
            //添加纹理图片
            List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
            BitmapDescriptor mRedTexture = BitmapDescriptorFactory
                    .fromAsset("ic_map_texture_blue.png");
            BitmapDescriptor mBlueTexture = BitmapDescriptorFactory
                    .fromAsset("ic_map_texture_green.png");
            BitmapDescriptor mGreenTexture = BitmapDescriptorFactory
                    .fromAsset("ic_map_texture_red.png");
            textureList.add(mRedTexture);
            textureList.add(mBlueTexture);
            textureList.add(mGreenTexture);
            // 添加纹理图片对应的顺序
            List<Integer> textureIndexs = new ArrayList<Integer>();
            textureIndexs.add(0);
            textureIndexs.add(1);
            textureIndexs.add(2);
            PolylineOptions polylienOptions = new PolylineOptions();
            polylienOptions.setCustomTextureList(textureList);
            polylienOptions.setCustomTextureIndex(textureIndexs);
            polylienOptions.setUseTexture(true);
            polylienOptions.width(7.0f);
            return polylienOptions;
        } catch (Exception e) {
        }
        return null;
    }
}
