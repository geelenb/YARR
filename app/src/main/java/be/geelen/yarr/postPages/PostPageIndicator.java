package be.geelen.yarr.postPages;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import be.geelen.yarr.PostAdapter;
import be.geelen.yarr.R;
import be.geelen.yarr.VoteViewPager;

public class PostPageIndicator extends View implements
        ViewPager.OnPageChangeListener,
        VoteViewPager.OnVoteListener {
    private VoteViewPager voteViewPager;
    private int currentItem;

    private int position;
    private float positionOffset;

    private HashMap<Integer, Integer> colors = new HashMap<Integer, Integer>();

    public PostPageIndicator(Context context) {
        super(context);
    }

    public PostPageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setVoteViewPager(VoteViewPager voteViewPager) {
        if (voteViewPager.getAdapter() == null)
            return;

        this.voteViewPager = voteViewPager;

        voteViewPager.setOnVoteListener(this);

        final PostAdapter postAdapter = (PostAdapter) this.voteViewPager.getAdapter();
        postAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                for (int i = 0; i < postAdapter.getCount(); i++) {
                    JSONObject child = postAdapter.getChild(i);
                    checkColorFor(child, i);
                }
                invalidate();
            }
        });

        invalidate();
    }

    private void checkColorFor(JSONObject child, int i) {
        // orange > blue > red > green > yellow
        try {
            child = child.getJSONObject("data");
            if (!child.isNull("likes")) {
                if (child.getBoolean("likes")) {
                    colors.put(i, getResources().getColor(R.color.reddit_upvote));
                } else {
                    colors.put(i, getResources().getColor(R.color.reddit_downvote));
                }
            } else if ("admin".equals(child.get("distinguished"))) {
                colors.put(i, Color.RED);
            } else if ("moderator".equals(child.get("distinguished"))) {
                colors.put(i, getResources().getColor(R.color.reddit_moderator));
            } else if (child.getBoolean("stickied")) {
                colors.put(i, getResources().getColor(R.color.grey_600));
            } else if (child.getInt("gilded") > 0) {
                colors.put(i, Color.YELLOW);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.position = position;
        this.positionOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    protected void onDraw(Canvas canvas) {
        PostAdapter adapter = (PostAdapter) voteViewPager.getAdapter();
        int count = voteViewPager.getAdapter().getCount();

        if (count == 0)
            return;

        for (int i = 0; i < count; i++) {
            float top = 0;
            float left = i * getWidth() / count;
            float right = (i + 1) * getWidth() / count;
            float bottom = getHeight();

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(getColorForPost(i));

            canvas.drawRect(left, top, right, bottom, paint);
        }

        Paint seperatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        seperatorPaint.setColor(getResources().getColor(R.color.grey_100));

        for (int i = 1; i < count; i++) {
            float top = (float) (getHeight() * .2);
            float x = i * getWidth() / count;
            float bottom = (float) (getHeight() * .8);

            canvas.drawLine(x, top, x, bottom, seperatorPaint);
        }

        float strokeWidth = 3;
        float top = strokeWidth / 2;
        float left = (position + positionOffset) * getWidth() / count;
        float right = left + getWidth() / count;
        float bottom = getHeight() - strokeWidth / 2;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    int getColorForPost(int position) {
        if (colors.containsKey(position)) {
            return colors.get(position);
        }

        return getResources().getColor(R.color.reddit_neutral);
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