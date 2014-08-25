package be.geelen.yarr.postPages.Imgur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import be.geelen.yarr.R;

public class ImgurPictureFragment extends Fragment {
    protected final static String JSON_VAL = "JSON_VAL";

    protected String json;
    protected JSONObject rootObject;

    public ImgurPictureFragment() {
    }

    public static ImgurPictureFragment newInstance (String json) {
        ImgurPictureFragment defaultPostPage = new ImgurPictureFragment();
        Bundle args = new Bundle();
        args.putString(JSON_VAL, json);
        defaultPostPage.setArguments(args);
        return defaultPostPage;
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

    public void setJSON(String json) throws JSONException {
        this.json = json;
        this.rootObject = new JSONObject(json);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_title_caption, container, false);

        TextView titleTextView = (TextView) view.findViewById(R.id.picture_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.photo_view);
        TextView captionTextView = (TextView) view.findViewById(R.id.picture_caption);

        try {
            String url = rootObject.getJSONObject("links").getString("large_thumbnail");
            titleTextView.setText(rootObject.getJSONObject("image").getString("title"));
            Picasso .with(container.getContext())
                    .load(url)
                    .into(imageView);
            captionTextView.setText(rootObject.getJSONObject("image").getString("caption"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
