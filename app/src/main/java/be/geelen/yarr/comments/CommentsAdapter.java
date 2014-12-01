package be.geelen.yarr.comments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import be.geelen.yarr.R;
import be.geelen.yarr.tools.HttpAsyncTask;

public class CommentsAdapter extends FragmentStatePagerAdapter {

    private JSONArray commentsArray;
    private HashMap<Integer, CommentFragment> items = new HashMap<Integer, CommentFragment>();
    private String author;

    public CommentsAdapter(FragmentManager fm, String permalink) {
        super(fm);

        new HttpAsyncTask() {
            protected void onPostExecute(String result) {
                setJson(result);
            }
        }.execute("http://reddit.com" + permalink + ".json" + (true ? "?depth=4" : ""));
    }

    public CommentsAdapter(FragmentManager fm, JSONArray comments, String author) {
        super(fm);
        this.author = author;
        setCommentsArray(comments);
    }

    public void setJson(String json) {
        try {
            JSONArray rootArray = new JSONArray(json);
            author = rootArray // [0].data.children[0].data.author
                    .getJSONObject(0)
                    .getJSONObject("data")
                    .getJSONArray("children")
                    .getJSONObject(0)
                    .getJSONObject("data")
                    .getString("author");
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
            CommentFragment commentFragment = CommentFragment.newInstance(commentsArray.getJSONObject(position).toString(), getAuthor());
            items.put(position, commentFragment);
            return commentFragment;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    CommentFragment getItemFromHashMap(int position) {
        return items.get(position);
    }

    public void setCommentsArray(JSONArray commentsArray) {
        this.commentsArray = commentsArray;
        notifyDataSetChanged();
    }

    public JSONObject getChild(int i) throws JSONException {
        return commentsArray.getJSONObject(i);
    }

    public void setAuthor (String author) {
        this.author = author;
    }

    String getAuthor() {
        return author;
    }
}
