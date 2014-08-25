package be.geelen.yarr.postPages.Gfy;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.faizmalkani.floatingactionbutton.Fab;

import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.R;
import be.geelen.yarr.postPages.PostPage;
import be.geelen.yarr.tools.Anim;
import be.geelen.yarr.tools.HttpAsyncTask;

public class GfyPostPage extends PostPage {
    private VideoView videoView;

    private String gfyJSON;
    private JSONObject gfyJsonObject;

    public static GfyPostPage newInstance (String json) {
        GfyPostPage gfyPostPage = new GfyPostPage();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        gfyPostPage.setArguments(args);
        return gfyPostPage;
    }

    public GfyPostPage() {
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
        View view = inflater.inflate(R.layout.fragment_post_page_gfy, container, false);
        // against id collisions
        view.setId(((Object) this).hashCode());

        TextView postTitle = (TextView) view.findViewById(R.id.post_title);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        Fab fab = (Fab) view.findViewById(R.id.reddit_comment_fab);

        String url = "";

        try {
            postTitle.setText(postObject.getJSONObject("data").getString("title"));
            url = postObject.getJSONObject("data").getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Anim.collapse(view);
                return true;
            }
        });

        new HttpAsyncTask() {
            protected void onPostExecute(String result) {
                SetGfyJson(result);
            }
        }.execute(toApiURL(url));

        return view;
    }

    private void SetGfyJson(String json) {
        this.gfyJSON = json;
        try {
            setGfyJsonObject(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setGfyJsonObject(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null)
            return;

        if (jsonObject.has("gfyItem"))
            jsonObject = jsonObject.getJSONObject("gfyItem");

        this.gfyJsonObject = jsonObject;

//        videoView.setVideoURI(Uri.parse(jsonObject.getString("webmUrl")));
        videoView.setVideoURI(Uri.parse(jsonObject.getString("mp4Url")));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (videoView.isPlaying())
                    videoView.pause();
                else
                    videoView.resume();
                return false;
            }
        });
        videoView.start();
    }

    // examples:
    // http://upload.gfycat.com/transcode?fetchUrl=i.imgur.com/lKi99vn.gif/
    // http://gfycat.com/cajax/get/ScaryGrizzledComet
    private static String toApiURL(String url) {
        if (url.contains("gfycat.com/")) {
            return url.replace("gfycat.com/", "gfycat.com/cajax/get/");
        } else {
            return "upload.gfycat.com/transcode?fetchUrl=" + url;
        }
    }

    /* takes time away from video to load
    @Override
    public void onPageSelected() {
        if (!videoView.isPlaying())
            videoView.start();
    }
    */


    /*
    @Override
    public void onPageExited() {
        if (videoView.isPlaying())
            videoView.stopPlayback();
    }
    */
}
