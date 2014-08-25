package be.geelen.yarr.postPages.Readability;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.Fab;

import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.R;
import be.geelen.yarr.postPages.PostPage;
import be.geelen.yarr.tools.CollapseViewOnTouchListener;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class ReadabilityPostPage extends PostPage {
    private View view;

    public static ReadabilityPostPage newInstance (String json) {
        ReadabilityPostPage readabilityPostPage = new ReadabilityPostPage();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        readabilityPostPage.setArguments(args);
        return readabilityPostPage;
    }

    public ReadabilityPostPage() {
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
        view = inflater.inflate(R.layout.fragment_post_page_readability, container, false);
        // against id collisions
        view.setId(hashCode());

        TextView postTitle = (TextView) view.findViewById(R.id.post_title);
        VerticalViewPager verticalViewPager = (VerticalViewPager) view.findViewById(R.id.vertical_view_pager);
        Fab fab = (Fab) view.findViewById(R.id.reddit_comment_fab);
//        VerticalViewPagerCirclePageIndicator vvpcpi = (VerticalViewPagerCirclePageIndicator) view.findViewById(R.id.readability_post_indicator);

        try {
            postTitle.setText(postObject.getJSONObject("data").getString("title"));
            String url = "http://readability.com/api/content/v1/parser" +
                    "?token=d48451fa7e61e79f0f7e215fe2258dc41b69ca75" +
                    "&url=" + postObject.getJSONObject("data").getString("url");

            ReadabilityAdapter readabilityAdapter = new ReadabilityAdapter(getChildFragmentManager(), url);
            verticalViewPager.setAdapter(readabilityAdapter);

//            vvpcpi.setVoteViewPager(verticalViewPager);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postTitle.setOnTouchListener(new CollapseViewOnTouchListener());

        return view;
    }

    private void setReadabilityJSON(String json) {
        try {
            String content = new JSONObject(json).getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void setReadabilityJSONObject(JSONObject jsonObject) throws JSONException {
//    }
}
