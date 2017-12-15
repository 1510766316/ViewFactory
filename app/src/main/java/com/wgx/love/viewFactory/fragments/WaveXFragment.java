package com.wgx.love.viewFactory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wgx.love.viewFactory.R;
import com.wgx.love.viewlibs.IndicateX;
import com.wgx.love.viewlibs.WaveX;

/**
 * Created by wugx on 17-8-1.
 */

public class WaveXFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "WaveXFragment";
    private Button mSwitchBT;
    private View mRootView;
    private WaveX mWaveX;

    public static WaveXFragment newInstance() {
        Bundle args = new Bundle();
        WaveXFragment fragment = new WaveXFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_wavex, container, false);
        init();
        return mRootView;
    }

    private void init() {
        mWaveX = (WaveX) mRootView.findViewById(R.id.mWaveX);
        mSwitchBT = (Button) mRootView.findViewById(R.id.switchBT);
        mSwitchBT.setOnClickListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mWaveX.isPlaying()){
            mWaveX.stopPlay();
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchBT:
                mWaveX.startAnimation();
                break;
            default:
                break;
        }
    }
}
