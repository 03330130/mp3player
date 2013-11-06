package com.music.factory.model.playmode;

import com.music.factory.PlayModeFactory;

/**
 * �����ȡǰ��һ�׸��λ��
 * @author Administrator
 */
public class RandomPlayMode extends AbstractPlayMode {
	
	private RandomPlayMode() {
	}

	@Override
	public int prevIndex(int index, int size) {
		//Math.random()*(b-a)+a��a - b֮�����ѡȡһ����
		return (int) (Math.random()*(size - 1));
	}

	@Override
	public int nextIndex(int index, int size) {
		return (int) (Math.random()*(size - 1));
	}
	
	public static PlayModeFactory factory = new PlayModeFactory() {	
		@Override
		public AbstractPlayMode createPlayMode() {			
			return new RandomPlayMode();
		}
	};
}
