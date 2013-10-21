package com.music.mp3player;

import java.util.Timer;
import java.util.TimerTask;

import com.music.notification.TrayNotification;
import com.music.utils.FileUtils;

import com.music.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		getWindow().setBackgroundDrawableResource(R.drawable.splash);
		showTray();
		createAppFolder();
		startApp();
		
	}
	
	/**1.5��������֮������������*/
	private void startApp() {
		new Timer().schedule(new TimerTask() {			
			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(intent);							
				finish();
			}
		}, 500);
	}
	
	/**�������MP3��lrc��ͼƬ���ļ���*/
	private void createAppFolder() {
		FileUtils.createDefaultDir();	
	}
	
	/**��ʾ����������*/
	private void showTray() {
		TrayNotification.addNotification(SplashScreen.this, R.drawable.information_icon, null);
	}
}
