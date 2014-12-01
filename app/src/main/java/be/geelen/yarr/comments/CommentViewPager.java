package be.geelen.yarr.comments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import be.geelen.yarr.R;

public class CommentViewPager extends ViewPager implements CommentFragment.OnVoteListener {

    private int position;
    private float positionOffset;
    private CommentsAdapter commentsAdapter;
    private int positionOffsetPixels;

    private HashMap<Integer, Integer> colors = new HashMap<Integer, Integer>();

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
        for (int i = 0; i < this.commentsAdapter.getCount(); i++) {
           checkColorForIndex(i);
        }
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (commentsAdapter == null || commentsAdapter.getCount() == 0)
            return;

        int count = commentsAdapter.getCount();
        float interpolatedHeight = getResources().getDimension(R.dimen.comment_margin)
                + getInterpolatedHeight();

        float textMargin = getResources().getDimension(R.dimen.comment_margin);
        float relativeCircleSize = .50f;

        float minX = (position + positionOffset) * getWidth();
        float maxX = minX + getWidth();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);

        float minHeight = getResources().getDimension(R.dimen.comment_margin)
                + getInterpolatedHeight();

        float leftBubbles = position;

        if (positionOffset > .5)
            leftBubbles += 1;
        else
            leftBubbles += positionOffset * 2;

        for (int i = 0; i < Math.floor(leftBubbles); i++) {
            if (colors.containsKey(i))
                paint.setColor(colors.get(i));
            else
                paint.setColor(Color.GRAY);

            float y = textMargin * (1 + relativeCircleSize) * .5f * (leftBubbles - (1 + i));
            if (y > minHeight - textMargin)
                paint.setAlpha(0);
            else
                paint.setAlpha((int) (255 - y * 256 / minHeight));

            drawCircle(canvas, minX, y, textMargin, textMargin, relativeCircleSize, paint);
        }

        {
            if (colors.containsKey((int) Math.floor(leftBubbles)))
                paint.setColor(colors.get((int) Math.floor(leftBubbles)));
            else
                paint.setColor(Color.GRAY);

            float x = (float) (minX + ((1 - ((positionOffset + .5) % 1)) * (getWidth() - textMargin)));
            float y = 0;
            float w = textMargin;
            float h = textMargin;

            drawCircle(canvas, x, y, w, h, relativeCircleSize, paint);
        }

        float rightBubbles = count - position - 1;
        if (positionOffset > .5)
            rightBubbles += 1 - positionOffset * 2;

        for (int i = 0; i < Math.floor(rightBubbles); i++) {
            if (colors.containsKey(count - 1 - i))
                paint.setColor(colors.get(count - 1 - i));
            else
                paint.setColor(Color.GRAY);

            float y = textMargin * (1 + relativeCircleSize) * .5f * (rightBubbles - (1 + i));

            if (y > minHeight - textMargin)
                paint.setAlpha(0);
            else
                paint.setAlpha((int) (255 - y * 256 / minHeight));

            drawCircle(canvas, maxX - textMargin, y, textMargin, textMargin, relativeCircleSize, paint);
        }
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
        if (commentsAdapter.getCount() == 0)
            return 0f;

        Fragment leftFragment = commentsAdapter.getItemFromHashMap(position);
        if (leftFragment == null)
            return getResources().getDimension(R.dimen.comment_minimum_height);
        View leftView = leftFragment.getView();
        View leftTextView = leftView.findViewById(R.id.comment_textview);
        int leftHeight = leftTextView.getHeight();

        Fragment rightFragment = commentsAdapter.getItemFromHashMap(position + 1);
        if (positionOffsetPixels == 0 || rightFragment == null)
            return leftHeight;

        View rightView = rightFragment.getView();
        View rightTextView = rightView.findViewById(R.id.comment_textview);
        int rightHeight = rightTextView.getHeight();

        return leftHeight * (1 - positionOffset) + rightHeight * positionOffset;
    }


    private void checkColorForIndex(int i) {
        // orange > blue > red > green > yellow
        try {
            JSONObject child = commentsAdapter.getChild(i).getJSONObject("data");

            if ("reddit".equals(child.getString("author")))
                Log.d("author", "reddit");

            if (!child.isNull("likes")) {
                if (child.getBoolean("likes")) {
                    colors.put(i, getResources().getColor(R.color.reddit_upvote));
                } else {
                    colors.put(i, getResources().getColor(R.color.reddit_downvote));
                }
            } else if (commentsAdapter.getAuthor().equals(child.get("author"))) {
                colors.put(i, getResources().getColor(R.color.reddit_OP));
            } else if ("admin".equals(child.get("distinguished"))) {
                colors.put(i, getResources().getColor(R.color.reddit_admin));
            } else if ("moderator".equals(child.get("distinguished"))) {
                colors.put(i, getResources().getColor(R.color.reddit_moderator));
            } else if (child.getInt("gilded") > 0) {
                colors.put(i, getResources().getColor(R.color.reddit_gold));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVote(int i, boolean up) {
        if (up) {
            colors.put(i, getResources().getColor(R.color.reddit_upvote));
        } else {
            colors.put(i, getResources().getColor(R.color.reddit_downvote));
        }
        invalidate();
    }
}
