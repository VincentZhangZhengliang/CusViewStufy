package com.python.cusviewstufy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.python.cusviewstufy.R;

/**
 * @author Python
 * @date 2018/6/13
 * @desc
 */
public class NumberProgressBar extends View {

    private static final String TAG = "NumberProgressBar";

    private int mMaxProgress = 100;

    /**
     * Current progress, can not exceed the max progress.
     */
    private int mCurrentProgress = 0;

    /**
     * The progress area bar color.
     */
    private int mReachedBarColor;

    /**
     * The bar unreached area color.
     */
    private int mUnreachedBarColor;

    /**
     * The progress text color.
     */
    private int mTextColor;

    /**
     * The progress text size.
     */
    private float mTextSize;

    /**
     * The height of the reached area.
     */
    private float mReachedBarHeight;

    /**
     * The height of the unreached area.
     */
    private float mUnreachedBarHeight;

    /**
     * The suffix of the number.
     */
    private String mSuffix = "%";

    /**
     * The prefix.
     */
    private String mPrefix = "";

    private final int default_progress_max = 100;
    private final int default_progress_cur = 0;
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);
    private final float default_progress_text_offset;
    private final float default_text_size;
    private final float default_reached_bar_height;
    private final float default_unreached_bar_height;

    /**
     * The progress text offset.
     */
    private float mOffset;


    /**
     * should show text flag
     */
    private static final int PROGRESS_TEXT_VISIBLE = 0;

    private boolean mIfTextviewVisible;
    private String mCurrentDrawText;
    private Paint mTextPaint;
    private Paint mReachBarPaint;
    private Paint mUnreachBarPaint;
    private float mDrawTextWidth;

    private boolean mDrawReachBar;
    private boolean mDrawUnreachBar;
    private float mDrawTextStart;
    private int mReachBarRectf;


    /**
     * Unreached bar area to draw rect.
     */
    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    /**
     * Reached bar area rect.
     */
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);
    private int mDrawTextEnd;


    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_reached_bar_height = dp2px(1.5f);
        default_unreached_bar_height = dp2px(1.0f);
        default_text_size = sp2px(10);
        default_progress_text_offset = dp2px(3.0f);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar, defStyleAttr, 0);

        mTextColor = typedArray.getColor(R.styleable.NumberProgressBar_progress_text_color, default_text_color);
        mTextSize = typedArray.getDimension(R.styleable.NumberProgressBar_progress_text_size, default_text_size);

        mReachedBarColor = typedArray.getColor(R.styleable.NumberProgressBar_progress_reached_color, default_reached_color);
        mReachedBarHeight = typedArray.getDimension(R.styleable.NumberProgressBar_progress_reached_bar_height, default_reached_bar_height);

        mUnreachedBarColor = typedArray.getColor(R.styleable.NumberProgressBar_progress_unreached_color, default_unreached_color);
        mUnreachedBarHeight = typedArray.getDimension(R.styleable.NumberProgressBar_progress_unreached_bar_height, default_unreached_bar_height);

        mMaxProgress = typedArray.getInt(R.styleable.NumberProgressBar_progress_max, default_progress_max);
        mCurrentProgress = typedArray.getInt(R.styleable.NumberProgressBar_progress_current, default_progress_cur);
        setMaxProgress(mMaxProgress);
        setProgress(mCurrentProgress);

        mOffset = typedArray.getDimension(R.styleable.NumberProgressBar_progress_text_offset, 0);

        //show or hide text
        int textVisible = typedArray.getInt(R.styleable.NumberProgressBar_progress_text_visibility, PROGRESS_TEXT_VISIBLE);
        if (textVisible == PROGRESS_TEXT_VISIBLE) {
            mIfTextviewVisible = true;
        }

        //recycle typedArray
        typedArray.recycle();

        // init paint
        initPaint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        int padding = isWidth ? getPaddingRight() + getPaddingLeft() : getPaddingBottom() + getPaddingTop();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(size, result);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //是否文字
        if (mIfTextviewVisible) {
            calculateDrawRectF();
        } else {
            calculateDrawRectFWithoutProgressText();
        }

        //画reachbar
        if (mDrawReachBar) {
            canvas.drawRect(mReachedRectF, mReachBarPaint);
        }

        //画unreachbar
        if (mDrawUnreachBar) {
            canvas.drawRect(mUnreachedRectF, mUnreachBarPaint);
        }

        if (mIfTextviewVisible) {
            canvas.drawText(mCurrentDrawText, mDrawTextStart, mDrawTextEnd, mTextPaint);
        }


    }


    private void calculateDrawRectFWithoutProgressText() {

        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
        mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft();
        mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;

        //处理unreachbar的绘制区域
        mUnreachedRectF.left = mReachedRectF.right;
        mUnreachedRectF.top = getHeight() / 2.0f - mUnreachedBarHeight / 2.0f;
        mUnreachedRectF.right = getWidth() - getPaddingRight();
        mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;

    }

    @SuppressLint("DefaultLocale")
    private void calculateDrawRectF() {

        //处理最终显示的文字
        mCurrentDrawText = String.format("%d", getProgress() * 100 / getMax());
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix;
        //Return the width of the text.
        mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText);

        if (getProgress() == 0) {    //进度是0
            mDrawReachBar = false;
            mDrawTextStart = getPaddingLeft();
        } else {
            mDrawReachBar = true;
            //处理reachBar的绘制区域
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft() - mOffset;
            mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;
            mDrawTextStart = mReachedRectF.right + mOffset;
        }

        mDrawTextEnd = (int) ((getHeight() / 2.0f) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f));
        if (mDrawTextStart + mDrawTextWidth >= getWidth() - getPaddingRight()) {
            mDrawTextStart = getWidth() - getPaddingRight() - mDrawTextWidth;
            mReachedRectF.right = mDrawTextStart - mOffset;
        }

        float unreachedBarStart = mDrawTextStart + mDrawTextWidth + mOffset;
        if (unreachedBarStart >= getWidth() - getPaddingRight()) {
            mDrawUnreachBar = false;
        } else {
            mDrawUnreachBar = true;
            //处理unreachbar的绘制区域
            mUnreachedRectF.left = unreachedBarStart;
            mUnreachedRectF.top = getHeight() / 2.0f - mUnreachedBarHeight / 2.0f;
            mUnreachedRectF.right = getWidth() - getPaddingRight();
            mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
        }

    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) mTextSize, Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight));
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mReachBarPaint = new Paint();
        mReachBarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mReachBarPaint.setColor(mReachedBarColor);

        mUnreachBarPaint = new Paint();
        mUnreachBarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mUnreachBarPaint.setColor(mUnreachedBarColor);
    }

    public void setProgress(int currentProgress) {
        if (currentProgress <= getMax() && currentProgress >= 0) {
            mCurrentProgress = currentProgress;
//            invalidate();
            postInvalidate();
        }
    }


    public int getProgress() {
        return mCurrentProgress;
    }

    public int getMax() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress > 0) {
            mMaxProgress = maxProgress;
            invalidate();
        }
    }


    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
        }

        if (onProgressBarListener != null) {
            onProgressBarListener.onProgressChange(getProgress(), getMax());
        }
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    OnProgressBarListener onProgressBarListener;

    public interface OnProgressBarListener {
        void onProgressChange(int progress, int max);
    }

    public void setOnProgressBarListener(OnProgressBarListener onProgressBarListener) {
        this.onProgressBarListener = onProgressBarListener;
    }


}
