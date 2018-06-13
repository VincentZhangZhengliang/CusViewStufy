package com.python.cusviewstufy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.python.cusviewstufy.R;

/**
 * @author Python
 * @date 2018/6/13
 * @desc
 */
public class PagerLayout extends LinearLayout {

    private static final String TAG = "PagerLayout";

    private View     mContainer;
    private Scroller mScroller;
    private int   mViewHeight = 0;
    private float mDownY      = 0;
    private GestureDetector mGestureDetector;
    private boolean isHidden = false;

    public PagerLayout(Context context) {
        this(context, null);
    }

    public PagerLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContainer = LayoutInflater.from(context).inflate(R.layout.default_view, this, false);
        this.addView(mContainer);
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new GestureListenerIml());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isHidden) {


            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    mDownY = event.getY();
                    return mGestureDetector.onTouchEvent(event);

                case MotionEvent.ACTION_MOVE:
                    Log.e(TAG, "onTouchEvent: ++++++++++++++++++++++++");
                    //获取当前滑动的y轴坐标
                    float curY = event.getY();
                    //获取移动的y轴距离
                    float deltaY = curY - mDownY;
                    //getScrollX() 就是当前view的左上角相对于母视图的左上角的X轴偏移量。
                    //getScrollY() 就是当前view的左上角相对于母视图的左上角的Y轴偏移量。
                    if (deltaY < 0 || getScrollY() > 0) {
                        //deltaY<0
                        return mGestureDetector.onTouchEvent(event);
                    } else {
                        return true;
                    }

                case MotionEvent.ACTION_UP:

                    int scrollY = this.getScrollY();

                    if (scrollY < 300) {
                        // 未超过制定距离，则返回原来位置
                        prepareScroll(0, 0);
                    } else {
                        // 超过指定距离，则上滑隐藏
                        prepareScroll(0, mViewHeight);
                        // 隐藏
                        isHidden = true;
                    }

                    break;
                default:
                    return mGestureDetector.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    //滚动到目标位置
    protected void prepareScroll(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        beginScroll(dx, dy);
    }

    //设置滚动的相对偏移
    protected void beginScroll(int dx, int dy) {
        //第一,二个参数起始位置;第三,四个滚动的偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        //必须执行invalidate()从而调用computeScroll()
        invalidate();
    }

    public void show() {
        isHidden = false;
        prepareScroll(0, 0);
    }

    private void hide() {
        isHidden = true;
        prepareScroll(0, mViewHeight);
    }

    class GestureListenerIml implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int disY = (int) ((distanceY - 0.5) / 2);
            beginScroll(0, disY);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    /**
     * 填充试图
     *
     * @param context
     * @param layoutId
     */
    public void setLayout(Context context, int layoutId) {
        this.removeAllViews();

        mContainer = LayoutInflater.from(context).inflate(layoutId, this, false);

        this.addView(mContainer);

        if (mScroller == null) {
            mScroller = new Scroller(context);
        }

        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(context, new GestureListenerIml());
        }

        invalidate();
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {
        TextView textView = (TextView) getView(viewId);
        textView.setText(charSequence);
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param color
     */
    public void setTextColor(int viewId, int color) {
        TextView textView = (TextView) getView(viewId);
        textView.setTextColor(color);
    }

    /**
     * 设置文本字体大小
     *
     * @param viewId
     * @param textSize
     */
    public void setTextSize(int viewId, int textSize) {
        TextView textView = (TextView) getView(viewId);
        textView.setTextSize(textSize);
    }

    /**
     * 设置按钮点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setButtonClickListener(int viewId, OnClickListener listener) {
        Button button = (Button) getView(viewId);
        button.setOnClickListener(listener);
    }

    /**
     * 设置图片资源
     *
     * @param viewId
     * @param resId
     */
    public void setImageResource(int viewId, int resId) {
        if (mContainer != null) {
            ImageView imageView = (ImageView) getView(viewId);
            imageView.setImageResource(resId);
        }
    }

    /**
     * 设置图片bitmap
     *
     * @param viewId
     * @param bitmap
     */
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        if (mContainer != null) {
            ImageView imageView = (ImageView) getView(viewId);
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 设置图片drawable
     *
     * @param viewId
     * @param drawable
     */
    public void setImageDrawable(int viewId, Drawable drawable) {
        if (mContainer != null) {
            ImageView imageView = (ImageView) getView(viewId);
            imageView.setImageDrawable(drawable);
        }
    }

    /**
     * 设置图片缩放类型
     *
     * @param viewId
     * @param type
     */
    public void setImageScaleType(int viewId, ImageView.ScaleType type) {
        if (mContainer != null) {
            ImageView imageView = (ImageView) getView(viewId);
            imageView.setScaleType(type);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        mContainer.setBackgroundColor(color);
    }

    /**
     * 设置背景图片
     *
     * @param background
     */
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mContainer.setBackground(background);
        }
    }

    /**
     * 设置背景图片资源id
     *
     * @param resId
     */
    public void setBackgroundResource(int resId) {
        mContainer.setBackgroundResource(resId);
    }

    /**
     * 获取视图控件
     *
     * @param viewId
     * @return
     */
    public View getView(int viewId) {
        return mContainer.findViewById(viewId);
    }

}
