package yuan.factory;

import yuan.factory.model.http.AbstractHttpApi;

/**
 * ��������
 * @author Administrator
 */
public interface HttpApiFactory {
	
	/**��������Ľ�������*/
	public AbstractHttpApi getHttpApi();
}
