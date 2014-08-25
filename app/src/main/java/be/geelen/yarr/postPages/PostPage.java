package be.geelen.yarr.postPages;


import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.postPages.Gfy.GfyPostPage;
import be.geelen.yarr.postPages.Imgur.ImgurAlbumPostPage;
import be.geelen.yarr.postPages.Imgur.ImgurPicturePostPage;
import be.geelen.yarr.postPages.Readability.ReadabilityPostPage;

public abstract class PostPage extends Fragment {
    protected static final String JSON_VAL = "json_val";

    protected JSONObject postObject;

    protected void setJSON(String json) throws JSONException {
        postObject = new JSONObject(json);
    }

    public static PostPage newInstance(String json) {
        PostPage newInstance = DefaultPostPage.newInstance(json);

        try {
            JSONObject postObject = new JSONObject(json);

            if (postObject.getJSONObject("data").getBoolean("is_self")) {
                newInstance = SelfPostPage.newInstance(json);
            } else {
                String url = postObject.getJSONObject("data").getString("url").toLowerCase();

                if (url.contains("imgur.com/a/")) {
                    newInstance = ImgurAlbumPostPage.newInstance(json);
                } else if (url.contains("imgur.com/")) {
                    // todo: other imgur links than imgur pictures, imgur.com/gallery
                    newInstance = ImgurPicturePostPage.newInstance(json);
                } else if (url.contains("gfycat.com/")) {
                    newInstance = GfyPostPage.newInstance(json);
                } else if (url.endsWith(".jpg") || url.endsWith(".png")) {
                    newInstance = ImagePostPage.newInstance(json);
                } else if (!url.contains("youtube.com/") &&
                        !url.contains("vimeo.com/") &&
                        !url.contains("twitter.com/") &&
                        !url.contains("wikipedia.org/") &&
                        !url.contains("liveleak.com/")) {
                    newInstance = ReadabilityPostPage.newInstance(json);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            newInstance.setJSON(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newInstance;
    }

    public void onPageSelected() {}

    public void onPageExited() {}

    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
}
