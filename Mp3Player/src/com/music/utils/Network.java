package com.music.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ���������ȡʱ����Ҫ���ж��ֻ��Ƿ���������ش���
 * @author Administrator
 */
public class Network {

	/**
	 * �ж����������
	 * @param context
	 * @return ���������������
	 */
	@SuppressLint("DefaultLocale") public static String netType(Context context) {      
        NetworkInfo info = getNetworkInfo(context);
        String typeName = info.getTypeName();
        if (typeName.equalsIgnoreCase("wifi")) {
        	System.out.print("����ʹ��wifi����������");
        } else {
            typeName = info.getExtraInfo().toLowerCase();                
            //3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
            System.out.print("����ʹ���շ���������������");
        }     
    	return typeName;    
    }
	
	/***
	 * �ж��Ƿ�������
	 * @param context
	 * @return ����ʱ����true������false
	 */
	public static boolean isAccessNetwork(Context context) {
		try {
			NetworkInfo info = getNetworkInfo(context);
			return info!=null && info.isConnected(); 
		} catch (Exception e) {
			System.out.println("�ж�����ʱ��������");
			return false;	
		}	
	}
		 
	private static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager manager = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo();		
	}
}
