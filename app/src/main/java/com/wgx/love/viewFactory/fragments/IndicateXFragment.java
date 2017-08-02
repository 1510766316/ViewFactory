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

/**
 * Created by wugx on 17-8-1.
 */

public class IndicateXFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "IndicateXFragment";
    private IndicateX mIndicateX;
    private Button mSwitchBT;

    private String[] array = {"One", "Two", "Three", "Four", "Five"};
    private int index = 0;
    private View mRootView;

    public static IndicateXFragment newInstance() {
        Bundle args = new Bundle();
        IndicateXFragment fragment = new IndicateXFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_indicatex, container, false);
        init();
        return mRootView;
    }

    private void init() {
        mIndicateX = (IndicateX) mRootView.findViewById(R.id.indicate);
        mIndicateX.addTabData(array);
        mIndicateX.setIndicateOnClickListener(new IndicateX.IndicateOnClickListener() {

            @Override
            public void onClick(View view, int position, String msg) {
                Snackbar.make(view, "position:" + position + " ---msg:" + msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mSwitchBT = (Button) mRootView.findViewById(R.id.switchBT);
        mSwitchBT.setOnClickListener(this);
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
                index += 1;
                if (index > array.length - 1) {
                    index = 0;
                }
                mIndicateX.setSelectNum(index);
                break;
            default:
                break;
        }
    }
}
