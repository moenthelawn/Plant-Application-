package com.example.chris.plantapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class Needle extends View {

    private Paint linePaint;
    private Path linePath;
    private Paint needleScrewPaint;

    private Matrix matrix;
    private int framePerSeconds = 100;
    private long animationDuration = 10000;
    private long startTime;
    private float scaleFactor;
    private float angleRotation;
    private float deltaX;
    private float deltaY;

    public Needle(Context context) {
        super(context);
        matrix = new Matrix();
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();

        this.angleRotation = 180f;
        init();
    }

    public Needle(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
        init();
    }

    public Needle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        matrix = new Matrix();
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
        init();
    }

    private void init() {
        scaleFactor = 1f;
        this.deltaX = 500f;
        this.deltaY = 425f;

        linePaint = new Paint();
        linePaint.setColor(Color.RED); // Set the color
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE); // set the border and fills the inside of needle
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(5.0f); // width of the border
        linePaint.setShadowLayer(8.0f, 0.1f, 0.1f, Color.GRAY); // Shadow of the needle

        linePath = new Path();
        linePath.moveTo(50.0f + deltaX, (50.0f + deltaY));
        linePath.lineTo(130.0f + deltaX, 40.0f + deltaY);
        linePath.lineTo(600.0f + deltaX, 50.0f + deltaY);
        linePath.lineTo(130.0f + deltaX, 60.0f + deltaY);
        linePath.lineTo(50.0f + deltaX, 50.0f + deltaY);
        linePath.addCircle(130.0f + deltaX, 50.0f + deltaY, 20.0f, Path.Direction.CW);
        linePath.close();

        needleScrewPaint = new Paint();
        needleScrewPaint.setColor(Color.BLACK);
        needleScrewPaint.setAntiAlias(true);
        needleScrewPaint.setShader(new RadialGradient(130.0f + deltaX, 50.0f + deltaY, 10.0f,
                Color.DKGRAY, Color.BLACK, Shader.TileMode.CLAMP));
    }

    public void setRotation_Needle(float angle) {
        this.angleRotation = angle;
    }
    public float getRotation_Needle(){
        return this.angleRotation;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long elapsedTime = System.currentTimeMillis() - startTime;

      //  matrix.postRotate(1.0f, 130.0f + deltaX, 50.0f + deltaY); // rotate 10 degree every second
        canvas.scale(0.3f, 0.3f);
        canvas.concat(matrix);

        canvas.drawPath(linePath, linePaint);

        canvas.drawCircle(130.0f + deltaX, 50.0f + deltaY, 16.0f, needleScrewPaint);
        matrix.setRotate(this.angleRotation, 130.0f + deltaX, 50.0f + deltaY);

        if (elapsedTime < animationDuration) {
            this.postInvalidateDelayed(10000 / framePerSeconds);
        }

        //this.postInvalidateOnAnimation();
        invalidate();
    }

}