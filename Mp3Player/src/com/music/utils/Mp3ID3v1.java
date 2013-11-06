package com.music.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import com.music.mp3player.Music;


public class Mp3ID3v1 {

	private static boolean isID3v1 = false;
	
	/**
	 * ��ȡ���MP3��Ϣ�����128λ����
	 * @param filePath
	 * @return ����װ��MP3ID3v1��128λ����
	 */
	private static byte[] getMp3ID3v1Data(String filePath) {
		RandomAccessFile raf = null;
		byte[] data128 = new byte[128];
		try {
			raf = new RandomAccessFile(filePath, "r");		
			raf.seek(raf.length() - 128);
			raf.read(data128);
		} catch (FileNotFoundException e) {		
			System.out.println("��ȡMP3��ID3v1��128λ���ݳ�������");
		} catch (IOException e) {
			System.out.println("��ȡMP3��ID3v1��128λ���ݳ�������");
		}
		return data128;		
	}	
	
	public static Music Mp3ID3v1Info(String filePath) {
		Music mp3Info = null;
		byte[] data128 = getMp3ID3v1Data(filePath);
		String tag = new String(data128, 0, 3);
		if(tag.equalsIgnoreCase("TAG")) {
			isID3v1 = true; //����
			mp3Info = new Music();
			try {
				mp3Info.setMp3SimpleName(new String(data128, 3, 30, "GB2312").trim());
				mp3Info.setMp3Name(new String(data128, 3, 30, "GB2312").trim() + "mp3");
				mp3Info.setSingerName(new String(data128, 33, 30, "GB2312").trim());
				mp3Info.setAlbumName(new String(data128, 63, 30, "GB2312").trim());		
			} catch (UnsupportedEncodingException e) {
			}			
			System.out.println("name:" + mp3Info.getMp3SimpleName() + "                             ����:" + mp3Info.getSingerName() + "                                   ר����:"
					+ mp3Info.getAlbumName());
			/*songName = new String(data128, 3, 30).trim();
			artist = new String(data128, 33, 30).trim();
			album = new String(data128, 63, 30).trim();
			year = new String(data128, 93, 4).trim();
			comment = new String(data128, 97, 28).trim();*/
		}
		return mp3Info;
	}

	public static boolean isID3v1() {
		return isID3v1;
	}
}
