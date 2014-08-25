package be.geelen.yarr.comments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.R;

public class CommentFragment extends Fragment {
    protected static final String JSON_VAL = "json_val";

    // this is already the "data" part
    protected JSONObject commentObject;
    private View view;
    private ViewPager viewPager;
    private TextView commentTextView;
    private CommentsAdapter commentsAdapter;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(String json) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        commentFragment.setArguments(args);
        return commentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        view.setId(hashCode());

        setJson(getArguments().getString(JSON_VAL));

        commentTextView = (TextView) view.findViewById(R.id.comment_textview);
        viewPager = (ViewPager) view.findViewById(R.id.comment_replies_viewpager);

        try {
            if (commentObject.has("body")) {
                commentTextView.setText(Html.fromHtml(Html.fromHtml(commentObject.getString("body_html")).toString()).toString());

                if ("".equals(commentObject.get("replies"))) {
                    ((ViewGroup) viewPager.getParent()).removeView(viewPager);
                } else {
                    JSONArray replyArray = commentObject
                            .getJSONObject("replies")
                            .getJSONObject("data")
                            .getJSONArray("children");
                    commentsAdapter = new CommentsAdapter(getChildFragmentManager(), replyArray);
                    viewPager.setAdapter(commentsAdapter);
                }
            } else {
                int amount = commentObject.getInt("count");
                final String text;
                if (amount == 1) {
                    text = getResources().getString(R.string.load_1_more_reply);
                } else {
                    text = String.format(getResources().getString(R.string.load_X_more_replies),amount);
                }

                commentTextView.setText(text);
                commentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(), "TODO: implement \"More comments...\"", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (viewPager.getAdapter() != null)
//            cpi.setVoteViewPager(viewPager);

        return view;
    }

    public void setJson(String json) {
        try {
            commentObject = new JSONObject(json).getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
