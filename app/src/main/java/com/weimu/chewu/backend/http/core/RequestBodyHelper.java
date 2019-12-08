package com.weimu.chewu.backend.http.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Retrofit发送json数据
 */
public class RequestBodyHelper {
    private JsonObject mRawJson;

    public RequestBodyHelper() {
        mRawJson = new JsonObject();
    }


    /**
     * json数组
     *
     * @param key
     * @param value
     */
    public RequestBodyHelper addRaw(String key, JsonArray value) {
        if (mRawJson == null) {
            mRawJson = new JsonObject();
        }

        mRawJson.add(key, value);
        return this;
    }

    /**
     * 字符串
     *
     * @param key
     * @param value
     */
    public RequestBodyHelper addRaw(String key, String value) {
        if (mRawJson == null) {
            mRawJson = new JsonObject();
        }

        mRawJson.addProperty(key, value);
        return this;
    }

    /**
     * 整形
     *
     * @param key
     * @param value
     */
    public RequestBodyHelper addRaw(String key, Integer value) {
        if (mRawJson == null) {
            mRawJson = new JsonObject();
        }
        mRawJson.addProperty(key, value);
        return this;
    }

    /**
     * 整形
     *
     * @param key
     * @param value
     */
    public RequestBodyHelper addRaw(String key, Long value) {
        if (mRawJson == null) {
            mRawJson = new JsonObject();
        }
        mRawJson.addProperty(key, value);
        return this;
    }

    /**
     * 浮点型
     *
     * @param key
     * @param value
     */
    public RequestBodyHelper addRaw(String key, Double value) {
        if (mRawJson == null) {
            mRawJson = new JsonObject();
        }
        mRawJson.addProperty(key, value);
        return this;
    }

    public RequestBody builder() {
        JsonObject body = new JsonObject();
        body.addProperty("data", mRawJson.toString());
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), body.toString());
    }

}
