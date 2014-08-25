package be.geelen.yarr.postPages.Imgur;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.geelen.yarr.R;

public class ImgurTitlePage extends Fragment {
    public static ImgurTitlePage newInstance() {
        ImgurTitlePage fragment = new ImgurTitlePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public ImgurTitlePage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imgur_titlepage, container, false);

        return view;
    }
}
