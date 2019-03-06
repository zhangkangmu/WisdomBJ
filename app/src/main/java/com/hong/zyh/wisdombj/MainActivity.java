package com.hong.zyh.wisdombj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hong.zyh.wisdombj.fragment.ContentFragment;
import com.hong.zyh.wisdombj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by shuaihong on 2019/3/4.
 */

public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_LEFT_MENU="LEFTMENUFRAGMENT";
    private static final String TAG_CONTENT="CONTENTFRAGMENT";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置左边的布局
        setBehindContentView(R.layout.left_menu);
        //获取SlidingMenu方法
        SlidingMenu slidingMenu = getSlidingMenu();
        //全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //屏幕预留200像素宽度,是主屏，不是侧滑的面板
        slidingMenu.setBehindOffset(500);

        initFragment();
    }

    private void initFragment() {
        //获取FragmentManager对象，getSupportFragmentManager用这个方法可以支持的版本的
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开始事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //注意：是由FragmentTransaction进行替换的，使用三个参数的包含tag的，下次可以通过tag找到这个fragment
        //R.id.fl_left_menu,注意这里是id，不是layout
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
        transaction.commit();

        //根据标记找到对应的fragment
        // Fragment fragment =fm.findFragmentByTag(TAG_LEFT_MENU);
    }
}
