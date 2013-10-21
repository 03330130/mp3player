package com.music.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class DownloadNotification {
	/**
	 * ����ʱ֪ͨ�û��������
	 * @param downloadMsg ���ط��ص���Ϣ
	 */
	public static void showNotification(Context context, String downloadMsg) {		
		
		NotificationManager notificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
	
		Notification notification=new Notification(android.R.drawable.stat_notify_chat, 
				downloadMsg, System.currentTimeMillis());

		Intent intent = new Intent();
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
				
		notification.setLatestEventInfo(context, "��������", "����״̬ :" + downloadMsg, pi);
		notificationManager.notify(0, notification);
	}
}
