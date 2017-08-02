package com.wgx.love.viewFactory.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wgx.love.viewFactory.R;
import com.wgx.love.viewlibs.CircularProgressBar;

public class ProgressBarFragment extends Fragment implements View.OnClickListener {

    private TextView tv;
    private CircularProgressBar mCircularProgressBar;
    private Button bt_in, bt_re;

    private float oneProgress = 0f;
    private float twoProgress = 0f;

    private View mRootView;

    public static ProgressBarFragment newInstance() {
        ProgressBarFragment fragment = new ProgressBarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_progressbar, container, false);
        init();
        showSecondProgress();
        return mRootView;
    }

    private void init() {
        mCircularProgressBar = (CircularProgressBar) mRootView.findViewById(R.id.progressBar);
        bt_in = (Button) mRootView.findViewById(R.id.bt_in);
        bt_re = (Button) mRootView.findViewById(R.id.bt_re);
        tv = (TextView) mRootView.findViewById(R.id.tv);
        bt_in.setOnClickListener(this);
        bt_re.setOnClickListener(this);
        mCircularProgressBar.setOneProgress(oneProgress);
        mCircularProgressBar.setTwoProgress(twoProgress);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_in:
                oneProgress = oneProgress + 4;
                if (oneProgress < 0)
                    oneProgress = 0;
                else if (oneProgress > 100)
                    oneProgress = 100;
                mCircularProgressBar.setOneProgress(oneProgress);
                break;

            case R.id.bt_re:
                oneProgress = oneProgress - 4;
                if (oneProgress < 0)
                    oneProgress = 0;
                else if (oneProgress > 100)
                    oneProgress = 100;
                mCircularProgressBar.setOneProgress(oneProgress);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv.setText(twoProgress + "");
            mCircularProgressBar.setTwoProgress(twoProgress);
        }
    };

    private void showSecondProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (twoProgress < 100) {
                    twoProgress = twoProgress + 4;
                    mHandler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
