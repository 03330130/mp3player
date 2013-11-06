package com.music.constant;

public interface MusicContant {
	
	public final static class PlayState {
		/**����ʼ����*/
		public final static int INIT = 0x11;
		
		/**����ʼ����*/
		public final static int PLAY = 0x12;
		
		/**������ͣ����*/
		public final static int PAUSE = 0x13;
		
		/**����ֹͣ����*/
		public final static int STOP = 0x14;
		
		/**������ǰһ�׸���*/
		public final static int PREV = 0x15;
		
		/**�����ź�һ�׸���*/
		public final static int NEXT = 0x16;
	}
	
	public final static class PlayMode {	
	
		/**����ѭ������*/
		public final static int CYCLE = 0x11;
		
		/**�����б���*/
		public final static int SEQUENCE = 0x12;	
		
		/**�����������*/
		public final static int SINGLE = 0x13;
		
		/**������ѭ������*/
		public final static int RANDOM = 0x14;
	}
	
	public final static class MusicList {
		public final static int INSERT_MUSICS = 0x11;
		public final static int UPDATE_MUSICS = 0x12;
	}
	
	public final static String MUSIC = "com.music.MUSIC";
	
	public final static String MUSICS = "com.music.MUSICS";
	
	
	public final static String PLAY_STATE = "com.music.PLAY_STATE";
	
	
	public final static String PLAY_MODE = "com.music.PLAY_MODE";
	
	
	public final static String CURRENT_MUSIC_INDEX = "com.music.CURRENT_MUSIC_INDEX";
	
	/***/
	public final static String LRC_MESSAGE_ACTION = "com.music.LRC_MESSAGE.ACTION";
	
	/***/
	public final static String UNDATE_LIST_ACTION = "com.music.REMOTE_LIST_ACTION";
	
	/***/
	public final static String SEARCH_KEY_WORD_ACTION = "com.music.SEARCH_KEYWORD_ACTION";
	
	/**����ģʽ*/
	public final static String PLAY_MODE_ACTION = "com.music.PLAYMODE_ACTION";
	
	/**����ͼƬ������ϱ�־*/
	public final static String LOAD_SINGER_IMAGE_OVER = "com.music.LOAD_SINGER_IAMGE_OVER";
	
	/**��ʼ������*/
	public final static String LOAD_LYRIC_OVER = "com.music.LOAD_LYRIC_OVER";
	
	/**�������ֱ�־*/
	public final static String PLAY_MUSIC_ACTION = "com.music.PLAY_MUSIC_ACTION";
	
	/**���Ž���Ԫ�ظ���*/
	public final static String UPDATE_UI_ACTION = "com.music.UPDATE_UI_ACTION";
}
