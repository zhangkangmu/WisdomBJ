package com.hong.zyh.wisdombj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;

import com.hong.zyh.wisdombj.MainActivity;
import com.hong.zyh.wisdombj.R;
import com.hong.zyh.wisdombj.pager.BasePager;
import com.hong.zyh.wisdombj.pager.impl.GovAffairsPager;
import com.hong.zyh.wisdombj.pager.impl.HomePager;
import com.hong.zyh.wisdombj.pager.impl.NewsCenterPager;
import com.hong.zyh.wisdombj.pager.impl.SettingPager;
import com.hong.zyh.wisdombj.pager.impl.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * 替换主activity的主面板的Fragment
 * Created by shuaihong on 2019/3/5.
 */

public class ContentFragment extends BaseFragment {

    private ViewPager mViewPager;
    //五个标签页的集合
    private ArrayList<BasePager> mPagers;
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        //mActivity是父类传过来的
        View view = View.inflate(mActivity, R.layout.fragment_content_menu,null);
        mViewPager = view.findViewById(R.id.vp_content);
        rgGroup = view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<BasePager>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        // 手动加载第一页数据
        mPagers.get(0).initData();
        setSlidingMenuEnable(false);

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        // 首页
                        // mViewPager.setCurrentItem(0);
                        mViewPager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                        break;
                    case R.id.rb_news:
                        // 新闻中心
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        // 智慧服务
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        // 政务
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        // 设置
                        mViewPager.setCurrentItem(4, false);
                        break;

                    default:
                        break;
                }
            }
        });

        //viewpager页面完成监听，切换完成的时候才初始化数据，否则viewpager默认的自动缓左右两个页面的数据
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager basePager = mPagers.get(position);
                basePager.initData();
                if (position == 0 || position == mPagers.size() - 1) {
                    // 首页和设置页要禁用侧边栏
                    setSlidingMenuEnable(false);
                } else {
                    // 其他页面开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

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

    //ViewPager的适配器
    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mPagers.get(position);
            // 获取当前页面对象的布局
            View view = basePager.mRootView;
            // basePager.initData();// 初始化数据, viewpager会默认加载下一个页面,
            // 为了节省流量和性能,不要在此处调用初始化数据的方法
            //不要忘记这个addview了
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}
