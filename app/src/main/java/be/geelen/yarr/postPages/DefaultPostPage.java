package be.geelen.yarr.postPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.Fab;

import org.json.JSONException;

import be.geelen.yarr.R;
import be.geelen.yarr.tools.CollapseViewOnTouchListener;


public class DefaultPostPage extends PostPage {

    private View view;

    public static DefaultPostPage newInstance (String json) {
        DefaultPostPage defaultPostPage = new DefaultPostPage();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        defaultPostPage.setArguments(args);
        return defaultPostPage;
    }

    public DefaultPostPage() {
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
        view = inflater.inflate(R.layout.fragment_post_page_default, container, false);
        // against id collisions
        view.setId(hashCode());

        TextView postTitle = (TextView) view.findViewById(R.id.post_title);
        WebView webView = (WebView) view.findViewById(R.id.webview);
        Fab fab = (Fab) view.findViewById(R.id.reddit_comment_fab);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        try {
            postTitle.setText(postObject.getJSONObject("data").getString("title"));
            webView.loadUrl(postObject.getJSONObject("data").getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postTitle.setOnTouchListener(new CollapseViewOnTouchListener());

        return view;
    }
}
