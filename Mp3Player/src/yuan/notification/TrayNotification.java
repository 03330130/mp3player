package yuan.notification;

import yuan.mp3player.MainActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class TrayNotification {	
	
	private static NotificationManager manager = null;
	private static Notification notification = null;
	
	/**��ʾ������֪ͨ*/
	public static void addNotification(Context context, int iconId, String title) { 
		manager = (NotificationManager) context.getSystemService(
        		Context.NOTIFICATION_SERVICE);       
        notification = new Notification(iconId, title, System.currentTimeMillis());                  
        
        Intent intent = new Intent(context, MainActivity.class);	
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);      
        PendingIntent contentItent = PendingIntent.getActivity(context, 0, intent, 0);
           
        notification.setLatestEventInfo(context, "��������", title, contentItent);
        manager.notify(0, notification);
    }
	
	/**ȡ��������֪ͨ*/
	public static void removeNotification(Context context) {
		NotificationManager manager = (NotificationManager) context.getSystemService(
        		Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
	}
}
