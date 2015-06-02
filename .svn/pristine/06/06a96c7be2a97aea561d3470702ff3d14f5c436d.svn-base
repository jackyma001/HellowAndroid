package com.jacky;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



public class PlanExcute extends BroadcastReceiver{        
	@Override        
	public void onReceive(Context context, Intent intent) { 		
		try
		{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		int icon = R.drawable.icon;
		CharSequence tickerText = "计划任务";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		//Context context = getApplicationContext();
		Bundle bundle = new Bundle();	
		bundle = intent.getExtras();
		int list_id = bundle.getInt("LIST_ID");
		String alert_message = bundle.getString("ALERT_MESSAGE");
		CharSequence contentTitle = "单词王计划";
		
		CharSequence contentText = "亲，该学List"+Integer.toString(list_id)+"了,"+alert_message+"！不能偷懒哦！";
		Intent notificationIntent = new Intent(context, Dic.class);
		
		notificationIntent.putExtras(bundle);
		notificationIntent.setAction(intent.getAction());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		mNotificationManager.notify(list_id, notification);
		}
		catch(Exception e)
		{
			Log.e("jacky", e.getMessage());
		}
		
	}    
}
