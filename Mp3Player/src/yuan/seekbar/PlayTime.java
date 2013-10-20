package yuan.seekbar;

import yuan.constant.AppConstant;
import android.media.MediaPlayer;
import android.os.Handler;

/**
 * ���¸�������ʱ��͸��½�����
 * @author Administrator
 */
public class PlayTime {
	
	private Handler handler = new Handler();
	private MediaPlayer player = null;
	private TimerThread timerThread = null;
	
	public PlayTime(MediaPlayer player) {
		this.player = player;
		this.timerThread = new TimerThread();
	}
	
	/**�������ż�ʱ���߳�,ע������̲߳���һ���µ��̣߳�����UI�����߳�*/
	private class TimerThread extends Thread {
		@Override
		public void run() {			
			int currentPosition = player.getCurrentPosition();			
			refreshSeekBar(currentPosition);
			refreshPlayTime(currentPosition / 1000);			
			handler.post(timerThread);
		}		
	}
	
	/**���½�����*/
	private void refreshSeekBar(int currentPosition) { 	    		
		int songDuration = player.getDuration();
		int seekBarMaxValue = AppConstant.PlayComponent.timeSeekBar.getMax(); 		
		AppConstant.PlayComponent.timeSeekBar.setProgress(currentPosition*seekBarMaxValue/songDuration); 
	}
	
	/**���²���ʱ���*/
	private void refreshPlayTime(int pos) {
		AppConstant.PlayComponent.countTime.setText(timeConvert(pos));
		AppConstant.PlayComponent.miniCountTime.setText(timeConvert(pos));
	}
	
	/**��ʼ��ʱ*/
	public void beginCountTime() {		
		handler.post(timerThread);
	}
	
	/**��ͣ��ʱ*/
	public void pauseCountTime() {
		stopCountTime();
	}
	
	/**ֹͣ��ʱ*/
	public void stopCountTime() {
		handler.removeCallbacks(timerThread);
	}
		
	/**����ʱ��ת��*/
	private String timeConvert(int millTime) {
		int min = millTime / 60; //����
		int sec = millTime % 60; //����			
		return getFormatTime(min) + ":" + getFormatTime(sec);
	}
	
	/**@return ���ظ����ĸ�ʽ����׼ʱ�� ����ʽ�磺00:00*/
	private String getFormatTime(int time) {
		return (time >9 ? time : "0" + time).toString();		
	}
}
