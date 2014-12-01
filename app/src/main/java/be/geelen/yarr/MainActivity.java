package be.geelen.yarr;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import be.geelen.yarr.comments.CommentsOverlay;
import be.geelen.yarr.postPages.PostPageIndicator;
import be.geelen.yarr.tools.MultipleOnPageChangeListeners;

public class MainActivity
        extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment navigationDrawerFragment;
    private VoteViewPager voteViewPager;
    private CommentsOverlay commentsOverlay;
    private PostAdapter postAdapter;
    private JSONObject loginJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        String url = "http://www.reddit.com/";

        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            url = intent.getDataString();
        }

        if (!url.contains(".json")) {
            if (url.contains("?")) {
                String arguments = url.substring(url.indexOf('?'));
                url = url.substring(0, url.indexOf('?')) + ".json" + arguments;
            } else {
                url = url + (url.endsWith("/") ? "" : "/") + ".json";
            }
        }

        postAdapter = new PostAdapter(getSupportFragmentManager(), url);
        voteViewPager = (VoteViewPager) findViewById(R.id.view_pager);
        
        voteViewPager.setAdapter(postAdapter);
        postAdapter.setViewPager(voteViewPager);

        PostPageIndicator ppi = (PostPageIndicator) findViewById(R.id.circle_page_indicator);
        ppi.setVoteViewPager(voteViewPager);

        voteViewPager.setOnPageChangeListener(new MultipleOnPageChangeListeners(
                ppi));
//                postAdapter.getOnPageChangeListener()));

        addCommentsOverlay();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.reddit.com" + NavigationDrawerFragment.links[position]));
        browserIntent.setComponent(getComponentName());
        startActivity(browserIntent);
    }

    public void restoreActionBar() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        if (!navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        */
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void addCommentsOverlay() {
        commentsOverlay = CommentsOverlay.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.comments_overlay_container, commentsOverlay)
                .commit();
    }

    public String getCurrentPermalink() {
        return postAdapter.getPermalink(voteViewPager.getCurrentItem());
    }

    public JSONObject getJsonObject(int i) {
        return postAdapter.getChild(i);
    }

    @Override
    public void onBackPressed() {
        if (commentsOverlay.getOpened())
            commentsOverlay.setOpened(false);
        else
            super.onBackPressed();
    }


    public void setLoginJson(String loginJson) {
        if ("".equals(loginJson))
            return;

        try {
            loginJsonObject = new JSONObject(loginJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
