package com.wgx.love.viewFactory.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import android.R.integer;
import android.text.TextUtils;

/**
 * 字符串操作
 * 
 * @author venshine
 */
public class StringUtil {

	/**
	 * Judge whether a string is whitespace, empty ("") or null.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		int strLen;
		if (null == str || (strLen = str.length()) == 0 || str.equalsIgnoreCase("null")) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if a and b are equal, including if they are both null.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(CharSequence a, CharSequence b) {
		return TextUtils.equals(a, b);
	}

	/**
	 * Judge whether a string is number.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Encode a string
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeString(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * Decode a string
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeString(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * Converts this string to lower case, using the rules of {@code locale}.
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowerCase(String s) {
		return s.toLowerCase(Locale.getDefault());
	}

	/**
	 * Converts this this string to upper case, using the rules of
	 * {@code locale}.
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpperCase(String s) {
		return s.toUpperCase(Locale.getDefault());
	}

	/**
	 * 频点转换
	 * 
	 * @param mode
	 * @param freq
	 * @return
	 */
	public static String stringChange(int mode, int freq) {
		return mode == 1 ? String.format(Locale.US, "%.2f", (float) freq) : String.valueOf(freq);
	}

	/**
	 * 删除指定一个元素
	 * 
	 * @param arr
	 * @param num
	 * @return
	 */
	public static int[] remove(int[] arr, int num) {
		int[] tmp = new int[arr.length - 1];
		int idx = 0;
		boolean hasRemove = false;
		for (int i = 0; i < arr.length; i++) {
			if (!hasRemove && arr[i] == num) {
				hasRemove = true;
				continue;
			}
			tmp[idx++] = arr[i];
		}
		return tmp;
	}

	/**
	 * 去重
	 * 
	 * @param arr
	 * @return
	 */
	public static int[] ifRepeat(int[] arr) {
		if (null == arr || arr.length == 0) {
			return new int[]{0};
		} 
		
		Set<Integer> set = new TreeSet<Integer>();//新建一个set集合
        for (int i : arr) {
            set.add(i);
        }
        Integer[] arr2 = set.toArray(new Integer[0]);
        int[] result = new int[arr2.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = arr2[i];
        }
		return result;

	}

}
