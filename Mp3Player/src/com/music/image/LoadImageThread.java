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

import com.music.constant.MusicContant;
import com.music.download.HttpDownloader;
import com.music.factory.model.Image;
import com.music.mp3player.Music;
import com.music.utils.FileUtils;
import com.music.utils.Network;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class LoadImageThread extends Thread {
	
	private Music mMusic = null;	
	private Bitmap sourceBitmap = null;
	private Bitmap bigBitmap = null;
	private Bitmap miniBitmap = null;
	private Context context = null;
	private boolean isLoadNextImg = false;
	private BitmapHandle picHandle = null;
	private final static int WIDTH = 320;
	private final static int HEIGHT = 480;
	private final static float SCREEN_RATIO = 1.5f;

	public LoadImageThread(Music music, Context context, boolean isLoadNextImg) {
		this.mMusic = music;
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
				if(mMusic.getSingerName() != null && !mMusic.getSingerName().equals("<unknown>")) {
					name = mMusic.getSingerName();
				} else {
					name = mMusic.getMp3SimpleName();
				}
				file = new FileOutputStream(FileUtils.IMAGESDIR + name + FileUtils.IMAGEEXTENSION);								
				sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 90, file);
				System.out.println("�������ͼƬ�ɹ�:" + mMusic.getSingerName());
				file.flush();
				file.close();
			} catch (FileNotFoundException e) {
				System.err.println("�������ͼƬ��������");
			} catch (IOException e) {				
				System.err.println("�������ͼƬ��������");
			}			
		}		
	}	
	
	private String getOnlinePicData(int width, int pageNum, String keyWord) 
			throws ClientProtocolException, IOException {		
		if(keyWord.contains(" ")) {
			keyWord = keyWord.replace(" ", "-");
		}
		HttpClient hClient = new DefaultHttpClient();
	    HttpClientParams.setCookiePolicy(hClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
	    /*String url = "http://image.baidu.com/i?" +
	    		"tn=baiduimagejson&width=" + width +  "&height=" + (int)(width*SCREEN_RATIO)
	    	  + "&ie=utf-8&rn=60&pn=" + pageNum + "&word=" + keyWord;*/
	    String url2 = "http://image.baidu.com/i?" +
	    		"tn=baiduimagejson&ie=utf-8&rn=60&pn=" + pageNum + "&word=" + keyWord;
	    System.out.println(url2);
	    HttpGet hGet = new HttpGet(url2); 	    
	    ResponseHandler<String> rHandler = new BasicResponseHandler();
		return hClient.execute(hGet, rHandler);	
	} 
	
	private List<Image> searchPic(int width, int pageNum, String keyWord) {
		List<Image> images = new ArrayList<Image>();			     	     	      
		JSONArray jsonArray = null;
		try {
			 String data = getOnlinePicData(width, pageNum, keyWord);
			 System.out.println(data);
			 JSONObject jo = new JSONObject(data);
			 jsonArray = jo.optJSONArray("data");
		} catch (Exception e) {
			e.printStackTrace();
		}        
        
        for (int i = 0; i < jsonArray.length() - 1; ++i) {        	        	 
        	try {
				int mWidth = jsonArray.getJSONObject(i).getInt("width");
				int mHeight = jsonArray.getJSONObject(i).getInt("height");
        	
				if(mWidth >= WIDTH && mHeight >= HEIGHT) {
					if((float)mHeight/mWidth >= SCREEN_RATIO) {
						Image imageInfo = new Image();
						imageInfo.setPicUrl(jsonArray.getJSONObject(i).getString("objURL"));
						images.add(imageInfo);
					}		        		 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}    	    	                	 
        }	         			    
		return images;		
	}
	
	private void loadSingerImage() {							
		File picFile = new File(mMusic.getSingerBigImageURL());		
		if(!isLoadNextImg && picFile.exists() && picFile.length() > 0) { //����ͼƬ							
			sourceBitmap = BitmapFactory.decodeFile(mMusic.getSingerBigImageURL(), setBitmapFactoryOptions());					
		} else {//����ͼƬ
			//ɾ��SD���д�СΪ0��ͼƬ���д�ͼƬ��
			if(picFile.length() == 0) {
				picFile.delete(); 
			}
			if(Network.isAccessNetwork(context)) {
				List<Image> images = null;
				int pageNum = -59;
				int curWidth = WIDTH - 50;
				System.out.println("������������ͼƬ......");
				if(mMusic.getSingerName() != null && !mMusic.getSingerName().
						equals("") && !mMusic.getSingerName().equals("<unknown>")) 
					//�Ը���Ϊ�ؼ�������ͼƬ					
					do {
						pageNum += 60;
						curWidth += 50;
						if(pageNum > 600) {
							sourceBitmap = null;							
							System.out.println("�Ҳ�������ͼƬ");	
							break;
						}
						images = searchPic(curWidth, pageNum, mMusic.getSingerName());						
					} while(!isFindPic(images));										
				else {
					//��������Ϊ��ʱ���Ը���Ϊ�ؼ�������ͼƬ
					//�Ը���Ϊ�ؼ�������ͼƬ
					pageNum = -59;
					curWidth = WIDTH - 50;
					do {
						pageNum += 60;
						curWidth += 50;
						if(pageNum > 600) {						
							sourceBitmap = null;
							System.out.println("�Ҳ�������ͼƬ");
							break;
						}
						images = searchPic(WIDTH, pageNum, mMusic.getMp3SimpleName());
					} while(!isFindPic(images));									
				}				
			} else {
				Toast.makeText(context, "��ǰ��û����", Toast.LENGTH_SHORT).show();
			}							
		}		
		bigBitmap = picHandle.clipPlayBgPic(sourceBitmap);	
		miniBitmap = picHandle.clipMiniPic(sourceBitmap);		
	}
	
	private boolean isFindPic(List<Image> images) {
		boolean flag = false;
		//System.out.println("images :" + images.size() + "\n" + images);
		if(images.size() > 0) {	
			
			int index = (int) (Math.random()*(images.size() - 1));
			String picUrl = images.get(index).getPicUrl();			
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
		intent.setAction(MusicContant.UPDATE_UI_ACTION);		
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
