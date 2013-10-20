package yuan.seekbar;

import yuan.constant.MusicPlayer;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
	
/**
 * ����������������
 * @author Administrator
 */
public class SeekBarListener implements OnSeekBarChangeListener {	

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	
	/**����������������player�Ĳ��Ž��ȵ����������ڵ�λ��*/
	public void onStopTrackingTouch(SeekBar seekBar) {		
		MediaPlayer player = MusicPlayer.getPlayer();
		if(player != null) {
			int currentProgressValue = seekBar.getProgress();
			int songDuration = player.getDuration();		
			int seekBarMaxValue = seekBar.getMax();
			player.seekTo(songDuration * currentProgressValue / seekBarMaxValue);	
		} else 
			seekBar.setProgress(0);
	}	
}

