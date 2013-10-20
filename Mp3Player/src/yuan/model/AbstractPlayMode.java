package yuan.model;

import java.io.Serializable;
import java.util.List;

/**
 * ��ȡǰ��һ�׸��λ��
 * @author Administrator
 *
 */
public abstract class AbstractPlayMode implements Serializable{
		
	private static final long serialVersionUID = 1L;
	/**��ȡǰһ�׸��λ��*/
	public abstract int getBeforePosition(int position, List<Mp3Info> mp3Infos);
	/**��ȡ��һ�׸��λ��*/
	public abstract int getAfterPosition(int position, List<Mp3Info> mp3Infos);
}
