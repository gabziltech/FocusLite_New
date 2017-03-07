package com.example.focuslite.MainFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.example.focuslite.R;

public class FragmentVideos extends Fragment {

    public FragmentVideos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        getActivity().setTitle("Demo Videos");
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }
}
