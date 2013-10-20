package yuan.constant;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public interface AppConstant {
	public class PlayerMsg {
		/**����ʼ����*/
		public final static int PLAY_MSG = 1;
		
		/**������ͣ����*/
		public final static int PAUSE_MSG = 2;
		
		/**����ֹͣ����*/
		public final static int STOP_MSG = 3;
		
		/**������ǰһ�׸���*/
		public final static int BEFORE_MSG = 4;
		
		/**�����ź�һ�׸���*/
		public final static int AFTER_MSG = 5;
	}
	
	public class PlayMode {	
		/**����ѭ������*/
		public final static int SEQUENCE_MODE = 1;
		/**�����������*/
		public final static int RANDOM_MODE = 2;
		/**������ѭ������*/
		public final static int SINGLE_MODE = 3;
	}
	
	public class PlayComponent {
		
		public static TextView singerName;
		public static TextView songName;		
		public static TextView countTime;	
		public static TextView mp3Time;
		
		public static TextView miniSingerName;
		public static TextView miniSongName;
		public static TextView miniCountTime;	
		public static TextView miniMp3Time;
					
		public static ImageButton playButton;
		public static ImageButton prevButton;
		public static ImageButton nextButton;
		
		public static ImageButton miniPlayBtn;
		public static ImageButton miniPrevBtn;
		public static ImageButton miniNextBtn;
		public static ImageButton stopButton;		
		
		public static ImageButton returnBack;
		public static ImageButton currentPlayList;
		public static ImageButton playMode;		
		public static ImageView singerImage;		
		public static ImageView miniSingerImage;
		public static SeekBar timeSeekBar;
		public static RelativeLayout playerBg;
	}
	
	public final static String DEBUG_TAG = "yuan.mp3player";
	
	//��������ʾ����Ϣ
	static String title = new StringBuilder().append(AppConstant.PlayComponent.songName)
			.append(" - ").append(AppConstant.PlayComponent.singerName).toString();
	
	/***/
	public final static String LRC_MESSAGE_ACTION = "yuan.mp3player.lrcmessage.action";
	/***/
	public final static String UNDATE_LIST_ACTION = "yuan.mp3player.remotelist.action";
	/***/
	public final static String SEARCH_KEY_WORD_ACTION = "yuan.mp3player.searchkeyword.action";
	/**����ģʽ*/
	public final static String PLAY_MODE_ACTION = "yuan.mp3player.playmode.action";
	/**����ͼƬ������ϱ�־*/
	public final static String LOAD_SINGER_IMAGE_OVER = "yuan.mp3player.load.singerimage.over";
	/**��ʼ������*/
	public final static String LOAD_LYRIC_OVER = "yuan.mp3player.load.lyric.over";
}
