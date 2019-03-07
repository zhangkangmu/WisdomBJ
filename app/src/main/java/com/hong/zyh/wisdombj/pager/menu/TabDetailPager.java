package com.hong.zyh.wisdombj.pager.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hong.zyh.wisdombj.pager.BaseMenuDetailPager;

import domain.NewsMenu;


/**
 * Created by shuaihong on 2019/3/7.
 */

public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData;// 单个页签的网络数据
    private TextView view;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initView() {
        view = new TextView(mActivity);
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public void initData() {
        view.setText(mTabData.title);
    }
}
