package com.music.factory;

import com.music.factory.model.http.AbstractHttpApi;

/**
 * 工厂方法
 * @author Administrator
 */
public interface HttpApiFactory {
	
	/**创建具体的解析对象*/
	public AbstractHttpApi getHttpApi();
}
