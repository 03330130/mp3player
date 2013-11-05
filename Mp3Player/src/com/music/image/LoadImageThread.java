package com.music.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.music.constant.Music;
import com.music.download.HttpDownloader;
import com.music.factory.model.ImageInfo;
import com.music.factory.model.Mp3Info;
import com.music.utils.FileUtils;
import com.music.utils.Network;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class LoadImageThread extends Thread {
	
	private Mp3Info mp3Info = null;	
	private Bitmap sourceBitmap = null;
	private Bitmap bigBitmap = null;
	private Bitmap miniBitmap = null;
	private Context context = null;
	private boolean isLoadNextImg = false;
	private BitmapHandle picHandle = null;
	private final static int WIDTH = 320;
	private final static int HEIGHT = 480;
	private final static float SCREEN_RATIO = 1.5f;

	public LoadImageThread(Mp3Info mp3Info, Context context, boolean isLoadNextImg) {
		this.mp3Info = mp3Info;
		this.context = context;
		this.isLoadNextImg = isLoadNextImg;
		this.picHandle = new BitmapHandle(context);
	}

	/** �Ѿ������ĸ���ͼ���浽���� */
	private void saveSingerPic(Bitmap sourceBitmap) {		
		if(sourceBitmap != null) {
			FileOutputStream file = null;
			String name = null;
			try {
				if(mp3Info.getSingerName() != null && !mp3Info.getSingerName().equals("<unknown>")) {
					name = mp3Info.getSingerName();
				} else {
					name = mp3Info.getMp3SimpleName();
				}
				file = new FileOutputStream(FileUtils.IMAGESDIR + name + FileUtils.IMAGEEXTENSION);								
				sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 90, file);
				System.out.println("�������ͼƬ�ɹ�:" + mp3Info.getSingerName());
				file.flush();
				file.close();
			} catch (FileNotFoundException e) {
				System.err.println("�������ͼƬ��������");
			} catch (IOException e) {				
				System.err.println("�������ͼƬ��������");
			}			
		}		
	}	
	
	private String getOnlinePicData(int pageNum, String keyWord) 
			throws ClientProtocolException, IOException {		
		if(keyWord.contains(" ")) {
			keyWord = keyWord.replace(" ", "-");
		}
		HttpClient hClient = new DefaultHttpClient();
	    HttpClientParams.setCookiePolicy(hClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
	    HttpGet hGet = new HttpGet("http://image.baidu.com/i?" +
	    		"tn=baiduimagejson&ie=utf-8&rn=60&pn=" + pageNum + "&word=" + keyWord); 
	     		 
	    ResponseHandler<String> rHandler = new BasicResponseHandler();
		return hClient.execute(hGet, rHandler);	
	} 
	
	private List<ImageInfo> searchPic(int pageNum, String keyWord) {
		List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
		try{  	     	     	     
		    String data = getOnlinePicData(pageNum, keyWord);
		    //System.out.println(data);
			JSONObject jo = new JSONObject(data); 	     
	        JSONArray jsonArray = jo.optJSONArray("data");
	        
	        for (int i = 0; i < jsonArray.length(); ++i) {        	        	 
	       	
	        	int width = jsonArray.getJSONObject(i).getInt("width");
	        	int height = jsonArray.getJSONObject(i).getInt("height");
	        	
	        	if(width >= WIDTH && height >= HEIGHT) {
	        		if((float)height/width >= SCREEN_RATIO) {
		        		ImageInfo imageInfo = new ImageInfo();
		        		imageInfo.setPicUrl(jsonArray.getJSONObject(i).getString("objURL"));
		        		imageInfos.add(imageInfo);
	        		}		        		 
	        	}	        	                	 
	        }	         			    
		 } catch (Exception e) {
			 e.printStackTrace();
		 }  
		 return imageInfos;		
	}
	
	private void loadSingerImage() {							
		File picFile = new File(mp3Info.getSingerBigImageURL());		
		if(!isLoadNextImg && picFile.exists() && picFile.length() > 0) { //����ͼƬ							
			sourceBitmap = BitmapFactory.decodeFile(mp3Info.getSingerBigImageURL(), setBitmapFactoryOptions());					
		} else {//����ͼƬ
			//ɾ��SD���д�СΪ0��ͼƬ���д�ͼƬ��
			if(picFile.length() == 0) {
				picFile.delete(); 
			}
			if(Network.isAccessNetwork(context)) {
				List<ImageInfo> imageInfos = null;
				int pageNum = -59;
				System.out.println("������������ͼƬ......");
				if(mp3Info.getSingerName() != null && !mp3Info.getSingerName().
						equals("") && !mp3Info.getSingerName().equals("<unknown>")) 
					//�Ը���Ϊ�ؼ�������ͼƬ					
					do {
						pageNum += 60;							
						if(pageNum > 600) {
							sourceBitmap = null;							
							System.out.println("�Ҳ�������ͼƬ");	
							break;
						}
						imageInfos = searchPic(pageNum, mp3Info.getSingerName());						
					} while(!isFindPic(imageInfos));										
				else {
					//��������Ϊ��ʱ���Ը���Ϊ�ؼ�������ͼƬ
					//�Ը���Ϊ�ؼ�������ͼƬ			
					do {
						pageNum += 60;
						if(pageNum > 600) {						
							sourceBitmap = null;
							System.out.println("�Ҳ�������ͼƬ");
							break;
						}
						imageInfos = searchPic(pageNum, mp3Info.getMp3SimpleName());
					} while(!isFindPic(imageInfos));									
				}				
			} else {
				Toast.makeText(context, "��ǰ��û����", Toast.LENGTH_SHORT).show();
			}							
		}		
		bigBitmap = picHandle.clipPlayBgPic(sourceBitmap);	
		miniBitmap = picHandle.clipMiniPic(sourceBitmap);		
	}
	
	private boolean isFindPic(List<ImageInfo> imageInfos) {
		boolean flag = false;
		System.out.println("imageInfos :" + imageInfos.size() + "\n" + imageInfos);
		if(imageInfos.size() > 0) {	
			
			int index = (int) (Math.random()*(imageInfos.size() - 1));
			String picUrl = imageInfos.get(index).getPicUrl();			
			sourceBitmap = BitmapFactory.decodeStream(
					new HttpDownloader().getInputStreamFromUrl(picUrl), null, setBitmapFactoryOptions());																
			if(sourceBitmap != null) {
				flag = true;
				System.out.println("���ҵ�����ͼƬ");
			}									
			sourceBitmap = picHandle.clipBigPic(sourceBitmap);
			saveSingerPic(sourceBitmap);
		}		
		return flag;	
	}
	
	public void run() {
		loadSingerImage();		
		sendLoadImageOverBroadcast();
	}

	/** ���ͼ���ͼƬ��Ϲ㲥 */
	private void sendLoadImageOverBroadcast() {
		Intent intent = new Intent();
		intent.setFlags(0x15);
		intent.setAction(Music.UPDATE_UI_ACTION);		
		context.sendBroadcast(intent);
	}
	
	public void freeBitmap() {
		if(sourceBitmap != null) { 
			sourceBitmap.recycle();
			sourceBitmap = null;
			System.gc();
		}
		
		if(bigBitmap != null) {
			bigBitmap = null;
			System.gc();
		}
		if(miniBitmap != null) {
			miniBitmap = null;
			System.gc();
		}
	}
	
	private BitmapFactory.Options setBitmapFactoryOptions() {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888; 
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return opt;		
	}
	
	public Bitmap getMiniBitmap() {				
		return miniBitmap != null ? miniBitmap : null;
	}
	
	public Bitmap getBigBitmap() {	
		return bigBitmap != null ? bigBitmap : null;
	}
}
