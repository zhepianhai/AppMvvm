///*
// * Copyright (C) 2015 Square, Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.gw.slbdc.base.retrofitconvert;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.gw.safty.common.utils.JsonUtils;
//import com.gw.slbdc.core.ConstantKt;
//
//import org.json.JSONException;
//import org.json.JSONTokener;
//
//import java.io.IOException;
//import java.io.StringReader;
//
//import okhttp3.ResponseBody;
//import retrofit2.Converter;
//
//final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
//    private final Gson gson;
//    private final TypeAdapter<T> adapter;
//
//    private String TAG = GsonResponseBodyConverter.class.getSimpleName();
//
//    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
//        this.gson = gson;
//        this.adapter = adapter;
//    }
//
//    @Override
//    public T convert(ResponseBody value) throws IOException {
////        JsonReader jsonReader = gson.newJsonReader(value.charStream());
////        try {
////            return adapter.read(jsonReader);
////        } finally {
////            value.close();
////        }
//
//        try {
//            //TODO：此处将接口返回的json字符串进行反序列化为对象，按需改造
//            /** 接口返回结构如下
//             {
//             "code": "",
//             "msg": "",
//             "total": "",
//             "rows": []
//             }
//             ** 需改造的结构如下
//             {
//             "code": "",
//             "msg": "",
//             "data":
//             {
//             "list":[]
//             }
//             }
//             */
//            JsonObject jsonValue = (JsonObject) new JsonParser().parse(value.string());
//
//            JsonObject jsonRtValue = new JsonObject();
////            String strCode = jsonValue.get("code").getAsString();
////            String strMsg = jsonValue.get("msg").getAsString();
//////            String strTotal = jsonValue.get("total").getAsString();//total暂时无用
////            jsonRtValue.addProperty("code",strCode);
////            jsonRtValue.addProperty("msg",strMsg);
//            jsonRtValue.addProperty(ConstantKt.CODE, ConstantKt.SUCCESS);
//            jsonRtValue.addProperty(ConstantKt.MSG, ConstantKt.BLANK);
//
////            Timber.i(jsonValue.toString());
//            if (jsonValue.get(ConstantKt.CODE) != null) {
//
////                Timber.i(TAG + "jsonValue.get(ConstantKt.CODE) != null-->");
//                //存在code，为第二种接口标准
////                JsonReader jsonReader = gson.newJsonReader(value.charStream());
////                return adapter.read(jsonReader);
//
//
//
//                if (jsonValue.get(ConstantKt.DATA) == null || jsonValue.get(ConstantKt.DATA).toString().equals("null")) {
//                    JsonReader jsonReader = gson.newJsonReader(new StringReader(jsonValue.toString()));
//                    return adapter.read(jsonReader);
//                } else {
//                    JSONTokener jsonTokener = new JSONTokener(jsonValue.get(ConstantKt.DATA).toString());
//                    Object object = null;
//                    try {
//                        object = jsonTokener.nextValue();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    JsonObject jsData;
//                    if (JsonUtils.isJSONArray(object.toString())) {
//
//                        jsData = new JsonObject();
//                        jsData.add(ConstantKt.LIST, jsonValue.get(ConstantKt.DATA));
//                    } else {
//                        jsData = jsonValue.get(ConstantKt.DATA).getAsJsonObject();
//
////                    JsonObject jsObj =
////                    jsonRtValue.add(ConstantKt.DATA, jsObj);
//                    }
//                    jsonRtValue.add(ConstantKt.DATA, jsData);
//                    jsonRtValue.add(ConstantKt.TOKEN, jsonValue.get(ConstantKt.TOKEN));
//                    /**
//                     * 字段为success的时候没有这个字段
//                     */
//                    jsonRtValue.add(ConstantKt.SUCCESSSTR, jsonValue.get(ConstantKt.SUCCESSSTR));
//
//                    JsonReader jsonReader = gson.newJsonReader(new StringReader(jsonRtValue.toString()));
//                    return adapter.read(jsonReader);
//                }
//            } else {
//                //不存在code，为第一种标准
////                Timber.i(TAG + "jsonValue.get(ConstantKt.ROW)-->");
//
//                if (jsonValue.get(ConstantKt.ROW) == null) {
//                    //没有row字段
//                    if (jsonValue.get(ConstantKt.DATA_CAPITAL) != null) {
//                        if (jsonValue.get(ConstantKt.DATA_CAPITAL).isJsonArray()) {
//                            //DATA 是json数组
//                            JsonArray jsRows = jsonValue.get(ConstantKt.DATA_CAPITAL).getAsJsonArray();
//                            JsonObject jsonData = new JsonObject();
//                            jsonData.add(ConstantKt.LIST, jsRows);
//                            jsonRtValue.add(ConstantKt.DATA, jsonData);
//                        } else if (jsonValue.get(ConstantKt.DATA_CAPITAL).isJsonObject()) {
//                            //DATA 是json对象
//                            JsonObject jsObj = jsonValue.get(ConstantKt.DATA_CAPITAL).getAsJsonObject();
//                            jsonRtValue.add(ConstantKt.DATA, jsObj);
//                        } else {
//                            //未知
//                        }
//                    } else {
//                        if (jsonValue.get(ConstantKt.DATA) != null) {
//                            //data 是json对象
//                            try {
////                                String jsStr = jsonValue.get(ConstantKt.DATA).toString();
////                                JsonObject jsObj = new JsonObject();
////                                jsObj.addProperty("id", jsStr);
//
//                                JsonObject jsObj = jsonValue.get(ConstantKt.DATA).getAsJsonObject();
//                                jsonRtValue.add(ConstantKt.DATA, jsObj);
////                                jsonRtValue.add(ConstantKt.DATA, jsObj);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        //不是我们的接口标准的，按原接口内容返回
//                        else{
//                            try {
//                                jsonRtValue.add(ConstantKt.DATA, jsonValue);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                } else {
//                    if (jsonValue.get(ConstantKt.ROW).isJsonArray()) {
//                        //TODO:根据total确定data数据是json对象或者json数组，条件不准
//                        //TODO:尝试判断rows为json对象或者json数组
//                        //表示json数组
//                        JsonArray jsRows = jsonValue.get(ConstantKt.ROW).getAsJsonArray();
//                        JsonObject jsonData = new JsonObject();
////                jsonData.addProperty("total",strTotal);//total暂时无用
//                        jsonData.add(ConstantKt.LIST, jsRows);
//                        jsonRtValue.add(ConstantKt.DATA, jsonData);
//                    } else if (jsonValue.get(ConstantKt.ROW).isJsonObject()) {
//                        //表示json对象
//                        JsonObject jsObj = jsonValue.get(ConstantKt.ROW).getAsJsonObject();
//                        jsonRtValue.add(ConstantKt.DATA, jsObj);
//                    }
//                }
//
//
//                JsonReader jsonReader = gson.newJsonReader(new StringReader(jsonRtValue.toString()));
//                return adapter.read(jsonReader);
//            }
//
//        } finally {
//            value.close();
//        }
//    }
//}
