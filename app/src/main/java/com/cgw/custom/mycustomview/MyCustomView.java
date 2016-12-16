package com.cgw.custom.mycustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author Administrator
 * Time 2016/11/29.
 */

public class MyCustomView extends View {

    private Paint mPaint ,speedAreaPaint;
    private int circleCenterPointX ,circleCenterPointY;

    public MyCustomView(Context context) {
        super(context);
        init();
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //获取屏幕宽高 和 屏幕密度dpi
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;

        this.setClickable(true);
        //初始化  圆心左边 和 半径
        mDensityDpi = displayMetrics.densityDpi / 320;
        Log.e("mDensityDpi","------"+mDensityDpi);

        this.animate().rotationX(3f) .setDuration(500)
                .x(20f)
                .y(30f);
        speedAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        speedAreaPaint.setAntiAlias(true);
        //设置画笔样式
        speedAreaPaint.setStyle(Paint.Style.FILL);
        // 设置速度范围扇形的渐变颜色


    }


    private void parseAttributes(TypedArray a) {
//        mTextSize = a.getDimension(R.styleable.RangeBarChart_textsize, 25);
//
//        mTextColor = mBarColor = a.getColor(R.styleable.RangeBarChart_barcolor, Color.WHITE);
        a.recycle();
    }

    float mDensityDpi ;
    //仪表盘圆刻度值的半径
    private float textRaduis;

    //文字的偏移量
    private float textScale;
    private int width,height ;
    //速度范围的2个扇形外切矩形
    private RectF  speedRectFInner,speedRectF;

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        circleCenterPointX = circleCenterPointY = getWidth()/2;
        textRaduis = circleCenterPointX - 50*mDensityDpi;

        speedRectFInner = new RectF(circleCenterPointX/2 , circleCenterPointY /2,
                circleCenterPointY + circleCenterPointY/2 + 10*mDensityDpi , circleCenterPointY + textRaduis / 2);

        speedRectF = new RectF( circleCenterPointX- circleCenterPointX + 10 * mDensityDpi, circleCenterPointY - circleCenterPointX + 10 * mDensityDpi,
                circleCenterPointX + circleCenterPointX - 10 * mDensityDpi, circleCenterPointY + circleCenterPointX - 10 * mDensityDpi);

        Shader mShader = new LinearGradient(circleCenterPointX - textRaduis, circleCenterPointY, circleCenterPointX + textRaduis, circleCenterPointY,
                new int[]{0xFF445EED, 0xFF072AE9, 0xFF0625CE}, null, Shader.TileMode.CLAMP);
        speedAreaPaint.setShader(mShader);

        speed = 30 ;

        //抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF343434);
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, circleCenterPointX, mPaint);
//
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x7E3F51B5);
        mPaint.setStrokeWidth(3 );
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, circleCenterPointX, mPaint);
        mPaint.setStrokeWidth(3);
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, circleCenterPointX - 10 * mDensityDpi, mPaint);
//
        //内圈2个圆
        mPaint.setStrokeWidth(3 );
        mPaint.setColor(0xE73F51B5);
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, circleCenterPointX / 2, mPaint);
        mPaint.setColor(0xE73F51B5);
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, circleCenterPointX / 2 + 10* mDensityDpi , mPaint);
        mPaint.setStrokeWidth(3 );
//---------------------------------画刻度线---------------------------------------------

        for (int i = 0; i < 60; i++) {
            if (i % 6 == 0) {
                canvas.drawLine(circleCenterPointX - circleCenterPointX  + 10* mDensityDpi , circleCenterPointY, circleCenterPointX - circleCenterPointX  + 50* mDensityDpi , circleCenterPointY, mPaint);
            } else {
                canvas.drawLine(circleCenterPointX - circleCenterPointX  + 10 * mDensityDpi, circleCenterPointY, circleCenterPointX - circleCenterPointX  + 30* mDensityDpi , circleCenterPointY, mPaint);
            }
            //画布以circleCenterPointX，circleCenterPointY为圆心旋转6度 x轴y轴也跟着旋转6度 画的线起始点跟没旋转的时候是一样的 但是因位画布旋转了 所以在view上显示的是斜线 画布旋转不会音响旋转之前的
            //画的内容 但是之后画的内容会被旋转 因为画布旋转了
            canvas.rotate(6, circleCenterPointX, circleCenterPointY);
        }
//-------------------------------刻度度数-----------------------------
        for (int i = 0; i < 8; i++) {
            drawText(canvas, 30 * i);
        }

        drawSpeedArea(canvas);


    }

    private float xX=circleCenterPointX  ,yY=circleCenterPointX ;
    private float x = 100,y= 200 ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("x","-----onTouchEvent---");
        x = event.getX();
        y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                x = xX;
                y = yY;
                postInvalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e("x","----"+"ACTION_DOWN");
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                postInvalidate();
                break;
        }

        return super.onTouchEvent(event);
    }

    private int baseX ,baseY;
    private Paint textPaint = new Paint();

    /**
     * 绘制速度标识文字
     */
    private void drawText(Canvas canvas, int value) {
        String TEXT = String.valueOf(value);
        switch (value) {
            case 0:
                // 计算Baseline绘制的起点X轴坐标
                baseX = (int) (circleCenterPointX - textRaduis * Math.cos(Math.PI / 5) + textPaint.measureText(TEXT) / 2 + textScale / 2);
                // 计算Baseline绘制的Y坐标
                baseY = (int) (circleCenterPointY + textRaduis * Math.sin(Math.PI / 5) + textScale / 2);
                break;
            case 30:
                baseX = (int) (circleCenterPointX - textRaduis + textPaint.measureText(TEXT) / 2);
                baseY = (int) (circleCenterPointY + textScale);
                break;
            case 60:
                baseX = (int) (circleCenterPointX - textRaduis * Math.cos(Math.PI / 5) + textScale);
                baseY = (int) (circleCenterPointY - textRaduis * Math.sin(Math.PI / 5) + textScale * 2);
                break;
            case 90:
                baseX = (int) (circleCenterPointX - textRaduis * Math.cos(2 * Math.PI / 5) - textScale / 2);
                baseY = (int) (circleCenterPointY - textRaduis * Math.sin(2 * Math.PI / 5) + 2 * textScale);
                break;
            case 120:
                baseX = (int) (circleCenterPointX + textRaduis * Math.sin(Math.PI / 10) - textPaint.measureText(TEXT) / 2);
                baseY = (int) (circleCenterPointY - textRaduis * Math.cos(Math.PI / 10) + 2 * textScale);
                break;
            case 150:
                baseX = (int) (circleCenterPointX + textRaduis * Math.cos(Math.PI / 5) - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (circleCenterPointY - textRaduis * Math.sin(Math.PI / 5) + textScale * 2);
                break;
            case 180:
                baseX = (int) (circleCenterPointX + textRaduis - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (circleCenterPointY + textScale);
                break;
            case 210:
                baseX = (int) (circleCenterPointX + textRaduis * Math.cos(Math.PI / 5) - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (circleCenterPointY + textRaduis * Math.sin(Math.PI / 5) - textScale / 2);
                break;

        }
        canvas.drawText(TEXT, baseX, baseY, textPaint);




    }

    //速度
    private int speed;
    /**
     * 绘制速度区域扇形
     */
    private void drawSpeedArea(Canvas canvas) {
        int degree;
        if (speed < 210) {
            degree = speed * 36 / 30;
        } else {
            degree = 210 * 36 / 30;
        }
////
        canvas.drawArc(speedRectF, 144, degree, true, speedAreaPaint);

//        // TODO: 2016/5/12
//        //不显示中间的内圈的扇形区域
        mPaint.setColor(0xFF343434);
        mPaint.setStyle(Paint.Style.FILL);
        RectF test = new RectF(100 , 100,300 , 400);
        canvas.drawArc(test, 144, degree, true, mPaint);
        //设置控件大小
//        this.setScaleX((float) changex);
//        this.setScaleY((float) changey);
//        mPaint.setStyle(Paint.Style.STROKE);


        //测试
        Shader mShader = new LinearGradient(100, 200, 300, 400,
                new int[]{0xFF445EED, 0xFF072AE9, 0xFF0625CE}, null, Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setColor(0xFF343434);
        p.setShader(mShader);
        p.setStyle(Paint.Style.FILL);
        RectF testRectf = new RectF(x ,y ,x+100 ,y+100);
//        canvas.drawArc(testRectf, 0, degree, true, p);//画弧形
        canvas.drawRect(testRectf,p);//画矩形
//        canvas.drawOval(testRectf,p);//画圆
//        canvas.drawRoundRect(testRectf,10,100,p);//圆角矩形


    }

}
