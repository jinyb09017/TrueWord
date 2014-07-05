/**
 * 
 */
package com.jinyb.trueword.util;

import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/**
 * @author jinyb09017
 *
 */
public class GradeManager implements DialogInterface.OnClickListener {
	private static  Context context;
	private static GradeManager gradeManager;
	private int level;
	
	/**
	 * 查询积分
	 * @param context
	 * @return
	 */
	/**
	 * 做成稀有的构造方法，不能进行new进行实例化
	 */
	private GradeManager() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 生成惟一的实例
	 * @param con
	 * @return
	 */
	public static GradeManager getInstance(Context con){
		context = con;
		if(gradeManager==null){
			gradeManager = new GradeManager();
		}
		return gradeManager;
	}
	
	public int query(){
		int myPointBalance = PointsManager.getInstance(context).queryPoints();
	    Log.d("test","积分余额为:"+myPointBalance);
	    return myPointBalance;
	}
	/**
	 * 消费积分
	 * @param context
	 * @return
	 */
	/**
	 * 这里定义积分变化后弹出框的事件
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which){
		case Dialog.BUTTON_NEGATIVE:
			OffersManager.getInstance(context).showOffersWallDialog((Activity) context);
			dialog.dismiss();
			break;
		case Dialog.BUTTON_POSITIVE:
			//马上获得积分
			dialog.dismiss();
			break;			
		}
		
	}


}
