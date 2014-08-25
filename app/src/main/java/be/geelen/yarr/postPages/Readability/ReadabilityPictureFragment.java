package be.geelen.yarr.postPages.Readability;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.geelen.yarr.R;
import be.geelen.yarr.tools.Anim;

public class ReadabilityPictureFragment extends Fragment {
    protected final static String TITLE_VAL = "TITLE_VAL";
    protected final static String IMG_SRC = "IMG_SRC";
    protected final static String DESCRIPTION_SRC = "DESCRIPTION_VAL";

    private String title;
    private String src;
    private String description;

    public ReadabilityPictureFragment() {
    }

    public static ReadabilityPictureFragment newInstance (String title, String src, String description) {
        ReadabilityPictureFragment readabilityPictureFragment = new ReadabilityPictureFragment();
        Bundle args = new Bundle();

        args.putString(TITLE_VAL, title);
        args.putString(IMG_SRC, src);
        args.putString(DESCRIPTION_SRC, description);

        readabilityPictureFragment.setArguments(args);
        return readabilityPictureFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString(TITLE_VAL);
            this.src = getArguments().getString(IMG_SRC);
            this.description = getArguments().getString(DESCRIPTION_SRC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_title_caption, container, false);

        TextView titleTextView = (TextView) view.findViewById(R.id.picture_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.photo_view);
        TextView captionTextView = (TextView) view.findViewById(R.id.picture_caption);

        if (!title.isEmpty())
            titleTextView.setText(Html.fromHtml("<u>" + title + "</u>"));

        if (src != null) {
            Picasso.with(container.getContext())
                    .load(src)
                    .into(imageView);
        } else {
            Anim.collapse(imageView);
        }

        captionTextView.setText(description);

        return view;
    }
}
