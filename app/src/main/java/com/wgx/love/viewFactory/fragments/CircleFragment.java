package com.wgx.love.viewFactory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgx.love.viewFactory.R;
import com.wgx.love.viewlibs.DrawCircle;

public class CircleFragment extends BaseFragment {
    private final static String TAG = "ClockXFragment";
    private View mRootView;

    private TextView textView1,textView2;
    private DrawCircle drawCircle;
    public static CircleFragment newInstance() {
        CircleFragment fragment = new CircleFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_drawcircle, container, false);
        textView1 = (TextView) mRootView.findViewById(R.id.text1);
        textView2 = (TextView) mRootView.findViewById(R.id.text2);
        drawCircle = (DrawCircle) mRootView.findViewById(R.id.circle);
        drawCircle.addDrawCircleListener(drawListener);
        drawCircle.setDrawParms(8750,10800,50);
        return mRootView;
    }

    private DrawCircle.DrawCircleListener drawListener = new DrawCircle.DrawCircleListener(){
        @Override
        public void onMoveDraw(int tmpPro) {
            textView1.setText("临时: "+tmpPro);
        }

        @Override
        public void onUpDraw(int pro) {
            textView2.setText("最终: "+pro);
        }
    };
}
