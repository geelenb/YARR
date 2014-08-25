package be.geelen.yarr.postPages.Imgur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.Fab;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.R;
import be.geelen.yarr.postPages.PostPage;
import be.geelen.yarr.tools.Anim;
import be.geelen.yarr.tools.HttpAsyncTask;
import uk.co.senab.photoview.PhotoView;

public class ImgurPicturePostPage extends PostPage {
    private TextView pictureTitle;
    private PhotoView photoView;
    private TextView pictureCaption;

    private String imgurJSON;
    private JSONObject imgurJsonObject;

    public static ImgurPicturePostPage newInstance (String json) {
        ImgurPicturePostPage defaultPostPage = new ImgurPicturePostPage();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        defaultPostPage.setArguments(args);
        return defaultPostPage;
    }

    public ImgurPicturePostPage() {
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
        View view = inflater.inflate(R.layout.fragment_post_page_imgur_picture, container, false);
        // against id collisions
        view.setId(hashCode());

        TextView postTitle = (TextView) view.findViewById(R.id.post_title);
        pictureTitle = (TextView) view.findViewById(R.id.picture_title);
        photoView = (PhotoView) view.findViewById(R.id.photo_view);
        pictureCaption = (TextView) view.findViewById(R.id.picture_caption);
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
                setImgurJson(result);
            }
        }.execute(toApiURL(url));

        return view;
    }

    private void setImgurJson(String json) {
        this.imgurJSON = json;
        try {
            setImgurJsonObject(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setImgurJsonObject(JSONObject jsonObject) throws JSONException {
        this.imgurJsonObject = jsonObject;

        String title = imgurJsonObject
                .getJSONObject("image")
                .getJSONObject("image") // I didn't invent this shit
                .optString("title", "");

        String url = imgurJsonObject
                .getJSONObject("image")
                .getJSONObject("links")
                .optString("large_thumbnail", "http://i.imgur.com/removed.png");

        String caption = imgurJsonObject
                .getJSONObject("image")
                .getJSONObject("image")// okay, I could have shortened it.
                .optString("caption", "");

        if (!title.equals("null"))
            pictureTitle.setText(title);

        // animated is for later
        Picasso.with(photoView.getContext())
                .load(url)
                .into(photoView);

        if (!caption.equals("null"))
            pictureCaption.setText(caption);
    }


    private static String toApiURL(String url) {
        String albumHash = url.substring(url.lastIndexOf("imgur.com/") + 10);
        int index = albumHash.indexOf('/');
        if (index != -1)
            albumHash = albumHash.substring(0, index);

        index = albumHash.indexOf('.');
        if (index != -1)
            albumHash = albumHash.substring(0, index);

        return "http://api.imgur.com/2/image/" + albumHash + ".json";
    }
}
