package com.hong.zyh.wisdombj.pager.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hong.zyh.wisdombj.pager.BasePager;


/**
 * 首页
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("智慧服务初始化啦...");

		// 要给帧布局填充布局对象
		TextView view = new TextView(mActivity);
		view.setText("智慧服务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// 修改页面标题
		tvTitle.setText("智慧服务");

		// 隐藏菜单按钮
		btnMenu.setVisibility(View.GONE);
	}

}
