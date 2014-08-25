package be.geelen.yarr.comments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.tools.HttpAsyncTask;

public class CommentsAdapter extends FragmentStatePagerAdapter {

    private JSONArray commentsArray;

    public CommentsAdapter(FragmentManager fm, String permalink) {
        super(fm);

        new HttpAsyncTask() {
            protected void onPostExecute(String result) {
                setJson(result);
            }
        }.execute("http://reddit.com" + permalink + ".json" + (true ? "?depth=5" : ""));
    }

    public CommentsAdapter(FragmentManager fm, JSONArray comments) {
        super(fm);
        setCommentsArray(comments);
    }

    public void setJson(String json) {
        try {
            JSONArray rootArray = new JSONArray(json);
            setCommentsArray(
                    rootArray
                            .getJSONObject(1)
                            .getJSONObject("data")
                            .getJSONArray("children"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if (commentsArray == null)
            return 0;

        return commentsArray.length();
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return CommentFragment.newInstance(commentsArray.getJSONObject(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCommentsArray(JSONArray commentsArray) {
        this.commentsArray = commentsArray;
        notifyDataSetChanged();
    }

    public JSONObject getChild(int i) throws JSONException {
        return commentsArray.getJSONObject(i);
    }

    public int getChildCount() {
        return commentsArray.length();
    }
}
