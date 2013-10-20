package yuan.model;

import java.util.List;

/**
 * �����ȡǰ��һ�׸��λ��
 * @author Administrator
 *
 */
public class RandomPlayMode extends AbstractPlayMode {

	private static final long serialVersionUID = 1L;

	@Override
	public int getBeforePosition(int position, List<Mp3Info> mp3Infos) {
		//Math.random()*(b-a)+a��a - b֮�����ѡȡһ����
		position = (int) (Math.random()*(mp3Infos.size() - 1));
		return position;
	}

	@Override
	public int getAfterPosition(int position, List<Mp3Info> mp3Infos) {
		position = (int) (Math.random()*(mp3Infos.size() - 1));
		return position;
	}

}
