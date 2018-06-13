package com.python.cusviewstufy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.python.cusviewstufy.R;

/**
 * @author Python
 * @date 2018/6/13
 * @desc
 */
public class NumberProgressBar extends View {

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

    private final int default_progress_max    = 100;
    private final int default_progress_cur    = 0;
    private final int default_text_color      = Color.rgb(66, 145, 241);
    private final int default_reached_color   = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);
    private final float default_progress_text_offset;
    private final float default_text_size;
    private final float default_reached_bar_height;
    private final float default_unreached_bar_height;

    /**
     * The progress text offset.
     */
    private float mOffset;


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

        typedArray.recycle();

        initPaint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int size    = MeasureSpec.getSize(measureSpec);
        int mode    = MeasureSpec.getMode(measureSpec);
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

        //画reachbar

        //画unreachbar

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
        Paint textPaint = new Paint();
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setAntiAlias(true);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint reachBarPaint = new Paint();
        reachBarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        reachBarPaint.setColor(mReachedBarColor);

        Paint unreachBarPaint = new Paint();
        unreachBarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        unreachBarPaint.setColor(mUnreachedBarColor);
    }

    private void setProgress(int currentProgress) {

        if (currentProgress <= getMax() && currentProgress >= 0) {
            mCurrentProgress = currentProgress;
            invalidate();
        }

    }

    private int getMax() {
        return mMaxProgress;
    }

    private void setMaxProgress(int maxProgress) {
        if (maxProgress > 0) {
            mMaxProgress = maxProgress;
            invalidate();
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


}
