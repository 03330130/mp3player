package com.music.lyric;

public class LyricInfo {
	
	/**��һ�и�ʶ�Ӧ��ʱ���*/
	private Long time;
	
	/**��һ�и�ʳ�����ʱ��*/
	private Long duration = 0L;
	
	/**��һ�и�ʵ�����*/
	private String lyric;
	
	public LyricInfo() {
		
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getLyric() {
		return lyric;
	}
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	@Override
	public String toString() {
		return "LyricInfo [time=" + time + ", duration=" + duration
				+ ", lyric=" + lyric + "]";
	}	
}
