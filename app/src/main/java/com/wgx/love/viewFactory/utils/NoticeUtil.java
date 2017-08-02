package com.wgx.love.viewFactory.utils;

import android.content.Context;
import android.widget.Toast;

public class NoticeUtil {

	private static Toast mToast;

	public static void showTips(Context context, String msg, int time) {

		if (null == mToast) {
			mToast = Toast.makeText(context, msg, time);
		} else {
			mToast.setText(msg);
		}
		mToast.show();
	}

}
