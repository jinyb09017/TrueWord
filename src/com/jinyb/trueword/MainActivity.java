package com.jinyb.trueword;
import java.util.Random;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import com.jinyb.trueword.R;
import com.jinyb.trueword.base.BaseActivity;
import com.jinyb.trueword.custome.CustomDialog;
import com.jinyb.trueword.data.DBManager;
import com.jinyb.trueword.listener.ShakeListener;
import com.jinyb.trueword.listener.ShakeListener.OnShakeListener;
import com.jinyb.trueword.model.State;
import com.jinyb.trueword.util.CommonUtil;
import com.jinyb.trueword.util.GradeManager;
import com.jinyb.trueword.util.StateInstance;
import com.sj.qrcodescanner.QManager;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends BaseActivity implements DialogInterface.OnClickListener{
	
	public static String Tag = "MainActivity";

	public DBManager dbHelper;
	private Button pre,next,setting;
	private TextView content,tip,grade;
	private State state;
	public  Cursor c;
	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	MediaPlayer mPlayer;
	CheckBox voice ;
	RadioGroup catalog ;
	Spinner spinner ;
	RadioButton radio1,radio2;
	View layout;
	Boolean dialogFalg = false;
	public final Integer limitGrade = 100;
	
	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      //drawerSet ();//设置  drawer监听    切换 按钮的方向
		
      	mVibrator = (Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
      	
      	ReadFile();
        init();        
        initGuanggao();
        
        
        mShakeListener = new ShakeListener(this);
        
        mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				//Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
//				startAnim();  //开始 摇一摇手掌动画
				mShakeListener.stop();
				startVibrato(); //开始 震动
				if(state.getVoice()==1){
					mPlayer.start();
				}
				
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						mVibrator.cancel();
						dialogFalg = true;
						showAlertDialog(null, getRandom(), "哈哈哈哈，不许耍赖", "确定",null,new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub								
								dialog.dismiss();
								dialogFalg = false;
								mShakeListener.start();//重新开始监听
								if(state.getVoice()==1){
									mPlayer.pause();//暂停
								}
								
							}
						});
							   
							   
					}
				}, 1000);
			}
		});
                
    }
    
	
	private void initGuanggao() {
		// TODO Auto-generated method stub
		AdManager.getInstance(this).init("2ad854c3d3ed4658",
				"7c5a9a634ee7a025", false);//false表示不是测试模式
		// 请务必调用以下代码，告诉SDK应用启动，可以让SDK进行一些初始化操作。该接口务必在SDK的初始化接口之后调用。
		OffersManager.getInstance(this).onAppLaunch();
		// 检查有米积分广告墙是否启用成功，true为成功
		// 使用sdk自带的积分托管，不自定义积分账户的配置检查:
//		OffersManager off = OffersManager.getInstance(this);
//		if (off.checkOffersAdConfig())
//			Log.e("youmi", "测试成功");
//		else
//			Log.e("youmi", "测试失败");
		// 不使用sdk自带的积分托管，使用自定义积分账户的配置检查：
		// Boolean checkResult =
		// net.youmi.android.offers.OffersManager.checkOffersAdConfig(true);
		// call back the juyou push interface
		QManager manager = QManager.getInstance(this);
		manager.setJYId(this, "9a885b398e9575abfaa49bedc146e1ea");
		//360cn:9a885b398e9575abfaa49bedc146e1ea
		manager.setJYChannelId(this, "360cn");//
		manager.getMessage(this, true);// post ads
		

//		com.umeng.socom.Log.LOG = true;
		
//		Log.e("umtest", getDeviceInfo(this));
		//友盟的更新服务
		//UmengUpdateAgent.setUpdateOnlyWifi(false);
		 UmengUpdateAgent.update(this);

	}
    
	public void startVibrato(){		//定义震动
		mVibrator.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}
	
    
    public void ReadFile(){
    	state = StateInstance.getStateInstance();
    	state.setCategory(read("category"));
    	state.setRank(read("rank"));
    	state.setVoice(read("voice"));
    	
    }
    /**
     * get the cursor according state;
     * @return
     */
    public Cursor getCursor(){
    	 return dbHelper.getDatabase().query("items", new String[]{"item_content"}, "item_type=? and item_dirty=?", 
         		new String[]{state.getCategory()+"",state.getRank()+""}, null,
                 null, null);
    	
    }
    public void init(){
    	
    	mPlayer=MediaPlayer.create(getApplicationContext(), R.raw.xingchi);
//    	mPlayer.setLooping(true);//设置可以重复播放

        pre =(Button) findViewById(R.id.pre);
        next =(Button) findViewById(R.id.next);
        content = (TextView) findViewById(R.id.maincontent);
        grade = (TextView) findViewById(R.id.grade);
        
//        LinearLayout parent = (LinearLayout)content.getParent();
//        int width = parent.getWidth();
//        int height = parent.getHeight();
//        content.setWidth(width/3);
//        content.setHeight(height/3);
        
        
        
        tip = (TextView) findViewById(R.id.tip);
        setting = (Button) findViewById(R.id.setting);
//        page = (TextView) findViewById(R.id.page);
        
        //首次执行导入.db文件
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
        
        c = getCursor();
        c.moveToFirst();
        
        content.setText(c.getString(0));
        tip.setText(StateInstance.getTip());
//        page.setText(1+"/"+c.getCount());
        if(GradeManager.getInstance(this).query() < limitGrade){
        	grade.setText("当前积分:"+GradeManager.getInstance(this).query());
        }else{
        	grade.setText("当前积分:vip会员，永久免费使用");
        }
        
        pre.setOnClickListener(click);
        next.setOnClickListener(click);
        setting.setOnClickListener(click);
        
        
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.setting, null);
    	
    	voice = (CheckBox)layout.findViewById(R.id.voice);
    	catalog = (RadioGroup)layout.findViewById(R.id.catagory);
    	spinner = (Spinner)layout.findViewById(R.id.spinner);
    	
 
    	radio1=(RadioButton)layout.findViewById(R.id.radio1);  
    	radio2=(RadioButton)layout.findViewById(R.id.radio2); 
    	
    	
    	catalog.setOnCheckedChangeListener(radioListen);
    	spinner.setOnItemSelectedListener(new SpinnerListener());
    	
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	dbHelper.closeDatabase();//close the database connect;
    	
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
		mPlayer.stop();
    }
    
    
    OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.pre:
				
				if(c.isFirst()){
					Toast.makeText(MainActivity.this, "已经是第一条记录了", Toast.LENGTH_SHORT).show();
					break;
				}
					
				c.moveToPrevious();
				content.setText(c.getString(0));
//				page.setText(c.getPosition()+1+"/"+c.getCount());
				break;
			case R.id.next:
				if(c.isLast()){
					Toast.makeText(MainActivity.this, "已经是最后一条记录了", Toast.LENGTH_SHORT).show();
					break;
				}
					
				c.moveToNext();
				content.setText(c.getString(0));
//				page.setText(c.getPosition()+1+"/"+c.getCount());
				break;
			case R.id.setting:

	        	
	        	
	        	if(state.getCategory() == 0)
	        		radio1.setChecked(true);
	        	else
	        		radio2.setChecked(true);
	        	
	        	spinner.setSelection(state.getRank());
	        	
	        	if(state.getVoice() == 1)
	        		voice.setChecked(true);
	        	else
	        		voice.setChecked(false);
	        	
	        	
				showAlertDialog(layout, null, "设置", "确定", "取消", onclick);
			default:
				;
			}
			
		}
	};
	
	DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch(which){
			case Dialog.BUTTON_POSITIVE:
				if(voice.isChecked())
	        		state.setVoice(1);
	        	else state.setVoice(0);
				dialog.dismiss();					        					
				//点击确定，如果选择了重口味的话，则需要下载一百个积分后，永久使用。
				if(state.getRank() == 2 && GradeManager.getInstance(MainActivity.this).query()<limitGrade){
					CommonUtil.showInfoDialog(MainActivity.this,"主人，积分达到100成为vip后，才能永久免费使用人家啦","致我亲爱的主人","取消","马上获得积分",MainActivity.this);
					//重新改变rank的状态
					state.setRank(read("rank"));//重新读取其中的值。state别外两个状态是可以改变的。
					break;//直接退出
				}									
	        	Log.i(Tag,"voice:"+state.getVoice());       		        
	        	saveFile();
	       	    //移除layout，防止报状态异常的错误。
	        	LinearLayout view = (LinearLayout) layout.getParent();
	        	view.removeAllViews();
	        	
	        	c = getCursor();//reset the cursor.
	            c.moveToFirst();
	            content.setText(c.getString(0));
	            
	            tip.setText(StateInstance.getTip());
	        	
				break;
			case Dialog.BUTTON_NEGATIVE:
				//重置state的状态
		    	state.setCategory(read("category"));
		    	state.setRank(read("rank"));
		    	state.setVoice(read("voice"));
				dialog.dismiss();
				break;
			}
			
		}
	};
	/**
	 * 
	 */
	private void saveFile() {
		// TODO Auto-generated method stub
    	
    	write("category",state.getCategory());
    	write("rank",state.getRank());
    	write("voice",state.getVoice());
    	
		

	}
	
	RadioGroup.OnCheckedChangeListener radioListen = new RadioGroup.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			Log.i(Tag,"radiogroup:"+checkedId);
			
			if(checkedId == radio1.getId())
        	state.setCategory(0);
			else state.setCategory(1);
			
		}
	};
	
  class SpinnerListener implements OnItemSelectedListener{

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
    	Log.i(Tag, "Spinner："+arg2+"");
    	state.setRank(arg2);
		
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	  
  }
  
  public String getRandom(){
	  Cursor cursor = null;
	  c = getCursor();
	  
	  int num;
	  
	  Random rand = new Random(); //promise the random num got is defferent.
	  num = rand.nextInt(c.getCount());
	  
	  c.moveToPosition(num);
	  
	  return c.getString(0);
	  
	  
//	  while(num == lastRandomNum){
//		  num = rand.nextInt(c.getCount());
//	  }
//	  lastRandomNum = num;
//	  
	  
	  
  }
   @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();会调用finish()事件
    	mShakeListener.start();
    	mPlayer.pause();
    	if(mShakeListener.flag ==0){
    		mShakeListener.start();}//如果dialog是打开的状态，则重新启用shake
        else{
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					MainActivity.this);
			alertDialog.setTitle("退出");
			alertDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							finish();

						}
					});
			alertDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
    		
    	}
    }
  
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which){
		case Dialog.BUTTON_NEGATIVE:
			OffersManager.getInstance(this).showOffersWallDialog(this);
			dialog.dismiss();
			break;
		case Dialog.BUTTON_POSITIVE:
			//马上获得积分
			notitycation();
			dialog.dismiss();
			break;			
		}
		
	}
	
	
	public void notitycation(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		.setContentTitle("真心话大冒险")
		.setContentText("this is a test")
		.setSmallIcon(R.drawable.ic_launcher);
		
		Intent intent = new Intent(this,SplashActivity.class);
		 
		TaskStackBuilder taskstack = TaskStackBuilder.create(this);
		taskstack.addParentStack(SplashActivity.class);
		taskstack.addNextIntent(intent);
		
		PendingIntent pendingIntent = taskstack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		NotificationManager mm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mm.notify(1, builder.build());

		
	}

}