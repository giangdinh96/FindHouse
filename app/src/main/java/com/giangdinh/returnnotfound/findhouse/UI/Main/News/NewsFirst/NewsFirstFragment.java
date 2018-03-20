package com.giangdinh.returnnotfound.findhouse.UI.Main.News.NewsFirst;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.R;

public class NewsFirstFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_house_first, container, false);
        return view;
    }
}
