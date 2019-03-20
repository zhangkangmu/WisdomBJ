package com.hong.zyh.wisdombj.pager.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.hong.zyh.wisdombj.MainActivity;
import com.hong.zyh.wisdombj.R;
import com.hong.zyh.wisdombj.pager.BaseMenuDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import com.hong.zyh.wisdombj.domain.NewsMenu;


/**
 * 菜单详情页-新闻
 *  ViewPagerIndicator使用流程:
 *  1.引入库
 *  2.解决support-v4冲突(让两个版本一致)
 *  3.从例子程序中拷贝布局文件
 * 4.从例子程序中拷贝相关代码(指示器和viewpager绑定; 重写getPageTitle返回标题)
 * 5.在清单文件中增加样式
 * 6.背景修改为白色
 * 7.修改样式-背景样式&文字样式
 *
 * Created by shuaihong on 2019/3/7.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{

	@ViewInject(R.id.vp_news_menu_detail)
	ViewPager mViewPager;

	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;

	private ArrayList<TabDetailPager> mPagers;// 页签页面集合

	private ArrayList<NewsMenu.NewsTabData> mTabData;// 页签网络数据

	public NewsMenuDetailPager(Activity activity,ArrayList<NewsMenu.NewsTabData> children) {
		super(activity);
		mTabData = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail,null);
		ViewUtils.inject(this,view);
		return view;
	}

	@Override
	public void initData() {
		// 初始化页签
		mPagers = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,
					mTabData.get(i));
			mPagers.add(pager);
		}

		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		// 将viewpager和指示器绑定在一起.注意:必须在viewpager设置完数据之后再绑定
		mIndicator.setViewPager(mViewPager);

		//mViewPager.setOnPageChangeListener(this);
		// 此处必须给指示器设置页面监听,不能设置给viewpagers，因为指示器已经个别mViewPager关联起来了
		mIndicator.setOnPageChangeListener(this);
	}

	//页面滑动监听的方法start
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		System.out.println("当前位置:" + position);
		if (position == 0) {
			// 开启侧边栏
			setSlidingMenuEnable(true);
		} else {
			// 禁用侧边栏
			setSlidingMenuEnable(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
	//页面滑动监听的方法end

	/**
	 * 开启或禁用侧边栏
	 *
	 * @param enable
	 */
	protected void setSlidingMenuEnable(boolean enable) {
		// 获取侧边栏对象
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	class NewsMenuDetailAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {
			// 指定指示器的标题
			NewsMenu.NewsTabData newsTabData = mTabData.get(position);
			return newsTabData.title;
		}

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagers.get(position);

			View view = pager.mRootView;
			container.addView(view);

			pager.initData();

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return object==view;
		}
	}

	//另外一种设计点击事件，xUtils里的方法
	@OnClick(R.id.btn_next)
	public void nextPager(View view){
		// 跳到下个页面
		int currentItem = mViewPager.getCurrentItem();
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}
}
