package yuan.factory.model;

import yuan.factory.PlayModeFactory;

/**
 * ˳���ȡǰ��һ�׸��λ��
 * @author Administrator
 */
public class SequencePlayMode extends AbstractPlayMode {

	private SequencePlayMode() {
	}

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

	public static PlayModeFactory factory = new PlayModeFactory() {		
		@Override
		public AbstractPlayMode createPlayMode() {
			return new SequencePlayMode();
		}
	};
}
