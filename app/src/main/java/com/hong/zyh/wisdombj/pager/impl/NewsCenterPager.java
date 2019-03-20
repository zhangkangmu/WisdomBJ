package com.hong.zyh.wisdombj.pager.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hong.zyh.wisdombj.MainActivity;
import com.hong.zyh.wisdombj.fragment.LeftMenuFragment;
import com.hong.zyh.wisdombj.global.GlobalConstants;
import com.hong.zyh.wisdombj.pager.BaseMenuDetailPager;
import com.hong.zyh.wisdombj.pager.BasePager;
import com.hong.zyh.wisdombj.pager.menu.InteractMenuDetailPager;
import com.hong.zyh.wisdombj.pager.menu.NewsMenuDetailPager;
import com.hong.zyh.wisdombj.pager.menu.PhotosMenuDetailPager;
import com.hong.zyh.wisdombj.pager.menu.TopicMenuDetailPager;
import com.hong.zyh.wisdombj.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

import com.hong.zyh.wisdombj.domain.NewsMenu;


/**
 * 新闻页面
 * 
 *  Created by shuaihong on 2019/3/7.
 */
public class NewsCenterPager extends BasePager {

    //mNewsData是通过Gson解析完的数据类
    private NewsMenu mNewsData;

    // 菜单详情页集合
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;

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

		//从缓存文件中获取数据
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
         //有数据就请求解析
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
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
        MainActivity mainUI= (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        //把获得的解析类传给侧滑面板。给侧边栏设置数据
        leftMenuFragment.setMenuData(mNewsData.data);

        // 初始化4个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        // 将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);
    }

    /**
     * 修改详情页布局，主要让其他类调用方便修改，比如说LeftMenuFragment调用
     */
    public void setCurrentDetailPager(int position) {
        // 重新给frameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);// 获取当前应该显示的页面
        View view = pager.mRootView;// 当前页面的布局

        // 清除之前旧的布局,一定要有这个方法，否则会重叠在一起
        flContent.removeAllViews();
        flContent.addView(view);// 给帧布局添加布局
        // 初始化页面数据
        pager.initData();

        // 更新标题。是从网络数据中获得的
        tvTitle.setText(mNewsData.data.get(position).title);
    }
}
