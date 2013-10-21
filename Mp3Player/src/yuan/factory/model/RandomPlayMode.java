package yuan.factory.model;

import yuan.factory.PlayModeFactory;

/**
 * �����ȡǰ��һ�׸��λ��
 * @author Administrator
 */
public class RandomPlayMode extends AbstractPlayMode {
	
	private RandomPlayMode() {
	}

	@Override
	public int preSongIndex(int index, int size) {
		//Math.random()*(b-a)+a��a - b֮�����ѡȡһ����
		index = (int) (Math.random()*(size - 1));
		return index;
	}

	@Override
	public int nextSongIndex(int index, int size) {
		index = (int) (Math.random()*(size - 1));
		return index;
	}
	
	public static PlayModeFactory factory = new PlayModeFactory() {	
		@Override
		public AbstractPlayMode createPlayMode() {			
			return new RandomPlayMode();
		}
	};
}
