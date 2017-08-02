package com.wgx.love.viewFactory.utils;

import android.util.Log;

public class LoggerUtil {
	private final static boolean FLAG = true;

	public static void i(String tag, String msg) {
		if (FLAG) {
			Log.i(tag, " -------> " + msg);
		}

	}

	public static void e(String tag, String msg) {
		if (FLAG) {
			Log.e(tag, " -------> " + msg);
		}
	}

	public static void d(String tag, String msg) {
		if (FLAG) {
			Log.d(tag, " -------> " + msg);
		}
	}
}
