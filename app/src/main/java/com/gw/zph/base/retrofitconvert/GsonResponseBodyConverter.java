package com.gw.zph.base.retrofitconvert;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        String resultString = value.string();
        JsonReader jsonReader = gson.newJsonReader(new StringReader(resultString));
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        }catch (JsonSyntaxException exception){
            exception.printStackTrace();
            JsonElement jElement = new JsonParser().parse(resultString);
            if (jElement instanceof JsonObject){
                ((JsonObject)jElement).remove("data"); //如果json解析出错,则解析之前，直接把data 字段移除掉
                String message = ((JsonObject)jElement).get("message").toString();
                if (TextUtils.isEmpty(message)){ //如果返回的结果中不包含message字段，并且此时已经发生了解析异常，则添加自定义的message
                    ((JsonObject)jElement).addProperty("message", "返回结果解析异常");
                }
            }
            try {//移除data字段之后再尝试解析
                return adapter.read(gson.newJsonReader(new StringReader(jElement.toString())));
            }finally {
                value.close();
            }
        } finally {
            value.close();
        }
    }
}
