package yuan.factory.model;

/**
 * ��ȡǰ��һ�׸��λ��
 * @author Administrator
 *
 */
public abstract class AbstractPlayMode {

	/**��ȡǰһ�׸��λ��*/
	public abstract int preSongIndex(int index, int size);
	/**��ȡ��һ�׸��λ��*/
	public abstract int nextSongIndex(int index, int size);
}
