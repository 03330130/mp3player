package com.music.constant;

public interface Music {
	
	public final static class PlayState {
		/**����ʼ����*/
		public final static int PLAY = 1;
		
		/**������ͣ����*/
		public final static int PAUSE = 2;
		
		/**����ֹͣ����*/
		public final static int STOP = 3;
		
		/**������ǰһ�׸���*/
		public final static int PRE = 4;
		
		/**�����ź�һ�׸���*/
		public final static int NEXT = 5;
	}
	
	public final static class PlayMode {	
		/**����ѭ������*/
		public final static int SEQUENCE_MODE = 1;
		/**�����������*/
		public final static int RANDOM_MODE = 2;
		/**������ѭ������*/
		public final static int SINGLE_MODE = 3;
	}
	
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
