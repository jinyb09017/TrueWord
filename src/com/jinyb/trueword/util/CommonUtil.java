package com.jinyb.trueword.util;

import java.io.InputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * <公共工具类>
 *
 * @author 付禄山
 * @date 创建时间：2013-12-18 下午10:59:46 
 * @Copyright  晨阳工作室  All rights reserved.
 */
public class CommonUtil {
	public static void showInfoDialog(Context context, String message) {
		showInfoDialog(context, message, "提示", "确定","取消", null);
	}
	
	public static void showPositiveInfoDialog(Context context, String message) {
		showInfoDialog(context, message, "提示", "确定",null, null);
	}

	public static boolean isValidEmail(String paramString) {

		String regex = "[a-zA-Z0-9_\\.]{1,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
		if (paramString.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidMobiNumber(String paramString) {
		String regex = "^1\\d{10}$";
		if (paramString.matches(regex)) {
			return true;
		}
		return false;
	}

	public static void showInfoDialog(Context context, String message, String titleStr, String positiveStr,
			String negative,DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setTitle(titleStr);
		localBuilder.setMessage(message);
		if (onClickListener == null)
			onClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			};
		if(positiveStr != null)
		localBuilder.setPositiveButton(positiveStr, onClickListener);
		if(negative != null)
		localBuilder.setNegativeButton(negative, onClickListener);
		localBuilder.show();
	} 
	//根据路径获得位图
	public static Bitmap getBitmap(Context context,String path){
		InputStream ins = null;
		Bitmap bitmap = null;
		try {
			ins = context.getAssets().open(path);
			bitmap = BitmapFactory.decodeStream(ins);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bitmap;
	}
}
