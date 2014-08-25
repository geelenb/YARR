package be.geelen.yarr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.postPages.PostPage;
import be.geelen.yarr.tools.HttpAsyncTask;

public class PostAdapter extends FragmentStatePagerAdapter {
    private JSONArray children;

    public PostAdapter(FragmentManager fm, String url) {
        super(fm);

        new HttpAsyncTask() {
            protected void onPostExecute(String result) {
                setJson(result);
            }
        }.execute(url);
    }

    void setJson(String json) {
        try {
            children = new JSONObject(json)
                    .getJSONObject("data")
                    .getJSONArray("children");
            notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("PostAdapter::setJson", "no json to be set, check connectivity");
            e.printStackTrace();
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (children == null)
            return null;

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
    }

    @Override
    public int getCount() {
        if (children == null)
            return 0;
        return children.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "" + (position + 1);
    }

    public String getPermalink(int position) {
        if (children == null)
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
        try {
            return children.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
