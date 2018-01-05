package com.wgx.love.viewFactory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.wgx.love.viewFactory.fragments.CircleFragment;
import com.wgx.love.viewFactory.fragments.ClockXFragment;
import com.wgx.love.viewFactory.fragments.IndicateXFragment;
import com.wgx.love.viewFactory.fragments.MusicDancerFragment;
import com.wgx.love.viewFactory.fragments.ProgressBarFragment;
import com.wgx.love.viewFactory.fragments.TestFragment;
import com.wgx.love.viewFactory.fragments.VolumeProgressFragment;
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
    private CircleFragment mCircleFragment;

    private VolumeProgressFragment mVolumeProgressFragment;

    private TestFragment mTestFragment;

    private Bitmap LargeBitmap = null;
    private NotificationManager myManager = null;
    private Notification myNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //1.从系统服务中获得通知管理器
        myManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        init();
    }

    private void init() {
        mIndicateXFragment = IndicateXFragment.newInstance();
        mWaveXFragment = WaveXFragment.newInstance();
        mClockXFragment = ClockXFragment.newInstance();
        mProgressBarFragment = ProgressBarFragment.newInstance();
        mMusicDancerFragment = MusicDancerFragment.newInstance();
        mTestFragment = TestFragment.newInstance();
        mCircleFragment = CircleFragment.newInstance();
        mVolumeProgressFragment = VolumeProgressFragment.newInstance();
        fragments = new Fragment[]{mIndicateXFragment, mWaveXFragment, mClockXFragment, mProgressBarFragment, mMusicDancerFragment,mCircleFragment,mVolumeProgressFragment,mTestFragment};

        PendingIntent pi = PendingIntent.getActivity(
                mContext,
                100,
                new Intent(mContext, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        Notification.Builder myBuilder = new Notification.Builder(mContext);
        myBuilder.setContentTitle("QQ")
                .setContentText("这是内容")
                .setSubText("这是补充小行内容")
                .setTicker("您收到新的消息")
                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置默认声音和震动
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(false)//点击
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_HIGH)//高优先级
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                //android5.0加入了一种新的模式Notification的显示等级，共有三种：
                //VISIBILITY_PUBLIC  只有在没有锁屏时会显示通知
                //VISIBILITY_PRIVATE 任何情况都会显示通知
                //VISIBILITY_SECRET  在安全锁和没有锁屏的情况下显示通知
                .setContentIntent(pi);  //3.关联PendingIntent

        myNotification = myBuilder.build();
        //4.通过通知管理器来发起通知，ID区分通知
        myManager.notify(1, myNotification);
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
