package be.geelen.yarr.postPages;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONException;

import be.geelen.yarr.R;
import be.geelen.yarr.tools.CollapseViewOnTouchListener;


public class SelfPostPage extends PostPage {

    public static SelfPostPage newInstance (String json) {
        SelfPostPage defaultPostPage = new SelfPostPage();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        defaultPostPage.setArguments(args);
        return defaultPostPage;
    }

    public SelfPostPage() {
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
        View view = inflater.inflate(R.layout.fragment_post_page_self, container, false);
        // against id collisions
        view.setId(hashCode());

        TextView postTitle = (TextView) view.findViewById(R.id.post_title);
        WebView webView = (WebView) view.findViewById(R.id.webview);

        try {
            postTitle.setText(postObject.getJSONObject("data").getString("title"));

            // todo: unescape
            String data = postObject.getJSONObject("data").getString("selftext_html");
            data = Html.fromHtml(data).toString();
            webView.loadData(data, "text/html", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postTitle.setOnTouchListener(new CollapseViewOnTouchListener());

        return view;
    }
}
