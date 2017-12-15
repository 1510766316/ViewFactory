package com.wgx.love.viewFactory.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgx.love.viewFactory.R;

/**
 * Created by imotor on 17-8-2.
 */

public class MusicDancerFragment extends BaseFragment {
    private final static String TAG = "MusicDancerFragment";
    private View mRootView;

    public static MusicDancerFragment newInstance() {
        MusicDancerFragment fragment = new MusicDancerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_musicdance, container, false);
        return mRootView;
    }
}
