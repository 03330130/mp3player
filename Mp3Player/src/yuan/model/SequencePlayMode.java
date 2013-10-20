package yuan.model;

import java.util.List;

/**
 * ˳���ȡǰ��һ�׸��λ��
 * @author Administrator
 */
public class SequencePlayMode extends AbstractPlayMode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getBeforePosition(int position, List<Mp3Info> mp3Infos) {
		//System.out.println("��ǰ������ID----before--->" + position);
		if(position - 1 >= 0) {
			position = position - 1;
		} else {
			position = mp3Infos.size() - 1;
		}
		//System.out.println("ǰһ�׸�����ID------->" + position);
		return position;
	}

	@Override
	public int getAfterPosition(int position, List<Mp3Info> mp3Infos) {
		//System.out.println("��ǰ������ID----after--->" + position);
		if(position + 1 < mp3Infos.size()) {
			position = position + 1;
		} else {
			position = 0;
		}
		//System.out.println("ǰһ�׸�����ID------->" + position);
		return position;		
	}

}
