package yuan.constant;

import android.media.MediaPlayer;

public class MusicPlayer{
	
	private static  MediaPlayer player = null;
	/**��־�ǵ�һ�β�������*/
	public static boolean isFirstPlaying = false;
	
	public static MediaPlayer getPlayer() {
		return player;
	}
	
	public static void setPlayer(MediaPlayer player) {		
		
		MusicPlayer.player = player;
	}		
}


