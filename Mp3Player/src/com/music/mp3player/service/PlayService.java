package com.music.mp3player.service;

import java.io.File;
import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.music.R;
import com.music.constant.MusicContant;
import com.music.constant.MusicPlayer;
import com.music.factory.HttpApiFactory;
import com.music.factory.PlayModeFactory;
import com.music.factory.model.http.Mp3InfoHttpApi;
import com.music.factory.model.playmode.AbstractPlayMode;
import com.music.factory.model.playmode.CyclePlayMode;
import com.music.factory.model.playmode.RandomPlayMode;
import com.music.factory.model.playmode.SequencePlayMode;
import com.music.factory.model.playmode.SinglePlayMode;
import com.music.lyric.LyricLoadThread;
import com.music.mp3player.MainActivity;
import com.music.mp3player.Music;
import com.music.mp3player.broadcast.LoadLyricBroadcastReceiver;
import com.music.notification.TrayNotification;
import com.music.seekbar.PlayTime;
import com.music.utils.FileUtils;
import com.music.utils.Network;

public class PlayService extends Service{
	
	private static final String TAG = "PlayService";
	
	private boolean isPlaying = false;
	private boolean isPause = false;
	private int currentIndex = 0;
	
	
	private PlayTime playTime = null;
	private LyricLoadThread lyricLoadThread = null;	
	private Music mMusic = null;
	
	PlayModeReceiver mModeReceiver;
	
	ArrayList<Music> mMusics = null;
	
	PlayModeFactory playModeFactory;	
	
	int modeCode = MusicContant.PlayMode.CYCLE;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
		
	@Override
	public void onDestroy() {				
		if(MusicPlayer.getPlayer() != null) {
			playTime.stopCountTime();
			MainActivity.getLyricView().stopRefreshLyric();
			MusicPlayer.getPlayer().stop();
			MusicPlayer.getPlayer().release();
		}	
		if (mModeReceiver != null) {
			unregisterReceiver(mModeReceiver);
		}
	}
	
	class PlayModeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			modeCode = intent.getIntExtra(MusicContant.PLAY_MODE, MusicContant.PlayMode.CYCLE);
			Log.i(TAG, modeCode + "");
			switch (modeCode) {
				case MusicContant.PlayMode.CYCLE :
					setPlayModeFactory(CyclePlayMode.factory);
					break;
					
				case MusicContant.PlayMode.SINGLE :
					setPlayModeFactory(SinglePlayMode.factory);
					break;	
					
				case MusicContant.PlayMode.SEQUENCE :
					setPlayModeFactory(SequencePlayMode.factory);
					break;
					
				case MusicContant.PlayMode.RANDOM :
					setPlayModeFactory(RandomPlayMode.factory);
					break;	
			}
		}	
	}
	
	@Override
	public void onCreate() {
		super.onCreate();	
		setPlayModeFactory(SequencePlayMode.factory);
				
		mModeReceiver = new PlayModeReceiver();
		registerReceiver(mModeReceiver, new IntentFilter(MusicContant.PLAY_MODE_ACTION));
	}

	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int stateCode = intent.getIntExtra(MusicContant.PLAY_STATE, MusicContant.PlayState.INIT);	
		if (stateCode == MusicContant.PlayState.INIT) { //��ʼ��PlayService,ֻΪ�˵���onCreate()����
			return super.onStartCommand(intent, flags, startId);
		}
		ArrayList<Music> musics = (ArrayList<Music>) intent.getSerializableExtra(MusicContant.MUSICS);	
		if(mMusics == null || mMusics.size() < 1) { 
			if(musics == null) //����û�����musics
				return super.onStartCommand(intent, flags, startId);
			mMusics = musics; //���musics				
		} else {
			switch (intent.getFlags()) {
				case MusicContant.MusicList.UPDATE_MUSICS: //����mMusics
					mMusics = musics;
					break;
				case MusicContant.MusicList.INSERT_MUSICS: //��mMusics����׷��musics	
					mMusics.addAll(musics);	
					break;
				default : break; //û��ֱ������
			}
		}		
		exectue(intent, stateCode);
		return super.onStartCommand(intent, flags, startId);
	}

	/**���ݲ�ͬ��stateCodeִ����Ӧ�Ĳ���*/
	private void exectue(Intent intent, int stateCode) {
		switch (stateCode) {
			case MusicContant.PlayState.PLAY :
				currentIndex = intent.getIntExtra(MusicContant.CURRENT_MUSIC_INDEX, 0);
				mMusic = mMusics.get(currentIndex);
				play(mMusic);
				break;
			case MusicContant.PlayState.PREV :
				currentIndex = getPlayMode().prevIndex(currentIndex, mMusics.size());
				mMusic = mMusics.get(currentIndex);
				prev(mMusic);
				break;
			case MusicContant.PlayState.NEXT :
				currentIndex = getPlayMode().nextIndex(currentIndex, mMusics.size());
				mMusic = mMusics.get(currentIndex);
				next(mMusic);
				break;
		}		
	}
	
	private AbstractPlayMode getPlayMode() {
		return playModeFactory.createPlayMode();
	}
	
	private void setPlayModeFactory(PlayModeFactory playModeFactory) {
		this.playModeFactory = playModeFactory;
	}
		
	private void play(Music mMusic) {
		if(!isPlaying && !isPause || MusicPlayer.isFirstPlaying) {							
			if(MusicPlayer.getPlayer() != null) {
				setStopState();
			}
			if(mMusic.getMp3URL() != null && !mMusic.getMp3URL().contains("http")) {
				File mp3File = new File(mMusic.getMp3URL());
				if(mp3File.exists() && mp3File.length() > 0) {
					new ConnectAsyncTask().execute("����mp3�ļ�");
				} else {
					mp3File.delete(); //ɾ�����ϸ��MP3
					Toast.makeText(this, mMusic.getMp3URL() + "���Ϸ�", Toast.LENGTH_SHORT).show();
				}
			} else {
				if(Network.isAccessNetwork(this)) {
					new ConnectAsyncTask().execute("����mp3�ļ�");
				} else {
					Toast.makeText(this, "��ǰ��û����", Toast.LENGTH_SHORT).show();
				}			
			}					
		} 
		else if(isPlaying) {				
			setPauseState();//������ͣ״̬					
		} 
		else if(isPause) {			
			setPlayState();	//���ü�������״̬									
		}
	}
	
	private void prev(Music mMusic) {		
		if(MusicPlayer.getPlayer() != null)
			setStopState();
		play(mMusic);		
	}
	
	private void next(Music mMusic) {		
		if(MusicPlayer.getPlayer() != null)
			setStopState();
		play(mMusic);		
	}
	
	private void autoPlayNext() { //�Զ�������һ�׸�
		Log.i(TAG, "currentIndex1 : " + currentIndex);
		currentIndex = getPlayMode().nextIndex(currentIndex, mMusics.size());
		Log.i(TAG, currentIndex + "-" + MusicContant.PlayMode.SEQUENCE);
		if (currentIndex == 0 && modeCode == MusicContant.PlayMode.SEQUENCE) { 
			Log.i(TAG, currentIndex + "--" + MusicContant.PlayMode.SEQUENCE);
			currentIndex = mMusics.size() - 1; //�б��Ŵ����Զ����������һ�׸���ʱֹͣ
			Log.i(TAG, "currentIndex2 : " + currentIndex);
			setPauseState();
		} else {
			Log.i(TAG, currentIndex + "---" + MusicContant.PlayMode.SEQUENCE);
			setStopState();
			mMusic = mMusics.get(currentIndex);
			play(mMusic);
		}	
	}
	
	private void setPlayState() {				
		if(MusicPlayer.getPlayer() != null) {			
			MusicPlayer.getPlayer().start();		
			playTime.beginCountTime();
			if(MainActivity.getLyricView().isPauseRefreshLyric) {
				MainActivity.getLyricView().beginRefreshLyric();
			}
			isPlaying = true;
			isPause = false;
			setPauseButton();
			setMp3Time();
		}
	}
	
	private void setPauseState() {
		if(MusicPlayer.getPlayer() != null) {
			playTime.pauseCountTime();
			MainActivity.getLyricView().pauseRefreshLyric();
			MusicPlayer.getPlayer().pause();
			isPlaying = false;
			isPause = true;

			//ѡ����ͣ��ťͼƬ
			setPlayButton();
		}		
	}
	
	private void setStopState() {
		if(MusicPlayer.getPlayer() != null) {
			playTime.stopCountTime();
			MainActivity.getLyricView().stopRefreshLyric();
			MusicPlayer.getPlayer().stop();
			MusicPlayer.getPlayer().release();
			MusicPlayer.setPlayer(null);
			isPlaying = false;
			isPause = false;
			setPlayButton();			
		}
	}
	
	/**�����ĳ���ʱ��*/
	private void setMp3Time() {
		Intent intent = new Intent();
		intent.setAction(MusicContant.UPDATE_UI_ACTION);
		intent.setFlags(0x14);
		intent.putExtra("duration", FileUtils.IntTimeConvert(MusicPlayer.getPlayer().getDuration()));
		sendBroadcast(intent);
	}
	
	private void setPlayButton() {
		Intent intent = new Intent();
		intent.setAction(MusicContant.UPDATE_UI_ACTION);
		intent.setFlags(0x11);
		sendBroadcast(intent);	
	}
	
	private void setPauseButton() {
		Intent intent = new Intent();
		intent.setAction(MusicContant.UPDATE_UI_ACTION);
		intent.setFlags(0x12);
		sendBroadcast(intent);		
	}
	
	/**MediaPlayer�������������ж�һ�׸��Ƿ��Ѳ�����ϣ��Ա㲥����һ�׸��� */
	class MediaPlayerCompletionListener implements OnCompletionListener {
		public void onCompletion(MediaPlayer arg0) {
			autoPlayNext();
		}	
	}
	
	class MediaPlayerErrorListenner implements OnErrorListener {
		public boolean onError(MediaPlayer mp, int what, int extra) {
			autoPlayNext();
			return true;
		}
	}
	
	private class ConnectAsyncTask extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {					
			loadMusic();					
			initPlayer();	
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);							
			updateUI();
			loadLyric();		
			updateApplicationTrayTitle();			
			initTime();
			setPlayState();	//���ò���״̬			
		}
										
		/**����MP3*/
		private void loadMusic() {					
			if(MusicPlayer.getPlayer() != null) {
				setStopState();			
			}
			if(mMusic.getMp3URL() == null && mMusic.getMp3IdCode() != null) {
				HttpApiFactory factory = Mp3InfoHttpApi.factory;
				Bundle bundle = new Bundle();
				bundle.putString("mp3Id", mMusic.getMp3IdCode());
				factory.getHttpApi().execute(bundle, mMusic);//��ȡMP3��ַ
			}
			Uri mp3Uri = Uri.parse(mMusic.getMp3URL());
			System.out.println(mMusic.getMp3URL());
			MusicPlayer.setPlayer(MediaPlayer.create(PlayService.this, mp3Uri));													
		}
		
		/**����MediaPlayer�Ĳ��Ų���*/
		private void initPlayer() {
			try {
				if(MusicPlayer.getPlayer() != null) {
					MusicPlayer.isFirstPlaying = false;
					MusicPlayer.getPlayer().setLooping(false);
					MusicPlayer.getPlayer().setOnCompletionListener(new MediaPlayerCompletionListener());
					MusicPlayer.getPlayer().setOnErrorListener(new MediaPlayerErrorListenner());
				} 				
			} catch(Exception e) {				
				System.err.println("mp3��ʼ������" + e.getMessage());
			}	
		}
		
		private void updateUI() {
			Intent intent = new Intent();
			intent.setAction(MusicContant.UPDATE_UI_ACTION);
			intent.setFlags(0x13);
			intent.putExtra("musicSimpleName", mMusic.getMp3SimpleName());
			intent.putExtra("singerName", mMusic.getSingerName());
			intent.putExtra("singerBg", "null");
			intent.putExtra(MusicContant.MUSIC, mMusics.get(currentIndex));
			sendBroadcast(intent);
		} 
			
		/**���ظ��*/
		private void loadLyric() {																			
			MainActivity.getLyricView().setLyricInfo(null, 0);
			MainActivity.getLyricView().setCurrentMp3Info(mMusic);
			lyricLoadThread = new LyricLoadThread(mMusic, PlayService.this);
			lyricLoadThread.start();		
			LoadLyricBroadcastReceiver lyricReceiver = new LoadLyricBroadcastReceiver(lyricLoadThread);
			registerReceiver(lyricReceiver, lyricReceiver.getIntentFilter());
		}
		
		/**��ʾ��������������*/
		private void updateApplicationTrayTitle() {
			StringBuilder title = new StringBuilder().append(mMusic.getSingerName())
					.append(" - ").append(mMusic.getMp3SimpleName());
			TrayNotification.addNotification(PlayService.this, R.drawable.information_icon,
					title.toString());
		}
		
		/**��ʼ����ʱ�߳�*/
		private void initTime() {
			if(MusicPlayer.getPlayer() != null)
				playTime = new PlayTime(PlayService.this, MusicPlayer.getPlayer());
		}
	}
}
