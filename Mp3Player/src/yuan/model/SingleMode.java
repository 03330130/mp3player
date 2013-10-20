package yuan.model;

import java.util.List;

public class SingleMode extends AbstractPlayMode {

	private static final long serialVersionUID = 1L;

	@Override
	public int getBeforePosition(int position, List<Mp3Info> mp3Infos) {
		return position;
	}

	@Override
	public int getAfterPosition(int position, List<Mp3Info> mp3Infos) {
		return position;
	}
	
	/**���ص���ѭ�����ŵ���һ�׸��λ��*/
	public int getPosition(int position) {	
		return position;	
	}
}
