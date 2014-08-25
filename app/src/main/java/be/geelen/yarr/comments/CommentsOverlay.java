package be.geelen.yarr.comments;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;

import com.faizmalkani.floatingactionbutton.Fab;

import be.geelen.yarr.MainActivity;
import be.geelen.yarr.R;
import be.geelen.yarr.postPages.PostPageIndicator;

public class CommentsOverlay extends Fragment {
    private static final String PERMALINK_VAL = "PERMALINK";

    private View view;
    private boolean opened;

    private Fab commentFab;
    private Fab refreshFab;
    private Fab voteFab;
    private ScrollView scrollView;
    private ViewPager rootCommentViewPager;
//    private PostPageIndicator ppi;

    public static CommentsOverlay newInstance() {
        return new CommentsOverlay();
    }

    // Todo: delete
    @Deprecated
    public static CommentsOverlay newInstance(String permalink) {
        CommentsOverlay commentsOverlay = new CommentsOverlay();
        Bundle args = new Bundle();
        args.putString(PERMALINK_VAL, permalink);
        commentsOverlay.setArguments(args);
        return commentsOverlay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comments_overlay, container, false);

        opened = false;

        commentFab = (Fab) view.findViewById(R.id.reddit_comment_fab);
        refreshFab = (Fab) view.findViewById(R.id.refresh_comments_fab);
        voteFab = (Fab) view.findViewById(R.id.vote_fab);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        rootCommentViewPager = (ViewPager) view.findViewById(R.id.root_comment_viewpager);
//        ppi = (PostPageIndicator) view.findViewById(R.id.circle_page_indicator);

        commentFab.setFabColor(getResources().getColor(R.color.blue_100));
        commentFab.setFabDrawable(getResources().getDrawable(R.drawable.ic_comments));
        commentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOpened(!opened);
            }
        });

        refreshFab.setFabColor(getResources().getColor(R.color.reddit_neutral));
        refreshFab.setFabDrawable(getResources().getDrawable(R.drawable.ic_refresh));

        voteFab.setFabColor(getResources().getColor(R.color.reddit_neutral));
        voteFab.setFabDrawable(getResources().getDrawable(R.drawable.ic_circle_blank));

        return view;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;

        int parentWidth = getView().getWidth();
        int parentHeight = getView().getHeight();

        float size = getResources().getDimension(R.dimen.fab_size);
        float margin = getResources().getDimension(R.dimen.fab_margin);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator commentX, commentY;
        ObjectAnimator refreshX;
        ObjectAnimator voteX;
        ObjectAnimator mainY;

        if (opened) {
            if ((((MainActivity) getActivity()).getCurrentPermalink()).equals("")) {
                this.opened = !opened;
                return;
            }

            if (rootCommentViewPager.getAdapter() == null) {
                rootCommentViewPager.setMinimumHeight((int) (view.getHeight() - getResources().getDimension(R.dimen.comment_space_height)));
                scrollView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                scrollView.set
            }

            rootCommentViewPager.setAdapter(new CommentsAdapter(getChildFragmentManager(), ((MainActivity) getActivity()).getCurrentPermalink()));
//            ppi.setViewPager(rootCommentViewPager);

            commentX = ObjectAnimator.ofFloat(commentFab, "x", parentWidth / 2 - size / 2);
            commentX.setInterpolator(new DecelerateInterpolator());

            commentY = ObjectAnimator.ofFloat(commentFab, "y", margin);
            commentY.setInterpolator(new AccelerateInterpolator());

            refreshX = ObjectAnimator.ofFloat(refreshFab, "x", (int) (parentWidth * .20) - size / 2);
            refreshX.setInterpolator(new DecelerateInterpolator());

            voteX = ObjectAnimator.ofFloat(voteFab, "x", (int) (parentWidth * .80) - size / 2);
            voteX.setInterpolator(new DecelerateInterpolator());

            mainY = ObjectAnimator.ofFloat(scrollView, "y",  0);
            mainY.setInterpolator(new DecelerateInterpolator());
        } else {
//            rootCommentViewPager.setAdapter(null);
//            ppi.setViewPager(null);
            commentX = ObjectAnimator.ofFloat(commentFab, "x", parentWidth - margin - size);
            commentX.setInterpolator(new AccelerateInterpolator());

            commentY = ObjectAnimator.ofFloat(commentFab, "y", parentHeight - margin - size);
            commentY.setInterpolator(new DecelerateInterpolator());

            refreshX = ObjectAnimator.ofFloat(refreshFab, "x", -size);
            refreshX.setInterpolator(new AccelerateInterpolator());

            voteX = ObjectAnimator.ofFloat(voteFab, "x", parentWidth);
            voteX.setInterpolator(new AccelerateInterpolator());

            mainY = ObjectAnimator.ofFloat(scrollView, "y", parentHeight);
            mainY.setInterpolator(new AccelerateInterpolator());
        }

        animatorSet
                .play(commentX)
                .with(commentY)
                .with(refreshX)
                .with(voteX)
                .with(mainY);
        animatorSet.start();
    }

    public boolean getOpened() {
        return opened;
    }
}
