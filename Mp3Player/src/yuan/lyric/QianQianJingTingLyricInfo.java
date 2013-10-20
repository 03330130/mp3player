package yuan.lyric;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import yuan.utils.FileUtils;

/**װ��ǧǧ������ʵ���Ϣ��ʵ����*/
public class QianQianJingTingLyricInfo {
	public interface Task {
		public abstract String getLyricContent();
	}

	private int lrcId;
	private String lrcCode;
	private String artist;
	private String title;
	private Task task;
	private String content;

	public QianQianJingTingLyricInfo(int lrcId, String lrcCode, String artist, String title, Task task) {
		this.lrcId = lrcId;
		this.lrcCode = lrcCode;
		this.artist = artist;
		this.title = title;
		this.task = task;
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getLrcCode() {
		return lrcCode;
	}

	public int getLrcId() {
		return lrcId;
	}

	public String getContent() {
		if (content == null)
			content = task.getLyricContent();
		return content;
	}

	/**
	 * @param lrcName һ��Ҫ������׺��
	 * @throws IOException
	 */
	public void saveLRC(String lrcName)throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(FileUtils.LYRICDIR + lrcName), "GBK"));
		bw.write(getContent());
		bw.close();
	}

	public String toString() {
		return (new StringBuilder()).append(artist).append(":").append(title).toString();
	}
}
