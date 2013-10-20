package yuan.factory;

import java.util.List;

import yuan.factory.model.Mp3Info;
import yuan.http.AbstractHttpApi;
import yuan.xml.AbstractParse;

/**
 * ���󹤳��ӿ�
 * @author Administrator
 */
public interface OnlineFactory {
	
	/**��������Ľ�������*/
	public AbstractParse getParse();
	
	/**���������httpЭ�����*/
	public AbstractHttpApi getHttpApi();
	
	/**ִ�������������httpЭ��������Ϊ*/
	public List<Mp3Info> execute();
}
