package be.geelen.yarr.postPages;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.Fab;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import be.geelen.yarr.R;
import be.geelen.yarr.tools.CollapseViewOnTouchListener;
import uk.co.senab.photoview.PhotoView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ImagePostPage extends PostPage {
    private TextView pictureTitle;
    private PhotoView photoView;

    private static final String SRC_VAL = "SRC_VAL";
    private String src;

    public static ImagePostPage newInstance (String json) {
        ImagePostPage imagePostPage = new ImagePostPage();
        Bundle args = new Bundle();

        args.putString(JSON_VAL, json);

        imagePostPage.setArguments(args);
        return imagePostPage;
    }

    public ImagePostPage() {
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
        view.setId(((Object) this).hashCode());


        TextView postTitle = (TextView) view.findViewById(R.id.post_title);
        pictureTitle = (TextView) view.findViewById(R.id.picture_title);
        photoView = (PhotoView) view.findViewById(R.id.photo_view);
        Fab fab = (Fab) view.findViewById(R.id.reddit_comment_fab);

        String url = "";

        try {
            postTitle.setText(postObject.getJSONObject("data").getString("title"));
            url = postObject.getJSONObject("data").getString("url");

            Picasso .with(container.getContext())
                    .load(url)
                    .into(photoView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        postTitle.setOnTouchListener(new CollapseViewOnTouchListener());

        return view;
    }
}
