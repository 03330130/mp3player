package com.music.factory.model.playmode;

/**
 * ��ȡǰ��һ�׸��λ�ã�ʹ�ù����������ģʽ���ڲ��ࣩ
 * @author Administrator
 */
public abstract class AbstractPlayMode {
	
	/**
	 * @param index ��ǰ���ŵĸ���������
	 * @param size �����б������
	 * @return ǰһ�׸��ڸ����б��е�����
	 */
	public abstract int prevIndex(int index, int size);
	
	/**
	 * @param index ��ǰ���ŵĸ���������
	 * @param size �����б������
	 * @return ��һ�׸��λ��
	 */
	public abstract int nextIndex(int index, int size);
}
