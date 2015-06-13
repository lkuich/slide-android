package com.j03.mobileinput.Canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import Settings.AppSettings;
import Gesture.Controller;
import Gesture.TouchEvents;

public class CanvasView
    extends Controller
    implements TouchEvents
{
    public static Canvas mCanvas;
    public static CanvasView drawingContext;

    private Bitmap drawBitmap;
    private Path drawPath;
    private Paint drawPaint;

    private final int backgroundColor = Color.rgb(25, 25, 25);
    private final int drawColor = Color.rgb(255, 255, 255);
    private float mX, mY;

    @SuppressWarnings("deprecation")
    public CanvasView(final Context c)
    {
        super(c);
        drawingContext = this;
        drawPath = new Path();
        drawPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        getSource(0).setCoordinates((int) event.getX(0), (int) event.getY(0));

        if (AppSettings.getInstance().getSettingsElements().getDrawPathEnabled())
        {
            final float touchX = event.getX();
            final float touchY = event.getY();

            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    this.touchStart(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.touchMove(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    this.touchUp();
                    break;
            }

            invalidate();
        }

        return movePointer(event);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        drawBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(drawBitmap);
        mCanvas.drawColor(backgroundColor);
        AppSettings.getInstance().getCanvasSettings().getPaint().setColor(drawColor);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawBitmap(drawBitmap, 0, 0, drawPaint);
        canvas.drawPath(drawPath, AppSettings.getInstance().getCanvasSettings().getPaint());
    }

    @Override
    public void touchStart(final float x, final float y)
    {
        drawPath.reset();
        drawPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    @Override
    public void touchMove(final float x, final float y)
    {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        final float TOUCH_TOLERANCE = 4;
        if (dx >= TOUCH_TOLERANCE
            || dy >= TOUCH_TOLERANCE)
        {
            drawPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    @Override
    public void touchUp()
    {
        drawPath.lineTo(mX, mY);
        mCanvas.drawPath(drawPath, AppSettings.getInstance().getCanvasSettings().getPaint());
        drawPath.reset();
        //clear drawing
        if (AppSettings.getInstance().getSettingsElements().getAutoClearEnabled())
        {
            mCanvas.drawColor(backgroundColor);
        }
        AppSettings.getInstance().getCanvasSettings().getPaint().setColor(drawColor);
    }
}