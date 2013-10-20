package yuan.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import yuan.factory.model.Mp3Info;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class FileUtils {
	
	public final static String SD_CARD_ROOT = Environment.getExternalStorageDirectory().
			getAbsolutePath() + File.separator;
	public final static String MP3EXTENSION = ".mp3";
	public final static String LYRICEXTENSION = ".lrc";
	public final static String IMAGEEXTENSION = ".jpg";
	public final static String MAINFOLDER = "mp3player" + File.separator;
	public final static String MP3FOLDER = "mp3" + File.separator;
	public final static String LYRICFOLDER = "lyric" + File.separator;
	public final static String IMAGEFOLDER = "images" + File.separator;
	public final static String CRASHFOLDER = "crash" + File.separator;
	public final static String MAINDIR = SD_CARD_ROOT + MAINFOLDER;
	public final static String MP3DIR = SD_CARD_ROOT + MAINFOLDER + MP3FOLDER;
	public final static String LYRICDIR = SD_CARD_ROOT + MAINFOLDER + LYRICFOLDER;
	public final static String IMAGESDIR = SD_CARD_ROOT + MAINFOLDER + IMAGEFOLDER;
	public final static String CRASHDIR = SD_CARD_ROOT + MAINFOLDER + CRASHFOLDER;
	
	/**
	 * ��������ʱ������Ŀ¼���ڴ��MP3 ��lrc��images
	 */
	public static void createDefaultDir() {
		//scanSong(SD_CARD_ROOT);
		File mp3Dir = new File(MP3DIR);
		File lrcDir = new File(LYRICDIR);
		File imagesDir = new File(IMAGESDIR);
		File crash = new File(CRASHDIR);
		if(!mp3Dir.exists())
			mp3Dir.mkdirs();
		if(!lrcDir.exists())
			lrcDir.mkdirs();
		if(!imagesDir.exists())
			imagesDir.mkdirs();
		if(!crash.exists()) 
			crash.mkdirs();
	}
	
	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public boolean isFileExist(String fileName, String path) {
		File file = new File(path + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputStream���������д�뵽SD����
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {

		File file = null;
		OutputStream output = null;
		try {
			file = new File(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[1024 * 100];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * �Ѻ���ʱ��ת��ΪMP3�ı�׼ʱ��
	 * @param mp3Time : �ַ�������
	 * @return MP3�ı�׼ʱ��
	 */
	public static String timeConvert(String mp3Time) {
		if(mp3Time == null) {
			return null;
		}
		int millisecond = Integer.parseInt(mp3Time);
		int minute = millisecond/(60*1000);
		int secord = millisecond%(60*1000)/1000;
		//����String���͵ı�׼ʱ��
		return "0" + minute + ":" + (secord >9 ? secord : "0" + secord);
	}
	
	/**
	 * �Ѻ���ʱ��ת��ΪMP3�ı�׼ʱ��
	 * @param mp3Time : ���κ���
	 * @return MP3�ı�׼ʱ��
	 */
	public static String IntTimeConvert(int mp3Time) {
		int minute = mp3Time/(60*1000);
		int secord = mp3Time%(60*1000)/1000;
		//����String���͵ı�׼ʱ��
		return "0" + minute + ":" + (secord >9 ? secord : "0" + secord);
	}
	
	public static void scanSong(String dir) {
		File[] file = new File(dir).listFiles(new SongFileFilter());
		if(file == null) {
			return;
		}
		for (int i = 0; i < file.length; i++) {						
			if(file[i].isDirectory()) {				
				scanSong(file[i].getAbsolutePath());
			} else {			
				Mp3Info mp3Info = Mp3ID3v1.Mp3ID3v1Info(file[i].getAbsolutePath());
			}
		}
	}
	
	public static List<Mp3Info> getMediaStoreMp3Infos(Context content){
		//����ϵͳ�������ݿ�
		content.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + 
				Environment.getExternalStorageDirectory().getAbsolutePath())));
		
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		int id = 0;
		String[] projections = {
				MediaStore.Audio.Media.ARTIST,        //����
				MediaStore.Audio.Media._ID,           //ID
				MediaStore.Audio.Media.DURATION,      //ʱ��
				MediaStore.Audio.Media.SIZE,          //��С
				MediaStore.Audio.Media.DISPLAY_NAME,  //ȫ��������׺����
				MediaStore.Audio.Media.DATA};         //MP3���ڵľ���·��
		String[] selections = {
				"204800", //��С����200KB
				"%mp3"  //��׺Ϊ.mp3
		};	
		Cursor cursor = content.getContentResolver().query(
             MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projections,
             MediaStore.Audio.Media.SIZE + " >= ? and " +
             MediaStore.Audio.Media.DISPLAY_NAME + " like ?" , selections, null);
		while (cursor != null && cursor.moveToNext()) {//MediaStore.Audio.Media.IS_MUSIC + " = 1"			
			Mp3Info mp3Info = new Mp3Info();		
			
			mp3Info.setId(id++);
			mp3Info.setMp3Time(timeConvert(cursor.getString(2)));
			mp3Info.setMp3Size(cursor.getString(3));			
			//���ø�������
			if(cursor.getString(4).indexOf(" -") != -1)
				mp3Info.setSingerName(cursor.getString(4).substring(0, cursor.getString(4).indexOf(" -")));
			else if(cursor.getString(4).indexOf("-") != -1)
				mp3Info.setSingerName(cursor.getString(4).substring(0, cursor.getString(4).indexOf("-")));
			else  
				mp3Info.setSingerName(cursor.getString(0));
			//����MP3��������׺��	
			if(cursor.getString(4).indexOf("- ") != -1)
				mp3Info.setMp3Name(cursor.getString(4).substring(cursor.getString(4).indexOf("- ") + 2));
			else if(cursor.getString(4).indexOf("-") != -1)
				mp3Info.setMp3Name(cursor.getString(4).substring(cursor.getString(4).indexOf("-") + 1));
			else 
				mp3Info.setMp3Name(cursor.getString(4));
			
			mp3Info.setMp3SimpleName(mp3Info.getMp3Name().replace(MP3EXTENSION, ""));
			mp3Info.setMp3URL(cursor.getString(5));			
			mp3Info.setLrcURL(FileUtils.LYRICDIR + cursor.getString(4).replace(MP3EXTENSION, LYRICEXTENSION));
			if(mp3Info.getSingerName() != null && !mp3Info.getSingerName().equals("<unknown>"))
				mp3Info.setSingerBigImageURL(FileUtils.IMAGESDIR + mp3Info.getSingerName() + IMAGEEXTENSION);
			else 
				mp3Info.setSingerBigImageURL(FileUtils.IMAGESDIR + mp3Info.getMp3SimpleName() + IMAGEEXTENSION);
			mp3Infos.add(mp3Info);
		}
		cursor.close();//��Ҫ���ǹر�cursor
      return mp3Infos;
	}
}