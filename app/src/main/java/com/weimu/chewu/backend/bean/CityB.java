package com.weimu.chewu.backend.bean;

import android.support.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;
import com.weimu.chewu.backend.bean.base.BaseB;

/**
 * Author:你需要一台永动机
 * Date:2018/4/25 00:12
 * Description:
 */
public class CityB extends BaseB implements Comparable<CityB> {
    private String code;
    private String name;
    private String parentPinYin;


    public CityB(String code, String name) {
        this.code = code;
        this.name = name;
        this.parentPinYin = getPinYinFirst(name).substring(0,1);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NonNull CityB o) {
        return getPinYinFirst(this.name).compareTo(getPinYinFirst(o.name));
    }

    public String getPinYinFirst() {
        String[] pinyinArray = Pinyin.toPinyin(this.name, ",").split(",");
        StringBuilder stb = new StringBuilder();
        for (String item : pinyinArray) {
            stb.append(item.charAt(0));
        }
        return stb.toString();
    }

    public String getPinYinFirst(String name) {
        String[] pinyinArray = Pinyin.toPinyin(name, ",").split(",");
        StringBuilder stb = new StringBuilder();
        for (String item : pinyinArray) {
            stb.append(item.charAt(0));
        }
        return stb.toString();
    }

    public String getParentPinYin() {
        return parentPinYin;
    }

    public void setParentPinYin(String parentPinYin) {
        this.parentPinYin = parentPinYin;
    }
}
