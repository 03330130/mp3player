package com.music.lyric;

import java.util.Vector;

import com.music.constant.MusicContant;
import com.music.constant.MusicPlayer;
import com.music.mp3player.MainActivity;
import com.music.mp3player.Music;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class LyricUpdate {
	
	/**�ж��Ƿ���ڸ�ʣ����ڼ�������ʸ����̣߳���֮��Ȼ*/
	public boolean isLyric = false;
	
	private Handler lyricUpdateHandler = new Handler();;
	private LyricUpdateThread lyricUpdateThread = new LyricUpdateThread();
	private Music mp3Info = null;
	private Vector<LyricInfo> lyricInfos = null;
	private Context context = null;
		
	public LyricUpdate(Music mp3Info, Context context, Vector<LyricInfo> lyricInfos) {
		this.mp3Info = mp3Info;
		this.context = context;
		this.lyricInfos = lyricInfos;
	}

	/**
	 * ���¸��֮ǰ�����ж��Ƿ���ڸ��
	 */
	public void prepare(){			
		if(lyricInfos != null && lyricInfos.size() > 0) {	
			this.setLyric(false);
			MainActivity.getLyricView().setText(mp3Info.getMp3SimpleName());			
		} else {
			this.setLyric(false);
			MainActivity.getLyricView().setText("�Ҳ������");
		}		
	}


	/** ���¸�ʵ��߳�*/	 
	private class LyricUpdateThread implements Runnable{		
		public void run() {			
			int currentPosition = MusicPlayer.getPlayer().getCurrentPosition();					
			for (int i = 1; i < lyricInfos.size(); i++) {
				if (currentPosition < lyricInfos.get(i).getTime())
					if (currentPosition >= lyricInfos.get(i - 1).getTime()) {
						sendLyricBroadcast(lyricInfos.get(i - 1).getLyric(), context);//���͹㲥�Ը��¸��	
					}
			}							
			lyricUpdateHandler.postDelayed(lyricUpdateThread, 2);
		}
				
		/**���͹㲥�Ը��¸��*/ 
		private void sendLyricBroadcast(String lyricMsg, Context context) {
			Intent intent = new Intent();
			intent.setAction(MusicContant.LRC_MESSAGE_ACTION);
			intent.putExtra("lyricMsg", lyricMsg);
			context.sendBroadcast(intent);
		}
	}
	
	public void postDelayed(LyricUpdateThread lyricUpdateThread, long delayMillis) {
		if(isLyric())
			lyricUpdateHandler.postDelayed(lyricUpdateThread, 2);
	}
	
	public void removeCallbacks(LyricUpdateThread lyricUpdateThread) {
		if(isLyric())
			lyricUpdateHandler.removeCallbacks(lyricUpdateThread);
	}
	
	public LyricUpdateThread getUpdateLrcThread() {
		return lyricUpdateThread;
	}

	public boolean isLyric() {
		return isLyric;
	}

	public void setLyric(boolean isLyric) {
		this.isLyric = isLyric;
	}	
}
