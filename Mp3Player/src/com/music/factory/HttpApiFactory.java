package com.music.factory;

import com.music.factory.model.http.AbstractHttpApi;

/**
 * ��������
 * @author Administrator
 */
public interface HttpApiFactory {
	
	/**��������Ľ�������*/
	public AbstractHttpApi getHttpApi();
}
