package yuan.factory.model;

/**
 * �����ȡǰ��һ�׸��λ��
 * @author Administrator
 *
 */
public class RandomPlayMode extends AbstractPlayMode {

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

}
