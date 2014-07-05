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
	 * ��ѯ����
	 * @param context
	 * @return
	 */
	/**
	 * ����ϡ�еĹ��췽�������ܽ���new����ʵ����
	 */
	private GradeManager() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * ����Ωһ��ʵ��
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
	    Log.d("test","�������Ϊ:"+myPointBalance);
	    return myPointBalance;
	}
	/**
	 * ���ѻ���
	 * @param context
	 * @return
	 */
	/**
	 * ���ﶨ����ֱ仯�󵯳�����¼�
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
			//���ϻ�û���
			dialog.dismiss();
			break;			
		}
		
	}


}
