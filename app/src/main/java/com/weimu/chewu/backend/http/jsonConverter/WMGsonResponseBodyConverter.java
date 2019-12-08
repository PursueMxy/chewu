/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.weimu.chewu.backend.http.jsonConverter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.orhanobut.logger.Logger;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.PageB;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class WMGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private Type type;

    WMGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
        this.gson = gson;
        this.adapter = adapter;
        this.type = type;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        String result = value.string();

        //todo 主要修改的地方
        //Logger.d("读取内容\n" + result);

        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(result);
        if (TextUtils.isEmpty(result) || result.equalsIgnoreCase("null") || result.equals("{}") || rootElement.isJsonNull()) {
            return null;
        }
        JsonObject rootObj = rootElement.getAsJsonObject();


        //是否为泛型 NormalResponse
        if (type instanceof ParameterizedType) {
            JsonElement data = rootObj.get("data");
            Type[] entityClass = ((ParameterizedType) type).getActualTypeArguments();

            //是否包含泛型 Page,List
            if (entityClass[0] instanceof ParameterizedType) {

                Type type = ((ParameterizedType) entityClass[0]).getRawType();//获取泛型类型
                if (type == List.class || type == ArrayList.class) {
                    Log.e("weimu","集合类型");
                }else{
                    if (data.isJsonPrimitive() && TextUtils.isEmpty(data.getAsString())) {
                        rootObj.add("data", new JsonObject());
                    } else if (data.isJsonArray() && data.getAsJsonArray().size() == 0) {
                        rootObj.add("data", new JsonObject());
                    }
                }

            }else if (((ParameterizedType) type).getRawType()==PageB.class){
                //不做处理
            }else {
                //String,UserB
                Type type = entityClass[0];
                if (type == String.class) {
                    if (data.isJsonPrimitive() && TextUtils.isEmpty(data.getAsString())) {
                        rootObj.addProperty("data", "");
                    } else if (data.isJsonArray() && data.getAsJsonArray().size() == 0) {
                        rootObj.addProperty("data", "");
                    }
                } else {
                    if (data.isJsonPrimitive() && TextUtils.isEmpty(data.getAsString())) {
                        rootObj.add("data", new JsonObject());
                    } else if (data.isJsonArray() && data.getAsJsonArray().size() == 0) {
                        rootObj.add("data", new JsonObject());
                    }
                }

            }
        }


        //JsonReader jsonReader = new JsonReader(value.charStream());

        try {
            //todo 可以关掉
            Log.e("weimu", "转成标准json \n" + rootObj.toString());
            return new Gson().fromJson(rootObj.toString(), type);
            //return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
