package be.geelen.yarr.comments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import be.geelen.yarr.R;

public class CommentViewPager extends ViewPager { // implements CommentsAdapter.OnVoteListener

    private int position;
    private float positionOffset;
    private CommentsAdapter commentsAdapter;
    private int positionOffsetPixels;

    public CommentViewPager(Context context) {
        super(context);
    }

    public CommentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter commentsAdapter) {
        super.setAdapter(commentsAdapter);
        this.commentsAdapter = (CommentsAdapter) commentsAdapter;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (commentsAdapter == null)
            return;

        int count = commentsAdapter.getCount();

        float textMargin = getResources().getDimension(R.dimen.comment_margin);
        float relativeCircleSize = .70f;

        float minX = positionOffsetPixels + position * getWidth();
        float maxX = minX + getWidth();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);

        float interpolatedHeight = getInterpolatedHeight();


        {
            paint.setColor(Color.GRAY);
            float x = (float) (minX + (1 - (Math.abs(positionOffset + .5) % 1)) * (getWidth() - textMargin));
            float y = 0;
            float w = textMargin;
            float h = textMargin;
            drawCircle(canvas, x, y, w, h, relativeCircleSize, paint);
        }

        canvas.drawLine(minX, textMargin + interpolatedHeight, maxX, textMargin + interpolatedHeight, paint);
    }

    private void drawCircle(
            Canvas canvas,
            float x, float y,
            float w, float h,
            float relativeCircleSize,
            Paint paint) {

        x += w / 2;
        y += h / 2;

        w *= relativeCircleSize;
        h *= relativeCircleSize;

        x -= w / 2;
        y -= h / 2;

        canvas.drawOval(new RectF(x, y, x + w, y + h), paint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        this.position = position;
        this.positionOffset = positionOffset;
        this.positionOffsetPixels = positionOffsetPixels;
        invalidate();
    }

    public float getInterpolatedHeight() {
        View currentChild = getChildAt(position);
        View nextChild = getChildAt(position + 1);

        if (currentChild == null)
            return 0;

        if (nextChild == null)
            return currentChild.findViewById(R.id.comment_textview).getHeight();

        View currentView = currentChild.findViewById(R.id.comment_textview);
        View nextItem = nextChild.findViewById(R.id.comment_textview);

        if (currentView == null)
            return 0;

        if (nextItem == null)
            return currentView.getHeight();

        return (1 - positionOffset) * currentView.getHeight()
                + positionOffset * nextItem.getHeight();
    }
}
