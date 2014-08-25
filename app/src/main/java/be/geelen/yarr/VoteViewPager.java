package be.geelen.yarr;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;


public class VoteViewPager extends ViewPager {
    public VoteViewPager(Context context) {
        super(context);
    }

    public VoteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float firstX = -1;
    private float firstY = -1;

    private OnVoteListener onVoteListener;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int previousItem = getCurrentItem();
        boolean result = super.onTouchEvent(ev);

        if (firstX == -1) {
            firstX = ev.getX();
            firstY = ev.getY();
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int currentItem = getCurrentItem();
            if (currentItem == previousItem + 1) {
                float x = firstX / getWidth();
                float y = firstY / getHeight();

                if (x > .65) {
                    float maxUp = (float) (getResources().getInteger(R.integer.upvote_strip_height_percent) / 100.0);
                    float minDown = (float) (1.0 - (getResources().getInteger(R.integer.downvote_strip_height_percent) / 100.0));

                    if (y < maxUp) {
                        if (onVoteListener != null)
                            onVoteListener.onVote(previousItem, true);
                        Toast.makeText(getContext(), "upvoted " + x, Toast.LENGTH_SHORT).show();
                    } else if (y > minDown) {
                        if (onVoteListener != null)
                            onVoteListener.onVote(previousItem, false);
                        Toast.makeText(getContext(), "downvoted " + x, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            firstX = -1;
            firstY = -1;
        }

        return result;
    }

    public void setOnVoteListener(OnVoteListener onVoteListener) {
        this.onVoteListener = onVoteListener;
    }
    
    public interface OnVoteListener {
        void onVote(int i, boolean up);
    }
}
