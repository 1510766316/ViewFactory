package com.wgx.love.viewFactory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wgx.love.viewFactory.fragments.IndicateXFragment;
import com.wgx.love.viewFactory.fragments.WaveXFragment;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private Context mContext;
    private Fragment[] fragments;
    private int index;
    private int currentIndex = -1;

    private IndicateXFragment mIndicateXFragment;
    private WaveXFragment mWaveXFragment;

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
        fragments = new Fragment[]{mIndicateXFragment, mWaveXFragment};

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
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
                                switch (which) {
                                    case 0:
                                        if (currentIndex == 0) {
                                            return true;
                                        }
                                        index = 0;
                                        break;
                                    case 1:
                                        if (currentIndex == 1) {
                                            return true;
                                        }
                                        index = 1;
                                        break;
                                }
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
            if (currentIndex > -1){
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
