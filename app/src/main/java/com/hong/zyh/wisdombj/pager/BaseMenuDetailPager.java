package com.hong.zyh.wisdombj.pager;

import android.app.Activity;
import android.view.View;

/**
 * 左侧的菜单详情页基类
 * Created by shuaihong on 2019/3/7.
 */

public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    // 菜单详情页根布局
    public View mRootView;

    public BaseMenuDetailPager(Activity activity){
        mActivity=activity;
        mRootView = initView();
    }
    public  abstract View initView();
    public void initData(){

    }

}
