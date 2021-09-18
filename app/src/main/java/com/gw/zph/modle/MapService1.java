package com.gw.zph.modle;

import com.gw.safty.common.network.BaseResponse;
import com.gw.safty.common.network.CommonResponse;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.core.ConstantKt;
import com.gw.zph.utils.NetConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MapService1 {
    @Headers({ConstantKt.URL_NAME+ConstantKt.COLON+ConstantKt.URL_NAME_EMERGENCY})
    @POST("bis/tracked/listInsert")
    Call<CommonResponse> uploadPositionList(@Body List<OffLineLatLngInfo> bean);

}
