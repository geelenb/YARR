package be.geelen.yarr.postPages.Imgur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.Fab;

import org.json.JSONException;

import be.geelen.yarr.R;
import be.geelen.yarr.postPages.PostPage;
import be.geelen.yarr.tools.Anim;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class ImgurAlbumPostPage extends PostPage {

    public static ImgurAlbumPostPage newInstance (String json) {
        ImgurAlbumPostPage defaultPostPage = new ImgurAlbumPostPage();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        defaultPostPage.setArguments(args);
        return defaultPostPage;
    }

    public ImgurAlbumPostPage() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                setJSON(getArguments().getString(JSON_VAL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_page_imgur_album, container, false);
        // against id collisions
        view.setId(hashCode());

        TextView postTitle = (TextView) view.findViewById(R.id.post_title);
        VerticalViewPager verticalViewPager = (VerticalViewPager) view.findViewById(R.id.vertical_view_pager);
        Fab fab = (Fab) view.findViewById(R.id.reddit_comment_fab);
//        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.image_circle_page_indicator);

        try {
            postTitle.setText(postObject.getJSONObject("data").getString("title"));
            String url = postObject.getJSONObject("data").getString("url");
//            ImgurAlbumAdapter imgurAlbumAdapter = new ImgurAlbumAdapter(((FragmentActivity) container.getContext()).getSupportFragmentManager(), url);
            ImgurAlbumAdapter imgurAlbumAdapter = new ImgurAlbumAdapter(getChildFragmentManager(), url);
            verticalViewPager.setAdapter(imgurAlbumAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Anim.collapse(view);
                return true;
            }
        });

//        circlePageIndicator.setVoteViewPager(verticalViewPager);

        return view;
    }
}
