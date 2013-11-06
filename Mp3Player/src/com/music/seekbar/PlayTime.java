package com.music.seekbar;

import com.music.constant.MusicContant;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;

/**
 * ���¸�������ʱ��͸��½�����
 * @author Administrator
 */
public class PlayTime {
	
	private Handler handler = new Handler();
	private MediaPlayer player = null;
	private TimerThread timerThread = null;
	
	Context context;	
	
	public PlayTime(Context context, MediaPlayer player) {
		this.player = player;
		this.context = context;
		this.timerThread = new TimerThread();
	}
	
	/**�������ż�ʱ���߳�,ע������̲߳���һ���µ��̣߳�����UI�����߳�*/
	private class TimerThread extends Thread {
		@Override
		public void run() {
			if (player != null) {
				int currentPosition = player.getCurrentPosition();			
				updateSeekBar(currentPosition);
				handler.post(timerThread);
			}		
		}		
	}
	
	private void updateSeekBar(int currentPosition){
		Intent intent = new Intent();
		intent.setAction(MusicContant.UPDATE_UI_ACTION);
		intent.setFlags(0x16);
		intent.putExtra("processRate", (float)currentPosition / player.getDuration());
		intent.putExtra("currentMusicTime", timeConvert(currentPosition / 1000));
		context.sendBroadcast(intent);
	}
	
	/**��ʼ��ʱ*/
	public void beginCountTime() {		
		handler.post(timerThread);
	}
	
	/**��ͣ��ʱ*/
	public void pauseCountTime() {
		stopCountTime();
	}
	
	/**ֹͣ��ʱ*/
	public void stopCountTime() {
		handler.removeCallbacks(timerThread);
	}
		
	/**����ʱ��ת��*/
	private String timeConvert(int millTime) {
		int min = millTime / 60; //����
		int sec = millTime % 60; //����			
		return getFormatTime(min) + ":" + getFormatTime(sec);
	}
	
	/**@return ���ظ����ĸ�ʽ����׼ʱ�� ����ʽ�磺00:00*/
	private String getFormatTime(int time) {
		return (time >9 ? time : "0" + time).toString();		
	}
}
