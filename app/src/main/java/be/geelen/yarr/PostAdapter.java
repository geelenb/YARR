package be.geelen.yarr;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import be.geelen.yarr.postPages.LastPage;
import be.geelen.yarr.postPages.PostPage;
import be.geelen.yarr.tools.HttpAsyncTask;

public class PostAdapter extends FragmentStatePagerAdapter {
    private String url;
    private JSONArray children;
    private String after;
    private ViewPager viewPager;

    public PostAdapter(FragmentManager fm, String url) {
        super(fm);
        this.url = url;

        refresh();
    }

    void setJson(String json) {
//        this.json = json;
        try {
            JSONObject data = new JSONObject(json).getJSONObject("data");
            children = data.getJSONArray("children");
            after = data.getString("after");

            notifyDataSetChanged();
            viewPager.setCurrentItem(0);

        } catch (JSONException e) {
            Log.e("PostAdapter::setJson", "no json to be set, check connectivity");
//            e.printStackTrace();
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (children == null)
            return null;

        if (position == children.length()) {
//        if(position == 1) {
            LastPage lastPage = LastPage.newInstance();//url);
            lastPage.setPostAdapter(this);
            return lastPage;
        }
///*
        try {
            String json = children.getJSONObject(position).toString();
            Log.d("PostAdapter", "getItem(" + position + "):");
            Log.d("PostAdapter",
                    children.getJSONObject(position)
                            .getJSONObject("data")
                            .getString("title"));
            Log.d("PostAdapter",
                    children.getJSONObject(position)
                            .getJSONObject("data")
                            .getString("url"));

            return PostPage.newInstance(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
        //*/
    }

    @Override
    public int getCount() {
        if (children == null)
            return 0;
        return children.length() + 1;
    }

    public int getNumChildren() {
        if (children == null)
            return 0;
        return children.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "" + (position + 1);
    }

    public String getPermalink(int position) {
        if (children == null || position >= children.length())
            return "";
        try {
            return children
                    .getJSONObject(position)
                    .getJSONObject("data")
                    .getString("permalink");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public JSONObject getChild(int i) {
        if (i < 0 || i >= children.length())
            return null;
        try {
            return children.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void refresh() {
        new HttpAsyncTask() {
            protected void onPostExecute(String result) {
                setJson(result);
            }
        }.execute(url);
    }

    public void loadNext() {
        Uri uri = Uri.parse(url);

        if (uri.getQueryParameter("after") == null) {
            Uri.Builder b = Uri
                    .parse(url)
                    .buildUpon()
                    .appendQueryParameter("after", after);
            url = b.build().toString();
        } else {
            url = url.replaceFirst("after=[^+/]{9}", "after=" + after);
        }

        refresh();
    }

    void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}
