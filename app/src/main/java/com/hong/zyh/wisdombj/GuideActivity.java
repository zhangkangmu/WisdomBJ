package com.hong.zyh.wisdombj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hong.zyh.wisdombj.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 向导页
 * Created by shuaihong on 2019/3/4.
 * 通过设置一个viewPager来展示图片
 * 通过getViewTreeObserver目录树来获取红点的左边距，监听viewPager的滚动事件，时刻改变红心的位置
 * 灰色点通过在xml中设置一个空的LinearLayout，然后在加载图片的时候加载进去
 */

public class GuideActivity extends Activity {

    // 引导页图片id数组
    private int mImageIds[] = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    private ArrayList<ImageView> mImageViewList; // imageView集合
    private LinearLayout ll_container;
    private ViewPager mViewPager;
    private Button btnStart;
    private ImageView ivRedPoint;
    private int mPointDis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = findViewById(R.id.vp_guide);
        ll_container = findViewById(R.id.ll_container);
        btnStart = findViewById(R.id.btn_start);
        ivRedPoint = findViewById(R.id.iv_red_point);

        // 先初始化数据
        initData();

        mViewPager.setAdapter(new GuideAdapter());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 当页面滑动过程中的回调
                System.out.println("当前位置:" + position + ";移动偏移百分比:" + positionOffset);
                // 更新小红点距离
                // 小红点当前的左边距=（第二个控件左边距-第一个控件左边距）（这个在监听layout方法结束的事件获得）
                // *偏移百分比+当前的位置（position*（第二个控件左边距-第一个控件左边距））
                int leftMargin = (int) (mPointDis * positionOffset + mPointDis*position);
                RelativeLayout.LayoutParams pointLayoutParams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                pointLayoutParams.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(pointLayoutParams);
            }

            // 某个页面被选中的时候调用的方法
            @Override
            public void onPageSelected(int position) {
                if (position == mImageViewList.size() - 1) {
                    // 最后一个页面显示开始体验的按钮
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值
        // measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
        // mPointDis = llContainer.getChildAt(1).getLeft()
        // - llContainer.getChildAt(0).getLeft();
        // System.out.println("圆点距离:" + mPointDis);

        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // 移除监听,避免重复回调
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);  //这个是过时的方法
                mPointDis = ll_container.getChildAt(1).getLeft()
                        - ll_container.getChildAt(0).getLeft();
                System.out.println("圆点距离:" + mPointDis);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp, 已经不是第一次进入了
                PrefUtils.setBoolean(getApplicationContext(),"is_first_enter",false);
                //跳到主页面
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void initData() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);// 通过设置背景,可以让宽高填充布局
            // view.setImageResource(resId)
            mImageViewList.add(view);

            //步骤：设置好原点图片-->设置好布局--->setLayoutParams把布局设给图片--->找到对应的位置控件id,addView进去
            // 初始化小圆点
            ImageView pointImageView = new ImageView(this);
            // 设置图片(shape形状)
            pointImageView.setImageResource(R.drawable.shape_point_gray);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                layoutParams.leftMargin = 10;
            }
            //给自己创建的带点设置布局
            pointImageView.setLayoutParams(layoutParams);

            //给空的布局控件增加点
            ll_container.addView(pointImageView);// 给容器添加圆点
        }

    }

    //viewpager的适配器
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
    }
}
