package com.example.pn748_000.micinput;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Locale;

/**
 * Created by pn748_000 on 4/13/2016.
 */
public class GraphView extends View {
    private int width,height;
    private double maxValue;
    private double[] data;
    private Paint mPaint,textPaint;
    private float barHeight;
    private int sampleFrequency;
    private float x,textBoxSize=10f;
    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        TypedArray array=context.getTheme().obtainStyledAttributes(attrs,R.styleable.GraphView,0,0);
        try {

            mPaint.setColor(array.getColor(R.styleable.GraphView_barColor,Color.BLUE));
        }
        finally {
            array.recycle();
        }
    }

    public void setData(double[] data,double max,int sampleFrequency){
        this.data=data;
        maxValue=max;
        this.sampleFrequency=sampleFrequency;
        invalidate();
        requestLayout();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w-getPaddingLeft()-getPaddingRight();
        height=h-getPaddingBottom()-getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (data!=null){
            for(int i=0;i<data.length/2;i++){
                x=2*i*width/data.length;
                barHeight=(float)height-(float)(height/maxValue*data[i]);
                canvas.drawRect(x, barHeight ,2*(i+1)*width/data.length,
                        height-textBoxSize  ,mPaint);
                if(i%10==0)
                    canvas.drawText(String.format(Locale.UK,"%.0f Hz",(double)sampleFrequency/data.length*i),x,height,textPaint);
            }
        }
    }
}
