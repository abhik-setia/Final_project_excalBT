package com.example.vinay.bluesample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Vinay on 05-10-2016.
 */
public class CanvasView extends View {
    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 650;
    int [] arr = new int[20];
    String [] arrS = null;


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        arr[0]=26;
        // we set a new Path
        mPath = new Path();
        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        mPaint.setTextSize(35);
    }

    public void setArr(int [] a, String [] as, int length){
        arr = new int[length];
        arrS = new String[length];
        for(int i = 0; i < length; i++){
            arr[i] = new Integer(a[i]);
            arrS[i] = new String(as[i]);
        }
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawColor(Color.DKGRAY);
        //canvas.drawPath(mPath, mPaint);


        int min = Math.min(canvas.getWidth()/2,canvas.getHeight()/2);
        int div = min/6;
        double ratio = (5 * div)/115.0;

        mPaint.setColor(Color.GREEN);
        for(int i = 0; i < 6; i++){
            canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, div*i, mPaint);
        }

        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        if(arr != null){
            for(int i = 0; i < arr.length; i++){
                double value = 6.28 * Math.random();
                int x = (int) (ratio * arr[i] * Math.cos(value));
                int y = (int) (ratio * arr[i] * Math.sin(value));
                canvas.drawCircle(canvas.getWidth()/2 + x, canvas.getHeight()/2 + y, 20, mPaint);
                //canvas.drawText(arrS[i], canvas.getWidth()/2 + x, canvas.getHeight()/2 + y - 20, mPaint);
            }
        }
    }
    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }


}

