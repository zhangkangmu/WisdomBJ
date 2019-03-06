package com.hong.zyh.wisdombj.pager.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hong.zyh.wisdombj.global.GlobalConstants;
import com.hong.zyh.wisdombj.pager.BasePager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import domain.NewsMenu;


/**
 * 首页
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class NewsCenterPager extends BasePager {

    private NewsMenu mNewsData;

    public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("新闻页面初始化啦...");

		// 要给帧布局填充布局对象
		TextView view = new TextView(mActivity);
		view.setText("新闻");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// 修改页面标题
		tvTitle.setText("新闻");

		// 显示菜单按钮
		btnMenu.setVisibility(View.VISIBLE);

        // 请求服务器,获取数据
        // 开源框架: XUtils
        getDataFromServer();
	}

	/**
	 * 从服务器获取数据 需要权限:<uses-permission android:name="android.permission.INTERNET"
	 * />
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		//参数1：请求方式 参数2：uil地址   参数3：回调(返回的是json的字符串数据，因此繁星应该是String)
        //HttpMethod导入的方法是：com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
		utils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 请求成功
                String result = responseInfo.result;// 获取服务器返回结果
                Log.d("服务器返回结果:",result);
                //开始解析数据
                processData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                 // 请求失败
                error.printStackTrace();
                Log.d("服务器返回error:",msg);
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
	}

    protected void processData(String json) {
        Gson gson= new Gson();
        //mNewsData已经把所有的字段填充好了
        //参数1：jon原数据，相当于字符串  参数2：封装json数据的类
        mNewsData = gson.fromJson(json, NewsMenu.class);
        Toast.makeText(mActivity,mNewsData.toString(),Toast.LENGTH_SHORT).show();
    }
}
