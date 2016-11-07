package com.wangpeng.myplayer.view;


import java.util.List;




import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnTouchListener;

import com.wangpeng.myplayer.R;
import com.wangpeng.myplayer.beans.LrcContent;
import com.wangpeng.myplayer.utils.DisplayUtil;
import com.wangpeng.myplayer.utils.TimeUtil;

public class LrcView extends ScrollView implements OnScrollChangedListener, OnTouchListener {
    private float width;    //歌词视图宽度
    private float height;    //歌词视图高度
    private Paint currentPaint;        //当前画笔对象
    private Paint notCurrentPaint;        //非当前画笔对象
    private Paint tipsPaint;  //提示信息画笔

    private float lightTextSize;        //高亮文本大小
    private float norTextSize;        //非高亮文本大小
    private float tipsTextSize;    //提示文本大小
    private float textHeight;    //文本高度
    private int index;    //歌词list集合下标

    private LrcTextView lrcTextView;
    private List<LrcContent> lrcLists;

    private int scrollY;
    private boolean canDrawLine = false;
    private int pos = -1; //手指按下后歌词要到的位置
    private Paint linePaint;

    private boolean canTouchLrc = true;        //是否可以触摸并调整歌词
    private Context mContext;

    private OnLrcClick click;

    public interface OnLrcClick{
        public void onLrcClick(LrcView view);
    }

    public void setClick(OnLrcClick click1){
        this.click = click1;
    }

    public LrcView(Context context) {
        this(context, null);
    }

    public LrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        this.setOnTouchListener(this);
        init();
    }

    public void setLrcLists(List<LrcContent> lrcLists) {
        this.lrcLists = lrcLists;

        this.index = -1;

        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lrcTextView = new LrcTextView(this.getContext());
        lrcTextView.setLayoutParams(params1);


        this.removeAllViews();
        this.addView(lrcTextView);
    }

    public void setIndex(int index) {
        //歌曲位置发生变化,而且手指不是调整歌词位置的状态
        if (this.index != index && pos == -1) {
            this.smoothScrollTo(0, (int) (index * textHeight));
        }
        this.index = index;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    public int getIndexByLrcTime(int currentTime) {
        if (lrcLists == null) {
            return 0;
        }

        for (int i = 0; i < lrcLists.size(); i++) {
            if (currentTime < lrcLists.get(i).getLrcTime()) {
                return i - 1;
            }
        }
        return lrcLists.size() - 1;
    }


    class LrcTextView extends TextView {
        public LrcTextView(Context context) {
            this(context, null);
        }

        public LrcTextView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LrcTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setWillNotDraw(false);
        }

        //绘制歌词
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (canvas == null) return;
            int tempY = (int) height / 2;
            for (int i = 0; i < lrcLists.size(); i++, tempY += textHeight) {
                if (i == index) {
                    canvas.drawText(lrcLists.get(i).getLrcStr(), width / 2, tempY, currentPaint);
                } else if (i == pos) {
                    canvas.drawText(lrcLists.get(i).getLrcStr(), width / 2, tempY, linePaint);
                } else {
                    canvas.drawText(lrcLists.get(i).getLrcStr(), width / 2, tempY, notCurrentPaint);
                }
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            heightMeasureSpec = (int) (height + textHeight * (lrcLists.size() - 1));
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canDrawLine) {
            canvas.drawLine(0, scrollY + height / 2, width, scrollY + height / 2, linePaint);
            canvas.drawText(TimeUtil.mill2mmss(lrcLists.get(pos).getLrcTime()), 42, scrollY + height / 2 - 2, linePaint);
        }
    }

    private void init() {
        setFocusable(true);    //设置该控件可以有焦点
        this.setWillNotDraw(false);

        norTextSize = DisplayUtil.sp2px(mContext, 16);
        lightTextSize = DisplayUtil.sp2px(mContext, 18);
        tipsTextSize = DisplayUtil.sp2px(mContext, 20);
        textHeight = norTextSize+DisplayUtil.dip2px(mContext,10);

        //高亮歌词部分
        currentPaint = new Paint();
        currentPaint.setAntiAlias(true);    //设置抗锯齿
        currentPaint.setTextAlign(Paint.Align.CENTER);    //设置文本居中

        //非高亮歌词部分
        notCurrentPaint = new Paint();
        notCurrentPaint.setAntiAlias(true);
        notCurrentPaint.setTextAlign(Paint.Align.CENTER);

        //提示信息画笔
        tipsPaint = new Paint();
        tipsPaint.setAntiAlias(true);
        tipsPaint.setTextAlign(Paint.Align.CENTER);

        //
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setTextAlign(Paint.Align.CENTER);

        //设置画笔颜色
        currentPaint.setColor(getResources().getColor(R.color.white));
        notCurrentPaint.setColor(Color.argb(140, 255, 255, 255));
        tipsPaint.setColor(Color.WHITE);
        linePaint.setColor(Color.RED);
        linePaint.setTextSize(lightTextSize);
        //设置字体
        currentPaint.setTextSize(lightTextSize);
        currentPaint.setTypeface(Typeface.SERIF);

        notCurrentPaint.setTextSize(norTextSize);
        notCurrentPaint.setTypeface(Typeface.DEFAULT);

        tipsPaint.setTextSize(tipsTextSize);
        tipsPaint.setTypeface(Typeface.DEFAULT);

        linePaint.setTextSize(lightTextSize);
        linePaint.setTypeface(Typeface.SANS_SERIF);

    }

    @Override
    public void invalidate() {
        super.invalidate();
        lrcTextView.invalidate();
    }

    @Override
    public void onScrollChanged() {

    }

    int mDownX = 0;
    int mDownY = 0;
    int mTempX = 0;
    int mTempY = 0;
    private static final int MAX_DISTANCE_FOR_CLICK = 100;
    private static final int MAX_INTERVAL_FOR_CLICK = 250;
    boolean mIsWaitUpEvent = false;
    Runnable mTimerForUpEvent = new Runnable() {
        public void run() {
            if (mIsWaitUpEvent) {
                mIsWaitUpEvent = false;
            }
        }
    };
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mIsWaitUpEvent && ev.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mTempX = (int) ev.getX();
                mTempY = (int) ev.getY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                }
                break;
            case MotionEvent.ACTION_UP:
                mTempX = (int) ev.getX();
                mTempY = (int) ev.getY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                    break;
                } else {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                    click.onLrcClick(this);
                    return super.onTouchEvent(ev);
                }
            case MotionEvent.ACTION_CANCEL:
                mIsWaitUpEvent = false;
                removeCallbacks(mTimerForUpEvent);
                break;

        }return super.onTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (!canTouchLrc) return true;
        if (!mIsWaitUpEvent && ev.getAction() != MotionEvent.ACTION_DOWN) {
            return handleTouchLrcOK(ev.getAction());
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mIsWaitUpEvent = true;
                postDelayed(mTimerForUpEvent, MAX_INTERVAL_FOR_CLICK);
                break;
            case MotionEvent.ACTION_MOVE:
                mTempX = (int) ev.getX();
                mTempY = (int) ev.getY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                }
                break;
            case MotionEvent.ACTION_UP:
                mTempX = (int) ev.getX();
                mTempY = (int) ev.getY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                    break;
                } else {
                    System.out.println("符合");
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                    click.onLrcClick(this);
                }


        }
        return handleTouchLrcOK(ev.getAction());
    }

    boolean handleTouchLrcOK(int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                scrollY = this.getScrollY();
                pos = (int) (this.getScrollY() / textHeight);
                canDrawLine = true;
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                canDrawLine = false;
                pos =-1;
                this.invalidate();
                break;

        }
        return false;
    }

}