package com.wgx.love.viewFactory.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.wgx.love.viewFactory.MainActivity;

/**
 * Created by wugx on 17-9-4.
 */

public class BaseFragment extends Fragment {
    protected  MainActivity mainActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;
    }
}
