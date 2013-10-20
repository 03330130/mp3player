package yuan.lyric;
import java.util.List;
import yuan.constant.MusicPlayer;
import yuan.image.LoadImageThread;
import yuan.model.Mp3Info;
import yuan.mp3player.broadcast.LoadImageBroadcastReceiver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class LyricTextView extends TextView implements OnLongClickListener{

	private static final String TAG = "LyricTextView";
	
	private Paint currentPaint = null; 
	private Paint notCurrentPaint = null;
	private int currentPaintColor = Color.WHITE;
	private int notCurrentPaintColor = Color.WHITE;
	private Typeface currentPaintTypeface = Typeface.DEFAULT;
	private Typeface notCurrentPaintTypeface = Typeface.DEFAULT;
	private int currentPaintSize = 24;
	private int notCurrentPaintSize = 24; 
	private int lyricSpace = 32; //���¸��֮��ļ��
	private float width = 0;  //��ʽ���Ŀ�
	private float height = 0; //��ʽ���ĸ�
	
	private int index = -3; //��ʵ���������ʼ��Ϊ -3
	
	private boolean isLyric = true; //����Ƿ���ڵı�־
	private List<LyricInfo> lyricInfos = null;
	private Handler lyricUpdateHandler = new Handler();
	//����private Handler lyricUpdateHandler = getHandler();
	
	private LyricUpdateThread lyricUpdateThread = null;
	public boolean isPauseRefreshLyric = false;
	
	private Mp3Info currentMp3Info;

	Context context;
	
	/*�������캯��ȱһ����*/
	public LyricTextView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public LyricTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public LyricTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		this.setLongClickable(true);
		this.setOnLongClickListener(this);
		
		currentPaint = new Paint();
		currentPaint.setAntiAlias(true);
		currentPaint.setColor(currentPaintColor);
		currentPaint.setTextSize(currentPaintSize);
		currentPaint.setTypeface(currentPaintTypeface);
		currentPaint.setTextAlign(Paint.Align.LEFT);
		
		notCurrentPaint = new Paint();
		notCurrentPaint.setAntiAlias(true);
		notCurrentPaint.setColor(notCurrentPaintColor);
		notCurrentPaint.setAlpha(170);
		notCurrentPaint.setTextSize(notCurrentPaintSize);
		notCurrentPaint.setTypeface(notCurrentPaintTypeface);
		notCurrentPaint.setTextAlign(Paint.Align.LEFT);
	}
	
	/**�ж��Ƿ���ڸ�ʣ����isLyric��false,�Ͳ����������¸�ʵ��̣߳�������Ȼ*/
	public void setLyricInfo(List<LyricInfo> lyricInfos, int flag) {		
		this.lyricInfos = lyricInfos;
		lyricUpdateThread = new LyricUpdateThread();
		isPauseRefreshLyric = false;
		isLyric = false;
		if(flag == 0) {
			index = -2;
			invalidate();
		} else if(flag == 1) {
			if(lyricInfos != null && lyricInfos.size() > 0) {
				isLyric = true; //���������ڸ��
			} else if(lyricInfos == null) {
				isLyric = false; //�������ڸ��
				index = 10000;
				invalidate();
			}
		}
	}
	
	/** ���¸�ʵ��߳�*/	 
	private class LyricUpdateThread implements Runnable{				
		public void run() {						
			if(lyricInfos != null && lyricInfos.size() > 0 && MusicPlayer.getPlayer() != null) {
				int currentPosition = MusicPlayer.getPlayer().getCurrentPosition();					
				for (int i = 1; i < lyricInfos.size(); i++) {
					if (currentPosition < lyricInfos.get(i).getTime())
						if (currentPosition >= lyricInfos.get(i - 1).getTime()) {
							index = i - 1;
							invalidate();
						}
				}
				lyricUpdateHandler.postDelayed(lyricUpdateThread, 5);		
			}
		}		
	}
	
	/**��ʼ���¸��*/
	public void beginRefreshLyric() {	
		if(isLyric)
			lyricUpdateHandler.postDelayed(lyricUpdateThread, 5);
	}
	
	/**��ͣ���¸��*/
	public void pauseRefreshLyric() {
		if(isLyric) {
			isPauseRefreshLyric = true;
			lyricUpdateHandler.removeCallbacks(lyricUpdateThread);
		}
	}	
	
	/**ֹͣ���¸��*/
	public void stopRefreshLyric() {		
		if(isLyric) {
			lyricUpdateHandler.removeCallbacks(lyricUpdateThread);
		}			
	}	
	
	@Override
	protected void onDraw(Canvas canvas) {		
		super.onDraw(canvas);
		
		if(index == -3) {
			canvas.drawText("��������,һ·����", 0, height/2, currentPaint);
		}
		
		if(index == -2) {
			canvas.drawText("�����������...", 0, height/2 + 80, currentPaint);
		}
		
		if(index == 10000) {
			canvas.drawText("��������,һ·����", 0, height/2, currentPaint);
		}
		
		if(lyricInfos != null && lyricInfos.size() > 0){
			try {
				float plus = lyricInfos.get(index).getDuration() == 0 ? 10: 10 + (((float)
						MusicPlayer.getPlayer().getCurrentPosition() - (float)lyricInfos.
						get(index).getTime())/(float)lyricInfos.get(index).getDuration())*(float)20;			
				canvas.translate(0, -plus);							
				
				// �����м�һ����
				String sentence = null;
				int flag = 0;
				float size = currentPaint.measureText(lyricInfos.get(index).getLyric());    											
				if(size > width - 80) {
					sentence = lyricInfos.get(index).getLyric();
					canvas.drawText(sentence.substring(0, sentence.length()*2/3), 0 ,height / 2, currentPaint);																			
					flag = 1;
					sentence = lyricInfos.get(index).getLyric();
					canvas.drawText(sentence.substring(sentence.length()*2/3).trim(), 0 ,height / 2 + lyricSpace, currentPaint);
				} else {
					canvas.drawText(lyricInfos.get(index).getLyric(), 0 ,height / 2, currentPaint);	
				}						
																	
				// ��������֮ǰ�ĸ��
				float tempY = height / 2;	
				for (int i = index - 1; i >= 0; i--) {
					size = notCurrentPaint.measureText(lyricInfos.get(i).getLyric());
					if(size > width - 80) {						
						tempY = tempY - lyricSpace;
						sentence = lyricInfos.get(i).getLyric();
						canvas.drawText(sentence.substring(sentence.length()*2/3).trim(), 0, tempY, notCurrentPaint);
						tempY = tempY - lyricSpace;
						sentence = lyricInfos.get(i).getLyric();
						canvas.drawText(sentence.substring(0, sentence.length()*2/3), 0, tempY, notCurrentPaint);
					} else {
						tempY = tempY - lyricSpace;
						canvas.drawText(lyricInfos.get(i).getLyric(), 0, tempY, notCurrentPaint);	
					}										
				}
				
				// ��������֮��ĸ��
				tempY = height / 2;
				if(flag == 1) {
					tempY = tempY + lyricSpace;
				}				
				for (int i = index + 1; i < lyricInfos.size(); i++) {
					size = notCurrentPaint.measureText(lyricInfos.get(i).getLyric());
					if(size > width - 80) {
						tempY = tempY + lyricSpace;
						sentence = lyricInfos.get(i).getLyric();
						canvas.drawText(sentence.substring(0, sentence.length()*2/3), 0, tempY, notCurrentPaint);						
						tempY = tempY + lyricSpace;
						sentence = lyricInfos.get(i).getLyric();
						canvas.drawText(sentence.substring(sentence.length()*2/3).trim(), 0, tempY, notCurrentPaint);
					} else {
						tempY = tempY + lyricSpace;
						canvas.drawText(lyricInfos.get(i).getLyric(), 0, tempY, notCurrentPaint);	
					}														
				}
			} catch (Exception e) {
				System.out.println("��ʾ��ʳ�������" + e.getMessage());
			}
		}	
	}
	
	//��дview��onSizeChanged���÷�������onCreate֮��onDraw֮ǰ����
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d(TAG, "onSizeChanged,w="+w+",h="+h+", oldw="+oldw+",oldh="+oldh);  
		width  = w;
		height = h;
	}
	
	public boolean isPauseRefreshLyric() {
		return isPauseRefreshLyric;
	}	
	
	/**���ظ���ͼƬ*/
	private void loadNextSingerImg() {
		Log.d(TAG, "���ڼ�����һ��ͼƬ......");
		LoadImageThread loadImage = new LoadImageThread(currentMp3Info, context, true);
		loadImage.start();			
		LoadImageBroadcastReceiver imageReceiver = new LoadImageBroadcastReceiver(loadImage);								
		context.registerReceiver(imageReceiver, imageReceiver.getIntentFilter());																						 
	}
	
	public void setCurrentMp3Info(Mp3Info currentMp3Info) {
		this.currentMp3Info = currentMp3Info;
	}

	@Override
	public boolean onLongClick(View v) {
		loadNextSingerImg();	
		return true;
	}
}
