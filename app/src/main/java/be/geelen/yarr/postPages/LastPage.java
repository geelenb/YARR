package be.geelen.yarr.postPages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.geelen.yarr.PostAdapter;
import be.geelen.yarr.R;

/**
 * Created by bram on 27/08/2014.
 */
public class LastPage extends Fragment {

//    private static final String PREVIOUS_URL = "PREVIOUS_URL";
    private View view;
//    private String previousUrl;
    private PostAdapter postAdapter;
    private Button againButton;
    private Button nextButton;

    public static LastPage newInstance() { //String previousUrl) {
        LastPage lastPage = new LastPage();
        Bundle args = new Bundle();
//        args.putString(PREVIOUS_URL, previousUrl);
        lastPage.setArguments(args);
        return lastPage;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            this.previousUrl = getArguments().getString(PREVIOUS_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_last_post, container, false);
        // against id collisions
        view.setId(hashCode());

        nextButton = (Button) view.findViewById(R.id.lastpage_load_next_pages_button);
        againButton = (Button) view.findViewById(R.id.lastpage_load_link_again);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAdapter.loadNext();
            }
        });

        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAdapter.refresh();
            }
        });

        return view;
    }

    public void setPostAdapter(PostAdapter postAdapter) {
        this.postAdapter = postAdapter;
    }
}
