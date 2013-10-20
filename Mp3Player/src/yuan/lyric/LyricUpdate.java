package yuan.lyric;

import java.util.Vector;

import yuan.constant.AppConstant;
import yuan.constant.MusicPlayer;
import yuan.factory.model.Mp3Info;
import yuan.mp3player.MainActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class LyricUpdate {
	
	/**判断是否存在歌词，存在即启动歌词更新线程，反之亦然*/
	public boolean isLyric = false;
	
	private Handler lyricUpdateHandler = new Handler();;
	private LyricUpdateThread lyricUpdateThread = new LyricUpdateThread();
	private Mp3Info mp3Info = null;
	private Vector<LyricInfo> lyricInfos = null;
	private Context context = null;
		
	public LyricUpdate(Mp3Info mp3Info, Context context, Vector<LyricInfo> lyricInfos) {
		this.mp3Info = mp3Info;
		this.context = context;
		this.lyricInfos = lyricInfos;
	}

	/**
	 * 更新歌词之前的先判断是否存在歌词
	 */
	public void prepare(){			
		if(lyricInfos != null && lyricInfos.size() > 0) {	
			this.setLyric(false);
			MainActivity.getLyricView().setText(mp3Info.getMp3SimpleName());			
		} else {
			this.setLyric(false);
			MainActivity.getLyricView().setText("找不到歌词");
		}		
	}


	/** 更新歌词的线程*/	 
	private class LyricUpdateThread implements Runnable{		
		public void run() {			
			int currentPosition = MusicPlayer.getPlayer().getCurrentPosition();					
			for (int i = 1; i < lyricInfos.size(); i++) {
				if (currentPosition < lyricInfos.get(i).getTime())
					if (currentPosition >= lyricInfos.get(i - 1).getTime()) {
						sendLyricBroadcast(lyricInfos.get(i - 1).getLyric(), context);//发送广播以更新歌词	
					}
			}							
			lyricUpdateHandler.postDelayed(lyricUpdateThread, 2);
		}
				
		/**发送广播以更新歌词*/ 
		private void sendLyricBroadcast(String lyricMsg, Context context) {
			Intent intent = new Intent();
			intent.setAction(AppConstant.LRC_MESSAGE_ACTION);
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
