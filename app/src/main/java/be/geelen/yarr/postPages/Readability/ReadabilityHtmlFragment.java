package be.geelen.yarr.postPages.Readability;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import be.geelen.yarr.R;

/**
 * Created by bram on 16/08/2014.
 */
public class ReadabilityHtmlFragment extends Fragment {
    private static final String HTML_VAL = "HTML_VAL";
    private String html;

    public ReadabilityHtmlFragment() {
    }

    public static ReadabilityHtmlFragment newInstance (String html) {
        ReadabilityHtmlFragment readabilityHtmlFragment = new ReadabilityHtmlFragment();
        Bundle args = new Bundle();
        args.putString(HTML_VAL, html);
        readabilityHtmlFragment.setArguments(args);
        return readabilityHtmlFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.html = getArguments().getString(HTML_VAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_readability_htmlpart, container, false);

        WebView webView = (WebView) view.findViewById(R.id.htmlpart_webview);
        webView.loadData(html, "text/html", null);

        return view;
    }
}