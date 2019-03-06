package com.hong.zyh.wisdombj.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hong.zyh.wisdombj.MainActivity;
import com.hong.zyh.wisdombj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import domain.NewsMenu;

/**
 * 侧边栏fragment
 * Created by shuaihong on 2019/3/5.
 */

public class LeftMenuFragment extends BaseFragment {

    //通过增加注解的方式实现findViewById
    @ViewInject(R.id.lv_list)
    private ListView lvList;

    // 当前被选中的item的位置
    private int mCurrentPos;
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;// 侧边栏网络数据对象
    private LeftMenuAdapter mLeftAdapter;

    @Override
    public View initView() {
        //mActivity是父类传过来的
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
        //ViewUtils模块：android的ioc框架，完全注解的方式就可以进行UI，资源和事件绑定
//        lvList = view.findViewById(R.id.lv_list);

        ViewUtils.inject(this, view);// 注入view和事件
        return view;
    }

    @Override
    public void initData() {

    }

    //给左侧面板设置数据的方法，让新闻或者其他页面的pager调用
    public void setData() {

    }

    // 给侧边栏设置数据，这个方法是给新闻等其他页面调用的，
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        //当前选中的位置归零
        mCurrentPos = 0;
        // 更新页面
        mNewsMenuData=data;
        Log.d("setMenuData",mNewsMenuData.toString());

        mLeftAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mLeftAdapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mCurrentPos = position;// 更新当前被选中的位置
                mLeftAdapter.notifyDataSetChanged();// 刷新listview

                // 收起侧边栏
                toggle();
            }
        });

    }

    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
    }


    //左侧面板的listview适配器
    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu,null);
            TextView tvMenu = view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item=(NewsMenu.NewsMenuData)getItem(position);
            Log.d("LeftMenu-getView",item.toString());
            tvMenu.setText(item.title);

            if (position == mCurrentPos) {
                // 被选中
                tvMenu.setEnabled(true);// 文字变为红色
            } else {
                // 未选中
                tvMenu.setEnabled(false);// 文字变为白色
            }

            return view;
        }
    }
}
