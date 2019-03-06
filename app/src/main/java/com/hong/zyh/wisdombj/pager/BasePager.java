package com.hong.zyh.wisdombj.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hong.zyh.wisdombj.R;

/**
 * 五个页面的基类
 * Created by shuaihong on 2019/3/6.
 */

public class BasePager {

    public Activity mActivity;

    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContent;// 空的帧布局对象, 要动态添加布局
    public View mRootView;// 当前页面的布局对象

    //在构造方法中给变量设置mRootView，以便其他类中使用这个变量，不用担心initView（）和构造方法的执行顺序
    //https://www.cnblogs.com/lyxcode/p/9016322.html,执行顺序可以看下这个网站
    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView=initView();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        return view;
    }
    // 初始化数据
    public void initData() {

    }
}
