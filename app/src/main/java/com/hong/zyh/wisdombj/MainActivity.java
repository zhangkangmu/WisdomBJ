package com.hong.zyh.wisdombj;

import android.os.Bundle;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


/**
 * 主页面
 *
 * @author shuaihong
 * @date 2019-03-05
 */
public class MainActivity extends SlidingFragmentActivity{

	//SlidingFragmentActivity
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 必须在setContentView之前调用
		setContentView(R.layout.activity_main);

		//Utils.doSomthing();
		//R.drawable.p_10
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
		slidingMenu.setBehindOffset(200);//屏幕预留200像素宽度
	}
}
