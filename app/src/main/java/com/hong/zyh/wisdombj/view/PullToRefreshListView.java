package com.hong.zyh.wisdombj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hong.zyh.wisdombj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shuaihong on 2019/3/22.
 * 下拉刷新的listview
 */

public class PullToRefreshListView extends ListView {

    //头布局
    private View mHeaderView;
    //头布局高度
    private int mHeaderViewHeight;
    //按下的时候的y坐标
    private int startY;
    //松开的时候的y坐标
    private int endY;
    //开始刷新的状态
    private static final int STATE_PULL_TO_REFRESH=1;
    //松手释放刷新的状态
    private static final int START_RELEASE_REFRESH=2;
    //正在刷新的状态
    private static final int STATE_REFRESHING=3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前刷新状态
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private TextView tvTitle;
    private TextView tvTime;

    private RotateAnimation animUp;
    private RotateAnimation animDown;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        //在这个ListView中增加头布局
        this.addHeaderView(mHeaderView);

        //找到加载中，时间等id
        ivArrow = mHeaderView.findViewById(R.id.iv_arrow);
        pbProgress = mHeaderView.findViewById(R.id.pb_loading);
        tvTitle = mHeaderView.findViewById(R.id.tv_title);
        tvTime = mHeaderView.findViewById(R.id.tv_time);

        mHeaderView.measure(0,0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);

        initAnim();
        setCurrentTime();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //bug解决， 当用户按住头条新闻的viewpager进行下拉时,ACTION_DOWN会被viewpager消费掉,导致startY没有赋值,此处需要重新获取一下
                if (startY==-1){
                    startY = (int) ev.getY();
                }
                //当前正在刷新
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }
                endY = (int) ev.getY();
                //滑动的距离
                int dy = endY - startY;
                // 当前显示的第一个item的位置
                int firstVisiblePosition = getFirstVisiblePosition();
                if (dy > 0 && firstVisiblePosition == 0) {
                    // 计算当前下拉控件的padding值
                    int padding = dy - mHeaderViewHeight;
                    mHeaderView.setPadding(0,padding,0,0);
//
                    //也就是超出全部拉出来的范围了
                if (padding>0 && mCurrentState!=START_RELEASE_REFRESH){
                    // 改为松开刷新
                    mCurrentState=START_RELEASE_REFRESH;
                    refreshState();
                }else if (padding < 0 && mCurrentState!= STATE_PULL_TO_REFRESH){
                    // 改为下拉刷新
                    mCurrentState = STATE_PULL_TO_REFRESH;
                    refreshState();
                }
                //表示消费了事件
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == START_RELEASE_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();

                    // 完整展示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);

                    // 4. 进行回调
//                    if (iOnRefreshListener != null) {
                        iOnRefreshListener.onRefresh();
//                    }

                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    // 隐藏头布局
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }

                break;
            default:
                break;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据当前状态刷新界面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case START_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");

                ivArrow.clearAnimation();// 清除箭头动画,否则无法隐藏

                pbProgress.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }
    /**
     * 初始化箭头动画
     */
    private void initAnim() {
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }
    /**
     * 刷新结束,收起控件
     */
    public void onRefreshComplete(boolean success) {

        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        mCurrentState = STATE_PULL_TO_REFRESH;
        tvTitle.setText("下拉刷新");
        pbProgress.setVisibility(View.INVISIBLE);
        ivArrow.setVisibility(View.VISIBLE);

        if (success) {// 只有刷新成功之后才更新时间
            setCurrentTime();
        }
    }

    // 设置刷新时间
    private void setCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());

        tvTime.setText(time);
    }

    // 3. 定义成员变量,接收监听对象
    private OnRefreshListener iOnRefreshListener;

    /**
     * 2. 暴露接口,设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        iOnRefreshListener = listener;
    }

    /**
     * 1. 下拉刷新的回调接口
     *
     * @author zhangyuhong
     * @date 2019-03-31
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }
}
