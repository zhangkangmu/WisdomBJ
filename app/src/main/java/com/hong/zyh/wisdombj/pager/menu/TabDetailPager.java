package com.hong.zyh.wisdombj.pager.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hong.zyh.wisdombj.R;
import com.hong.zyh.wisdombj.global.GlobalConstants;
import com.hong.zyh.wisdombj.pager.BaseMenuDetailPager;
import com.hong.zyh.wisdombj.utils.CacheUtils;
import com.hong.zyh.wisdombj.view.TopNewsViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

import com.hong.zyh.wisdombj.domain.NewsTabBean;
import com.hong.zyh.wisdombj.domain.NewsMenu;
import com.viewpagerindicator.CirclePageIndicator;


/**
 * Created by shuaihong on 2019/3/7.
 */

public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData;// 单个页签的网络数据
//    private TextView view;

    //头条新闻的viewpager id
    private TopNewsViewPager mViewPager;
    private final String mUrl;
    private ArrayList<NewsTabBean.TopNews> mTopnews;

    private TextView tv_topnew_title;
    //指示器填充右下角的小圆点
    private CirclePageIndicator mIndicator;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalConstants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail,null);
        mViewPager=view.findViewById(R.id.vp_top_news);
        //头条新闻标题
        tv_topnew_title=view.findViewById(R.id.tv_topnew_title);
        //指示器填充右下角的小圆点
        mIndicator = view.findViewById(R.id.indicator);

        return view;
    }

    @Override
    public void initData() {
        //给首次加载页面的时候设置缓存数据
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET,mUrl,new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);

                //加载完了数据，就要进行缓存
                CacheUtils.setCache(mUrl, result, mActivity);
            }

            @Override
            public void onFailure(HttpException e, String msg) {
                // 请求失败
                e.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void processData(String result) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        // 头条新闻填充数据
        mTopnews = newsTabBean.data.topnews;
        if (mTopnews != null) {
            mViewPager.setAdapter(new TopNewsAdapter());

            //默认显示第一条新闻标题
            tv_topnew_title.setText(mTopnews.get(0).title);
            //给指示器设置viewpager
            mIndicator.setViewPager(mViewPager);
            //设置快照方式展示
            mIndicator.setSnap(true);

            //默认让第一个选中，解决页面销毁后重新初始化，indicator依然保留上次原点的位置bug
            mIndicator.onPageSelected(0);

            //给新闻头条图片设置滑动事件，
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //更新头条新闻标题
                    NewsTabBean.TopNews topNews = mTopnews.get(position);
                    tv_topnew_title.setText(topNews.title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }

    // 头条新闻数据适配器
    class TopNewsAdapter extends PagerAdapter {

        private final BitmapUtils mBitmaoUtils;

        public TopNewsAdapter(){
            //使用BitmapUtils加载图片，有缓冲功能，加载更快
            mBitmaoUtils = new BitmapUtils(mActivity);
            //设置加载中的图片，防止还没有加载出来的时候显示空白
            mBitmaoUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopnews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            //设置默认头条新闻图片
            //view.setImageResource(R.drawable.topnews_item_default);

            //BitmaoUtils默认设置图片方式是setImageResource，所以不会填充满控件
            //因此这里需要设置一个填充比例
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            //获取图片链接
            String imgUrl=mTopnews.get(position).topimage;

            //使用BitmaoUtils加载图片是为了防止内存溢出，因此需要利用BitmaoUtils设置缓存
            // 参数：view ,图片下载链接
            mBitmaoUtils.display(view,imgUrl);
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
