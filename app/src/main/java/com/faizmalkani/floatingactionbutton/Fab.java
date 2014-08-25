package com.faizmalkani.floatingactionbutton;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class Fab extends View
{
    private Paint mButtonPaint, mDrawablePaint;
    private Bitmap  mBitmap;

    public Fab(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    public Fab(Context context)
    {
        super(context);
    }

    public void setFabColor(int fabColor)
    {
        init(fabColor);
    }

    public void setFabDrawable(Drawable fabDrawable)
    {
        Drawable myDrawable = fabDrawable;
        mBitmap = ((BitmapDrawable) myDrawable).getBitmap();
        invalidate();
    }


    public void init(int fabColor)
    {
        setWillNotDraw(false);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setColor(fabColor);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setShadowLayer(10.0f, 0.0f, 3.5f, Color.argb(100, 0, 0, 0));
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        setClickable(true);
        if(mButtonPaint != null) {
            canvas.drawCircle(getWidth()/2, getHeight()/2,(float) (getWidth()/2.6), mButtonPaint);
            canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2, (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
        {
            setAlpha(1.0f);
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            setAlpha(0.8f);
        }
        return super.onTouchEvent(event);
    }
}
