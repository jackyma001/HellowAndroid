package com.jacky;

import java.util.Calendar;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.PendingIntent;


import android.content.DialogInterface;
import android.content.Intent;

import android.database.DatabaseUtils;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


public class Plan extends Activity {
	private SQLiteDatabase mSQLiteDatabase = null;
	private EditText txtWordPerDay = null;
	private DatePicker startDate = null;
	private static final int DIALOG_YES_NO_MESSAGE = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan);
        
        Button btnGenerate = (Button) findViewById(R.id.btnGenerate);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        txtWordPerDay = (EditText) findViewById(R.id.txtWordPerDay);
        startDate = (DatePicker) findViewById(R.id.dpStartDate);     
        mSQLiteDatabase = new DatabaseManager(this).openDatabase();
   
        btnGenerate.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		try
        		{
	        		
        			int wordPerDays = Integer.parseInt(txtWordPerDay.getText().toString());
	                Calendar calendar=Calendar.getInstance();
	                calendar.set(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
	                for(int i = 2;i <=getListNumber();i++)
	                {
	                	calendar.add(Calendar.HOUR, (i-1)*24);	
	                	addNotification(i,0,wordPerDays,"1",calendar.getTimeInMillis());
	                }
	                showDialog(DIALOG_YES_NO_MESSAGE);
	                
        		}
        		catch(Exception e)
        		{
        			Log.e("jacky", e.getMessage());	
        		}	
    			
        		}
        	}
        );
        
        btnClear.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		for(int i=1;i<=getListNumber();i++)
        		{
        			Intent intent =new Intent(Plan.this, PlanExcute.class);
            		PendingIntent sender = PendingIntent.getBroadcast(Plan.this, i, intent,0);
            		AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
            		alarm.cancel(sender);
        		}
        		Toast.makeText(getApplicationContext(), "所有计划提醒已取消。", Toast.LENGTH_SHORT).show();
        	}
        });
	}
	
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DIALOG_YES_NO_MESSAGE:
	            return new AlertDialog.Builder(this)
	                .setTitle("哥要开始学List1么？")
	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	int wordPerDays = Integer.parseInt(txtWordPerDay.getText().toString());
	                    	//学习List1的时机到了
	    	                Intent intent =new Intent(Plan.this, Dic.class);
	    	        		Bundle bundle = new Bundle(); 
	    	        		bundle.putInt("LIST_ID", 1);//从头开始
	    	        		bundle.putString("ALERT_MESSAGE", "1");
	    	        		bundle.putInt("WORD_PER_DAY", wordPerDays);
	    	        		intent.putExtras(bundle);
	    	        		startActivity(intent);
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
	
	private void addNotification(int listid,int reviewTimes,int wordPerDay,String alertMessage,long timeMillis){
		Intent intent =new Intent(Plan.this, PlanExcute.class);
		Bundle bundle = new Bundle(); 
		bundle.putInt("LIST_ID", listid);
		bundle.putString("ALERT_MESSAGE", alertMessage);
		bundle.putInt("WORD_PER_DAY", wordPerDay);
		bundle.putInt("REVIEW_TIMES", reviewTimes);
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//这行代码会解决此问题 
		//intent.addFlags(Intent.FILL_IN_DATA);
		intent.putExtras(bundle);
		//intent.setAction(Integer.toString(listid)+alertMessage);
		
		PendingIntent sender = PendingIntent.getBroadcast(Plan.this, listid, intent,0);
		AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
		alarm.set(AlarmManager.RTC_WAKEUP, timeMillis, sender);
    }
	
	//获取列表数
	private int getListNumber()
	{
		long totalWords= DatabaseUtils.queryNumEntries(mSQLiteDatabase, "dic");       
        int wordPerDays = Integer.parseInt(txtWordPerDay.getText().toString()); 
        int listNums =  (int) Math.ceil((double)totalWords/wordPerDays);
        return listNums;
	}
     
}
