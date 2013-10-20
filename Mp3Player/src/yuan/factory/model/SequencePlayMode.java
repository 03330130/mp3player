package yuan.factory.model;

/**
 * ˳���ȡǰ��һ�׸��λ��
 * @author Administrator
 */
public class SequencePlayMode extends AbstractPlayMode {

	@Override
	public int preSongIndex(int index, int size) {
		//System.out.println("��ǰ������ID----before--->" + index);
		if(index - 1 >= 0) {
			index = index - 1;
		} else {
			index = size - 1;
		}
		//System.out.println("ǰһ�׸�����ID------->" + index);
		return index;
	}

	@Override
	public int nextSongIndex(int index, int size) {
		//System.out.println("��ǰ������ID----after--->" + index);
		if(index + 1 < size) {
			index = index + 1;
		} else {
			index = 0;
		}
		//System.out.println("ǰһ�׸�����ID------->" + index);
		return index;		
	}

}
