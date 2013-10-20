package yuan.lyric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��ʴ�����
 * @author Administrator
 */
public class LyricHandle {
	
	public LyricHandle() {
		
	}
	
	/**
	 * �Ը���ļ����������д���ÿ��ȡ���е�ÿ�и�ʺͶ�Ӧ��ʱ��㣬<br>
	 * Ȼ������һ��������͵�LyricInfo���ݽṹ�У��ٰѴ˸�����͵�LyricInfo���ݽṹ<br>
	 * �ٴ����һ��������Vector<LyricInfo>�Ķ���
	 * @param lyricStream  ����ļ���������
	 * @param charset ���մ��ַ�������ʶ��lyricStream
	 * @return ��ž��������ÿ�и�ʺͶ�Ӧʱ����Vector<LyricInfo>�Ķ���
	 */
	public List<LyricInfo> handleLyric(InputStream lyricStream, String charset) {
		List<LyricInfo> lyricInfos = new ArrayList<LyricInfo>();
		try {					
			String lineStr = null;
			Long time = null;
			String lyric = null;			
			Pattern pattern = Pattern.compile("\\[([0-9\\:\\.]+)\\]");		
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(lyricStream, charset));
			while ((lineStr = bufferReader.readLine()) != null) {
				Matcher matcher = pattern.matcher(lineStr);
				while(matcher.find()) {
					LyricInfo lyricInfo = new LyricInfo();

					//�õ�ʱ��
					String timeStr = matcher.group();
					time = TimeToLong(timeStr.substring(1, timeStr
							.length() - 1));
					if(time == -1) {
						continue;//���ʱ��ת���쳣�����������и��
					}
					
					//�õ����
					if(!lineStr.endsWith("]"))
						lyric = lineStr.substring(lineStr.lastIndexOf("]") + 1);
					else 
						lyric = "......"; //���б�ʾ���и��Ϊ��
					
					lyricInfo.setTime(time);
					lyricInfo.setLyric(lyric.trim());
					lyricInfos.add(lyricInfo);				
				}
			}
			bufferReader.close();		
			
			//�Ը��ʱ����д�С��������
			Collections.sort(lyricInfos, new SortLyricInfoByTime());
			//�õ�һ�и�ʳ�����ʱ��
			for(int i=0; i<lyricInfos.size(); i++) {			
				LyricInfo lyricInfoOne = lyricInfos.get(i);			
				if(i+1 < lyricInfos.size()) {			
					LyricInfo lyricInfoTwo = lyricInfos.get(i+1);
					lyricInfoOne.setDuration(lyricInfoTwo.getTime()-lyricInfoOne.getTime());
				}
			}			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lyricInfos;		
	}
	
	/**������������͵�LyricInfo��ʱ�����д�С��������*/
	private class SortLyricInfoByTime implements Comparator<LyricInfo> {		
		public int compare(LyricInfo lyricInfoOne, LyricInfo lyricInfoTwo) {
			return sortUp(lyricInfoOne, lyricInfoTwo);
		}
		
		//���ո��ʱ����С��������
		private int sortUp(LyricInfo lyricInfoOne, LyricInfo lyricInfoTwo) {
			if (lyricInfoOne.getTime() < lyricInfoTwo.getTime())
				return -1;
			else if (lyricInfoOne.getTime() > lyricInfoTwo.getTime())
				return 1;
			else
				return 0;
		}
	}
	
	/**���ַ������͵�ʱ��ת��ΪLong���͵�ʱ��
	 * @return ����ַ������͵�ʱ��Ϸ���������long���͵�ʱ�䣬��λΪ����<br>
	 * 		        ���򷵻�-1
	 */
	private long TimeToLong(String Time) {
		try {
			String[] s1 = Time.split(":");
			int min = Integer.parseInt(s1[0]);
			String[] s2 = s1[1].split("\\.");
			int sec = Integer.parseInt(s2[0]);
			int mill = 0;
			if (s2.length > 1)
				mill = Integer.parseInt(s2[1]);
			return min * 60 * 1000 + sec * 1000 + mill * 10;
		} catch (Exception e) {
			return -1;
		}
	}
}
