package com.music.lyric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.music.constant.Music;
import com.music.download.HttpDownloader;
import com.music.factory.model.Mp3Info;
import com.music.utils.Network;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class LyricLoadThread extends Thread{

	private InputStream inputStream = null;
	private Mp3Info mp3Info = null;
	private String charset = null;
	private List<LyricInfo> lyricInfo = null;
	private Context context = null;
	
	public LyricLoadThread(Mp3Info mp3Info, Context context) {
		this.mp3Info = mp3Info;
		this.context = context;
	}
	
	@Override
	public void run() {
		loadLyric();		
	}
	
	/**���ظ��*/
	private void loadLyric(){
		lyricInfo = null;
		if(mp3Info.getLrcURL().indexOf("http") != -1) {		
			if(Network.isAccessNetwork(context)) {//��ȡ�����ϵĸ��		
				inputStream = new HttpDownloader().getInputStreamFromUrl(mp3Info.getLrcURL());
				this.setDecode("UTF-8");
			} else {
				Toast.makeText(context, "��ǰ��û������", Toast.LENGTH_SHORT).show();
			}	
						
		} else{
			File lyricFile = new File(mp3Info.getLrcURL());
			if(lyricFile.exists()) {//���ز����ڸ�ʣ�������		
				loadLocalLyric();					
			} else {
				if(Network.isAccessNetwork(context)) {
					downloadLRC();
					loadLocalLyric();
				} else {
					Toast.makeText(context, "��ǰ��û������", Toast.LENGTH_SHORT).show();
				}				
			}		
		}
		if(inputStream != null) {
			lyricInfo = new LyricHandle().handleLyric(inputStream, this.getDecode());
			sendLoadlyricOverBroadcast();
		}		
	}
	
	/**���ر��ظ��*/
	private void loadLocalLyric() {
		try {
			inputStream = new FileInputStream(mp3Info.getLrcURL());
			this.setDecode("GBK");				
		} catch (FileNotFoundException e) {
			System.out.println(mp3Info.getLrcURL() + " �Ҳ�����ʣ�");
		}
	}
	
	/**����ǧǧ�������*/		
	private void downloadLRC() {			
		List<QianQianJingTingLyricInfo> list;
		try {
			String lyricName = mp3Info.getSingerName() + " - " + mp3Info.getMp3SimpleName() + ".lrc";
			if(mp3Info.getSingerName().contains("<unknown>")) {
				mp3Info.setSingerName("");
				lyricName = mp3Info.getMp3SimpleName() + ".lrc";
			}
			list = QianQianJingTingLyricUtil.search(mp3Info.getSingerName(), mp3Info.getMp3SimpleName());
			if(list.size() > 0) {
				QianQianJingTingLyricInfo result = (QianQianJingTingLyricInfo)list.get(0);
				result.saveLRC(lyricName);			
				System.out.println(mp3Info.getMp3SimpleName() + "��������سɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(mp3Info.getMp3SimpleName() + "ǧǧ�����������ʧ��");
		}
	}
	
	private void setDecode(String charset) {		
		this.charset = charset;
	}

	public String getDecode() {
		return charset;
	}
	
	private void sendLoadlyricOverBroadcast() {
		Intent intent = new Intent();
		intent.setAction(Music.LOAD_LYRIC_OVER);
		context.sendBroadcast(intent);
	}
	
	public List<LyricInfo> getLyricInfos() {		
		return lyricInfo;
	}
}
