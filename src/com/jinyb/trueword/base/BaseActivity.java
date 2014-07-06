/**
 * 
 */
package com.jinyb.trueword.base;

import com.jinyb.trueword.config.SysConfig;
import com.jinyb.trueword.custome.CustomDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * @author jinyb 下午7:48:33 copyright all reserved
 */
public class BaseActivity extends Activity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private SharedPreferences sharedPreference;
	private SharedPreferences.Editor editor;
	public final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sharedPreference = getSharedPreferences("trueword", MODE_PRIVATE);
		editor = sharedPreference.edit();

	}

	public int read(String key) {
		if (sharedPreference != null) {
			if (key.equals("voice"))
				return sharedPreference.getInt(key, 1);
			return sharedPreference.getInt(key, 0);

		}
		return 0;
	}

	public void write(String key, int value) {
		if (sharedPreference != null) {
			editor.putInt(key, value);
			editor.commit();
		}
	}

	public void showAlertDialog(View view, String message, String title,
			String postive, String negative,
			DialogInterface.OnClickListener lister) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setContentView(view);
		if (lister == null) {
			builder.setPositiveButton(postive,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// 设置你的操作事项
						}
					});
			builder.setNegativeButton(negative,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		} else {
			builder.setPositiveButton(postive, lister);
			builder.setNegativeButton(negative, lister);
		}

		builder.create().show();

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "我要分享");
		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以
		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的

		return true;

	}

	// 菜单项被选择事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			// Toast.makeText(this, "删除菜单被点击了", Toast.LENGTH_LONG).show();
			mController.openShare(this, false);
			break;

		}

		return false;
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		// return super.onCreateDialog(id);
		Dialog dialog = null;
		CustomDialog.Builder builder =null;
		switch (id) {
		case SysConfig.Constants.SETTING:
			builder = new CustomDialog.Builder(this);
			builder.setMessage("setting");
			builder.setPositiveButton("confirm", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog =  builder.create();
			;
		case SysConfig.Constants.NOTICE:
			builder = new CustomDialog.Builder(this);
			builder.setMessage("setting");
			builder.setPositiveButton("confirm", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog =  builder.create();
			;
		}
		return dialog;
	}

}
