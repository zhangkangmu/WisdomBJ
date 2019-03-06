package com.hong.zyh.wisdombj.utils;

import android.content.Context;

/**
 * 网络缓存工具类
 * Created by shuaihong on 2019/3/6.
 */

public class CacheUtils {

    /**
     * 以url为key, 以json为value,保存在本地
     *
     * @param url
     * @param json
     */
    public static void setCache(String url, String json, Context context) {
        PrefUtils.setString(context, url, json);
    }

    /**
     * 获取缓存
     *
     * @param url
     * @param context
     * @return
     */
    public static String getCache(String url, Context context) {
        String cacheDatas = PrefUtils.getString(context, url, null);
        return cacheDatas;
    }
}
