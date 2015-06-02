package com.jacky;

import java.util.Calendar;
import java.util.Locale;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;

import android.app.Dialog;
import android.app.PendingIntent;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;


public class Dic extends Activity implements OnInitListener {
	public static final String PREFS_NAME = "MyPrefsFile";
	private static final int DIALOG_YES_NO_MESSAGE = 1;
	private SQLiteDatabase mSQLiteDatabase = null;
	private Cursor cur = null; 
	private static final int REQ_TTS_STATUS_CHECK = 0;  
	private static final String TAG = "TTS Demo";  
	private TextToSpeech mTts;
	private ImageButton btnSpeak = null;  
	private TextView txtWord = null;
	private Boolean mIsPlanMode = false;
	private int list_id = 1;
	private int word_per_day = 50;
	private int review_times = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.dic);
        Button btnFalse = (Button) findViewById(R.id.bt_false);
        Button btnPass = (Button) findViewById(R.id.bt_true);
        btnSpeak = (ImageButton) findViewById(R.id.img_speak);
        txtWord = (TextView) findViewById(R.id.txt_word);
        btnFalse.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		if(cur.moveToNext()){
        			//showWord();
        			showDialog(DIALOG_YES_NO_MESSAGE);		
        		}
        		else{//最后一个了大哥
        			
        			showDialog(DIALOG_YES_NO_MESSAGE);		
        		}
        	}
        });
        
        btnPass.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		if(cur.moveToNext()){
        			showWord();
        		}
        		else{//最后一个了大哥
        			Calendar calendar=Calendar.getInstance();
	                calendar.setTimeInMillis(System.currentTimeMillis());
	                int nextReviewTimes = review_times + 1;
	                calendar.add(Calendar.SECOND, ReviewSchedule.GetNextReviewDate(nextReviewTimes));
	                addNotification(list_id,nextReviewTimes,word_per_day,"第"+Integer.toString(nextReviewTimes)+"次了哥",calendar.getTimeInMillis());
        		}
        	}
        });
        
        btnSpeak.setOnClickListener(new ImageButton.OnClickListener(){
        	
			public void onClick(View v) {
				mTts.speak(txtWord.getText().toString(), TextToSpeech.QUEUE_ADD, null);
			}
        });
        
        mSQLiteDatabase = new DatabaseManager(this).openDatabase();
\        
        
        Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		if (bundle != null){
			mIsPlanMode = true;
			list_id = bundle.getInt("LIST_ID"); 
	        word_per_day = bundle.getInt("WORD_PER_DAY");
	        review_times = bundle.getInt("REVIEW_TIMES"); 
        	int startPosition = (list_id-1)*word_per_day;
        	int endPosition = startPosition +word_per_day;
        	String sql = "select word_key,pron,trans,list_id,example from dic order by word_key limit "+startPosition+","+endPosition;
            cur = mSQLiteDatabase.rawQuery(sql, null);
        	cur.moveToFirst();
        }
        else{
        	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            int lastWordPosition = settings.getInt("lastWordPosition",0);
            String sql = "select word_key,pron,trans,list_id,example from dic order by word_key";
            cur = mSQLiteDatabase.rawQuery(sql, null);
            cur.moveToPosition(lastWordPosition);
 
        ''}	
        
        showWord();
        Intent checkIntent = new Intent();  
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);  
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);  
        
    }
    
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if(requestCode == REQ_TTS_STATUS_CHECK)  
        {  
            switch (resultCode) {  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:  
                //这个返回结果表明TTS Engine可以用  
            {  
                mTts = new TextToSpeech(this, this);  
                Log.v(TAG, "TTS Engine is installed!");          
            }  
               break;  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:  
                //需要的语音数据已损坏  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:  
                //缺少需要语言的语音数据  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:  
                //缺少需要语言的发音数据  
            {  
                //这三种情况都表明数据有错,重新下载安装需要的数据  
                Log.v(TAG, "Need language stuff:"+resultCode);  
                Intent dataIntent = new Intent();  
                dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);  
                startActivity(dataIntent);     
            }  
                break;  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:  
                //检查失败  
            default:  
                Log.v(TAG, "Got a failure. TTS apparently not available");  
                break;  
            }  
        }  
        else  
        {  
            //其他Intent返回的结果  
        }  
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    
    	MenuInflater inflater = getMenuInflater();    
    	inflater.inflate(R.menu.hello_menu, menu);    
    	return true;
    }
    
    private void showWord(){
    	TextView txtWord =(TextView) findViewById(R.id.txt_word);
        TextView txtPron =(TextView) findViewById(R.id.txt_pron);
        TextView txtDescription =(TextView) findViewById(R.id.txt_description);
        TextView txtExample = (TextView) findViewById(R.id.txt_example);
        Typeface mFace = Typeface.createFromAsset(getAssets(), "font/lingoes.ttf"); 
        txtPron.setTypeface(mFace);
        txtWord.setText(cur.getString(0));
        txtPron.setText("["+cur.getString(1)+"]");
        txtDescription.setText(cur.getString(2)); 
        txtExample.setText(cur.getString(4).replace("|", "\r\n"));
        saveStudyProgress();
    }
    
	public void onInit(int status) {
		//TTS Engine初始化完成  
	    if(status == TextToSpeech.SUCCESS)  
	    {  
	        int result = mTts.setLanguage(Locale.US);  
	        //设置发音语言  
	        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)  
	        //判断语言是否可用  
	        {  
	            Log.v(TAG, "Language is not available");  
	            btnSpeak.setEnabled(false);  
	        }  
	        else  
	        {    
	        	btnSpeak.setEnabled(true);  
	        }  
	    }  
		
	}
	
	@Override   
	protected void onStop(){
		super.onStop();
		if (!mIsPlanMode){
			saveStudyProgress();
		}
		
	}
	
	@Override 
	 protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DIALOG_YES_NO_MESSAGE:
	        	int numDays = ReviewSchedule.GetNextReviewDate(this.review_times+1)/24;
    			Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.HOUR, numDays*24);
                
    			String title= String.format("亲,下次复习List%1$,d的时间定于%2$,d天之后%3$tF,我会通知你的！",this.list_id,numDays,calendar.getTime());
	            return new AlertDialog.Builder(this)
	            	.setMessage(title)
	                .setTitle("新计划")
	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	Calendar calendar=Calendar.getInstance();
	    	                calendar.setTimeInMillis(System.currentTimeMillis());
	    	                int nextReviewTimes = review_times +1;
	    	                calendar.add(Calendar.SECOND, ReviewSchedule.GetNextReviewDate(nextReviewTimes));
	    	                addNotification(list_id,nextReviewTimes,word_per_day,"第"+Integer.toString(nextReviewTimes)+"次了哥",calendar.getTimeInMillis());   
	                    }
	                })
	                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {

	                        dialog.dismiss();
	                    }
	                })
	                .create();
	        }
	        return null;
	 }
	
	private void saveStudyProgress(){
		// We need an Editor object to make preference changes. 
		// All objects are from android.context.Context      
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);      
		SharedPreferences.Editor editor = settings.edit();      
		editor.putInt("lastWordPosition", cur.getPosition());  
		// Commit the edits!      
		editor.commit();    
	}
	
	private void addNotification(int listid,int reviewTimes,int wordPerDay,String alertMessage,long timeMillis){
		Intent intent =new Intent(Dic.this, PlanExcute.class);
		Bundle bundle = new Bundle(); 
		bundle.putInt("LIST_ID", listid);
		bundle.putString("ALERT_MESSAGE", alertMessage);
		bundle.putInt("WORD_PER_DAY", wordPerDay);
		bundle.putInt("REVIEW_TIMES", reviewTimes);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//这行代码会解决此问题 
		intent.addFlags(Intent.FILL_IN_DATA);
		intent.putExtras(bundle);
		intent.setAction(listid+alertMessage);
		
		PendingIntent sender = PendingIntent.getBroadcast(Dic.this, 0, intent,0);
		AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
		alarm.set(AlarmManager.RTC_WAKEUP, timeMillis, sender);
    }
    
}