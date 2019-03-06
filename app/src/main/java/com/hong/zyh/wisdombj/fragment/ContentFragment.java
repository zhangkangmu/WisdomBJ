package com.hong.zyh.wisdombj.fragment;

import android.view.View;

import com.hong.zyh.wisdombj.R;

/**
 * 替换主activity的主面板的Fragment
 * Created by shuaihong on 2019/3/5.
 */

public class ContentFragment extends BaseFragment {
    @Override
    public View initView() {
        //mActivity是父类传过来的
        View view = View.inflate(mActivity, R.layout.fragment_content_menu,null);
        return view;
    }

    @Override
    public void initData() {

    }
}
