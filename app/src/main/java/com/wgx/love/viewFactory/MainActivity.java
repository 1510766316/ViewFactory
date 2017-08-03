package com.wgx.love.viewFactory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wgx.love.viewFactory.fragments.ClockXFragment;
import com.wgx.love.viewFactory.fragments.IndicateXFragment;
import com.wgx.love.viewFactory.fragments.MusicDancerFragment;
import com.wgx.love.viewFactory.fragments.ProgressBarFragment;
import com.wgx.love.viewFactory.fragments.TestFragment;
import com.wgx.love.viewFactory.fragments.WaveXFragment;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private Context mContext;
    private Fragment[] fragments;
    private int index;
    private int currentIndex = -1;

    private IndicateXFragment mIndicateXFragment;
    private WaveXFragment mWaveXFragment;
    private ClockXFragment mClockXFragment;
    private ProgressBarFragment mProgressBarFragment;
    private MusicDancerFragment mMusicDancerFragment;


    private TestFragment mTestFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        mIndicateXFragment = IndicateXFragment.newInstance();
        mWaveXFragment = WaveXFragment.newInstance();
        mClockXFragment = ClockXFragment.newInstance();
        mProgressBarFragment = ProgressBarFragment.newInstance();
        mMusicDancerFragment = MusicDancerFragment.newInstance();

        mTestFragment = TestFragment.newInstance();
        fragments = new Fragment[]{mIndicateXFragment, mWaveXFragment, mClockXFragment, mProgressBarFragment, mMusicDancerFragment,mTestFragment};

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:

                break;
            case R.id.action_dialog:
                new MaterialDialog.Builder(this)
                        .title(R.string.action_dialog)
                        .items(R.array.viewItems)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which > fragments.length || which < 0) {
                                    Log.e(TAG, "The selected index is out of fragments arrays!");
                                    return true;
                                }
                                if (currentIndex == which) {
                                    return true;
                                }
                                index = which;
                                switcherFragment(index);
                                return true;
                            }
                        })
                        .positiveText(R.string.sure)
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void switcherFragment(int index) {
        if (currentIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            if (currentIndex > -1) {
                trx.hide(fragments[currentIndex]);
            }
            if (!fragments[index].isAdded()) {
                trx.add(R.id.contain, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        showModule(index);
        currentIndex = index;
    }

    private void showModule(int index) {
    }
}
