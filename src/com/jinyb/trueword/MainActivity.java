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
        
      //drawerSet ();//����  drawer����    �л� ��ť�ķ���
		
      	mVibrator = (Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
      	
      	ReadFile();
        init();        
        initGuanggao();
        
        
        mShakeListener = new ShakeListener(this);
        
        mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				//Toast.makeText(getApplicationContext(), "��Ǹ����ʱû���ҵ���ͬһʱ��ҡһҡ���ˡ�\n����һ�ΰɣ�", Toast.LENGTH_SHORT).show();
//				startAnim();  //��ʼ ҡһҡ���ƶ���
				mShakeListener.stop();
				startVibrato(); //��ʼ ��
				if(state.getVoice()==1){
					mPlayer.start();
				}
				
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						mVibrator.cancel();
						dialogFalg = true;
						showAlertDialog(null, getRandom(), "��������������ˣ��", "ȷ��",null,new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub								
								dialog.dismiss();
								dialogFalg = false;
								mShakeListener.start();//���¿�ʼ����
								if(state.getVoice()==1){
									mPlayer.pause();//��ͣ
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
				"7c5a9a634ee7a025", false);//false��ʾ���ǲ���ģʽ
		// ����ص������´��룬����SDKӦ��������������SDK����һЩ��ʼ���������ýӿ������SDK�ĳ�ʼ���ӿ�֮����á�
		OffersManager.getInstance(this).onAppLaunch();
		// ������׻��ֹ��ǽ�Ƿ����óɹ���trueΪ�ɹ�
		// ʹ��sdk�Դ��Ļ����йܣ����Զ�������˻������ü��:
//		OffersManager off = OffersManager.getInstance(this);
//		if (off.checkOffersAdConfig())
//			Log.e("youmi", "���Գɹ�");
//		else
//			Log.e("youmi", "����ʧ��");
		// ��ʹ��sdk�Դ��Ļ����йܣ�ʹ���Զ�������˻������ü�飺
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
		//���˵ĸ��·���
		//UmengUpdateAgent.setUpdateOnlyWifi(false);
		 UmengUpdateAgent.update(this);

	}
    
	public void startVibrato(){		//������
		mVibrator.vibrate( new long[]{500,200,500,200}, -1); //��һ�����������ǽ������飬 �ڶ����������ظ�������-1Ϊ���ظ�����-1���մ�pattern��ָ���±꿪ʼ�ظ�
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
//    	mPlayer.setLooping(true);//���ÿ����ظ�����

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
        
        //�״�ִ�е���.db�ļ�
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
        
        c = getCursor();
        c.moveToFirst();
        
        content.setText(c.getString(0));
        tip.setText(StateInstance.getTip());
//        page.setText(1+"/"+c.getCount());
        if(GradeManager.getInstance(this).query() < limitGrade){
        	grade.setText("��ǰ����:"+GradeManager.getInstance(this).query());
        }else{
        	grade.setText("��ǰ����:vip��Ա���������ʹ��");
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
					Toast.makeText(MainActivity.this, "�Ѿ��ǵ�һ����¼��", Toast.LENGTH_SHORT).show();
					break;
				}
					
				c.moveToPrevious();
				content.setText(c.getString(0));
//				page.setText(c.getPosition()+1+"/"+c.getCount());
				break;
			case R.id.next:
				if(c.isLast()){
					Toast.makeText(MainActivity.this, "�Ѿ������һ����¼��", Toast.LENGTH_SHORT).show();
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
	        	
	        	
				showAlertDialog(layout, null, "����", "ȷ��", "ȡ��", onclick);
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
				//���ȷ�������ѡ�����ؿ�ζ�Ļ�������Ҫ����һ�ٸ����ֺ�����ʹ�á�
				if(state.getRank() == 2 && GradeManager.getInstance(MainActivity.this).query()<limitGrade){
					CommonUtil.showInfoDialog(MainActivity.this,"���ˣ����ִﵽ100��Ϊvip�󣬲����������ʹ���˼���","�����װ�������","ȡ��","���ϻ�û���",MainActivity.this);
					//���¸ı�rank��״̬
					state.setRank(read("rank"));//���¶�ȡ���е�ֵ��state��������״̬�ǿ��Ըı�ġ�
					break;//ֱ���˳�
				}									
	        	Log.i(Tag,"voice:"+state.getVoice());       		        
	        	saveFile();
	       	    //�Ƴ�layout����ֹ��״̬�쳣�Ĵ���
	        	LinearLayout view = (LinearLayout) layout.getParent();
	        	view.removeAllViews();
	        	
	        	c = getCursor();//reset the cursor.
	            c.moveToFirst();
	            content.setText(c.getString(0));
	            
	            tip.setText(StateInstance.getTip());
	        	
				break;
			case Dialog.BUTTON_NEGATIVE:
				//����state��״̬
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
		
    	Log.i(Tag, "Spinner��"+arg2+"");
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
    	//super.onBackPressed();�����finish()�¼�
    	mShakeListener.start();
    	mPlayer.pause();
    	if(mShakeListener.flag ==0){
    		mShakeListener.start();}//���dialog�Ǵ򿪵�״̬������������shake
        else{
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					MainActivity.this);
			alertDialog.setTitle("�˳�");
			alertDialog.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							finish();

						}
					});
			alertDialog.setNegativeButton("ȡ��",
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
			//���ϻ�û���
			notitycation();
			dialog.dismiss();
			break;			
		}
		
	}
	
	
	public void notitycation(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		.setContentTitle("���Ļ���ð��")
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