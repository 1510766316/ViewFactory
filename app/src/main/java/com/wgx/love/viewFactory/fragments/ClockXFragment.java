package com.wgx.love.viewFactory.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgx.love.viewFactory.R;

public class ClockXFragment extends Fragment {
    private final static String TAG = "ClockXFragment";
    private View mRootView;

    public static ClockXFragment newInstance() {
        ClockXFragment fragment = new ClockXFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_clockx, container, false);
        return mRootView;
    }

}
