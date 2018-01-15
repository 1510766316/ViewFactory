package com.wgx.love.viewFactory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wgx.love.viewFactory.R;
import com.wgx.love.viewlibs.IndicateX;
import com.wgx.love.viewlibs.VolumeProgressBar;

/**
 * Created by wugx on 17-8-1.
 */

public class VolumeProgressFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "IndicateXFragment";

    private View mRootView;

    private Button reBt, inBt;
    private VolumeProgressBar volume;
    private TextView tmp;
    private int num = 0;

    public static VolumeProgressFragment newInstance() {
        Bundle args = new Bundle();
        VolumeProgressFragment fragment = new VolumeProgressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_volumeprogress, container, false);
        init();
        return mRootView;
    }

    private void init() {
        reBt = (Button) mRootView.findViewById(R.id.reduce);
        inBt = (Button) mRootView.findViewById(R.id.increase);
        volume = (VolumeProgressBar) mRootView.findViewById(R.id.volume);
        volume.addVolumeTouchListener(mListener);
        tmp = (TextView) mRootView.findViewById(R.id.tmp);
        reBt.setOnClickListener(this);
        inBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reduce:
                num--;
                if (num == 0) {
                    num = 0;
                    return;
                }
                volume.setProgress(num);
                break;
            case R.id.increase:
                num++;
                if (num > 30) {
                    num= 30;
                    return;
                }
                volume.setProgress(num);
                break;
            default:
                break;
        }
        tmp.setText("drawNum="+volume.getDrawNum()+"  progress="+volume.getProgressNum());
    }

    private VolumeProgressBar.VolumeProgressListener mListener = new VolumeProgressBar.VolumeProgressListener() {
        @Override
        public void onTouchListener(int pro) {
            num = pro;
            tmp.setText("drawNum="+volume.getDrawNum()+"  progress="+pro);
        }
    };
}
