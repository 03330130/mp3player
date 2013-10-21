package com.music.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.WindowManager;

/**����ͼƬ������*/
public class BitmapHandle {	
	
	/**��ɫ����*/
	private ColorMatrix colorMatrix = null;		
	
	/**lightValue��ֵ��Χ��0-2f������1��ʾ���������С��1f��ͼƬ���䰵����֮��Ȼ*/	 
	private float lightValue = 0.64f;
	
	/**�豸����Ļ���*/
	private int deviceWidth; 
	/**�豸����Ļ�߶�*/
	private int deviceHeight;
	/**�豸�ı�������/��*/
	private float deviceRatio;
	
	private final static int WIDTH = 320;
	private final static int HEIGHT = 480;
		
	public BitmapHandle(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		deviceWidth = manager.getDefaultDisplay().getWidth();
		deviceHeight = manager.getDefaultDisplay().getHeight();
		deviceRatio = (float)deviceHeight / deviceWidth;
	}
	
	/**
	 * ����ͼƬ������
	 * @param sourceBitmap ԭͼƬ��bitmap
	 * @return ����һ�������������ȴ����bitmap, sourceBitmapΪ��ʱ����null��
	 */
	private Bitmap reducePicLight(Bitmap sourceBitmap) {
		if(sourceBitmap == null) {
			return null;
		}
		Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		// ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
		Canvas canvas = new Canvas(newBitmap); // �õ����ʶ���
		Paint paint = new Paint(); // �½�paint
		paint.setAntiAlias(true); // ���ÿ����,Ҳ���Ǳ�Ե��ƽ������

		colorMatrix = new ColorMatrix(); // ������ɫ�任�ľ���androidλͼ��ɫ�仯������Ҫ�ǿ��ö������		
		colorMatrix.reset(); // ��ΪĬ��ֵ
		colorMatrix.setScale(lightValue, lightValue, lightValue, 1);
		
		paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));// ������ɫ�任Ч��
		canvas.drawBitmap(sourceBitmap, 0, 0, paint); // ����ɫ�仯���ͼƬ������´�����λͼ��
		return newBitmap;	
	}
	
	/**
	 * ��ԭͼƬ�ü���һ��С�������θ���ͼƬ
	 * @param sourceBitmap ԭ��׼����ͼƬ����Ļ��Сһ�µ�Bitmap��
	 * @return һ��С�������θ���ͼƬ��sourceBitmapΪ��ʱ����null
	 */
	public Bitmap clipMiniPic(Bitmap sourceBitmap) {
		if(sourceBitmap == null) {
			return null;
		}
		return Bitmap.createBitmap(sourceBitmap, 0, 0, 
				sourceBitmap.getWidth(), sourceBitmap.getWidth());
	}
	
	/**
	 * �����غ��ͼƬ������С���ü�ʹ��ͼƬ��С����Ļ�Ĵ�Сһ��
	 * ���غ���Ļ��Сһ�µ�Bitmap��sourceBitmapΪ��ʱ����null
	 */
	public Bitmap clipBigPic(Bitmap sourceBitmap) {		
		if(sourceBitmap == null) {
			return null;
		}
		Bitmap bitmap = null;
		if(sourceBitmap.getWidth() >= WIDTH) {
			Matrix matrix = new Matrix();    
			float scaleWidth = ((float) WIDTH / sourceBitmap.getWidth());       	     
			matrix.postScale(scaleWidth, scaleWidth);       
			bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
					sourceBitmap.getHeight(), matrix, true);			
		} 	
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
	}
	
	/**�ü�ͼƬʹ�ú���Ļ�ֱ��ʵı���һ��*/
	public Bitmap clipPlayBgPic(Bitmap sourceBitmap) {
		if(sourceBitmap == null) {
			return null;
		}
		float picRatio = (float)sourceBitmap.getHeight() / sourceBitmap.getWidth();
		Bitmap bitmap = null;
		if(deviceRatio < picRatio) {
			bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), (int)(WIDTH * deviceRatio));
		} else if(deviceRatio > picRatio) {
			int width = (int)(sourceBitmap.getHeight()/deviceRatio);
			bitmap = Bitmap.createBitmap(sourceBitmap, (WIDTH - width)/2, 0, width, sourceBitmap.getHeight());
		} else {
			bitmap = sourceBitmap;
		}		
		return reducePicLight(bitmap);
	}
	
	/**�����豸����Ļ���*/
	public int getDeviceWidth() {
		return deviceWidth;
	}
	
	/**�����豸����Ļ�߶�*/
	public int getDeviceHeight() {
		return deviceHeight;
	}	
}
