package com.hong.zyh.wisdombj.fragment;

import android.view.View;

import com.hong.zyh.wisdombj.R;

/**
 * 侧边栏fragment
 * Created by shuaihong on 2019/3/5.
 */

public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        //mActivity是父类传过来的
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
        return view;
    }

    @Override
    public void initData() {

    }
}
