package com.wangpeng.myplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WP on 16/11/6.
 */

public class MyCircleImage extends CircleImageView implements Runnable{
    private float currentAngle = 0f; //当前旋转角度

    private boolean ifRotate=false; //是否旋转

    private int height=0,width=0;

    public MyCircleImage(Context context) {
        super(context);
    }

    public MyCircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCircleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getHeight()/2;
        width = getWidth()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-currentAngle, width, height);
        if (currentAngle >= 360f){
            currentAngle = currentAngle - 360f;
        } else{
            currentAngle = currentAngle + 0.5f;
        }
        super.onDraw(canvas);

    }
    public void startRotate(){
        if(ifRotate)return;
        ifRotate=true;
        Thread thread=new Thread(this);
        thread.start();
    }

    public void stopRotate(){
        ifRotate=false;
    }
    public void setCurrentAngle(int angle)
    {
        this.currentAngle = angle;
    }

    public void run() {
        try {
            while(ifRotate){
                this.postInvalidate();
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
