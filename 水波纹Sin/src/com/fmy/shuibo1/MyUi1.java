package com.fmy.shuibo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.TimeZoneFormat.ParseOption;
import android.util.AttributeSet;
import android.view.View;

public class MyUi1 extends View{

	
	//����ͼ
	Bitmap waveBitmap;
	
	//Բ������ͼ
	Bitmap circleBitmap;
	
	//����ͼsrc
	Rect waveSrcRect;
	//����ͼdst
	Rect waveDstRect;
	
	//Բ������src
	Rect circleSrcRect;

	//Բ������dst
	Rect circleDstRect;
	
	//����
	Paint mpaint;
	
	//ͼƬ����ģʽ
	PorterDuffXfermode mode;
	
	//�ؼ��Ŀ�
	int viewWidth;
	//�ؼ��ĸ�
	int viewHeight;
	
	//ͼƬ������
	PaintFlagsDrawFilter paintFlagsDrawFilter ;
	
	//ÿ���ƶ��ľ���
	int speek = 10 ;
	
	//��ǰ�ƶ�����
	int nowOffSet;
	
	public MyUi1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
	}
	
	//��ʼ��
	private void init() {
		
		mpaint = new Paint();
		//����ͼƬ����
		mpaint.setDither(true);
		//�����
		mpaint.setAntiAlias(true);
		//����ͼƬ���˲�
		mpaint.setFilterBitmap(true);
		//����ͼƬ����ģʽ
		mode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
		
		//������ֱ���趨����
		paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		
		//��ʼ��ͼƬ
		//ʹ��drawable��ȡ�ķ�ʽ��ȫ��ֻ������һ�ݣ�����ϵͳ����й���
		//��BitmapFactory.decode()��������decode���ٴ����ɶ����ţ�����Լ�����recycle��
		
		//��ȡ����ͼ
		waveBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.wave_2000)).getBitmap();
		
		//��ȡԲ������ͼ
		circleBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.circle_500)).getBitmap();
		
		//����ˢ�²���ͼ���� ���߿����Ȳ����ⲿ������  ��Ϊ��Ҫ�����������
		new Thread(){
			public void run() {
				while (true) {
					try {
						//�ƶ�����ͼ
						nowOffSet=nowOffSet+speek;
						//����ƶ�����ͼ��ĩβ��ô������
						if (nowOffSet>=waveBitmap.getWidth()) {
							nowOffSet=0;
						}
						sleep(30);
						postInvalidate();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			};
		}.start();
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//��ͼƬֱ�����ù���Ч��
		canvas.setDrawFilter(paintFlagsDrawFilter);
		//��ͼƬ��ɫ
		canvas.drawColor(Color.TRANSPARENT);
		//���ͼ�� ע��!!!!!ʹ��ͼƬ����ģʽ��Ӱ��ȫ����ͼ��(Ҳ����˵��canvas.restoreToCount ����ͼ�����ܵ�Ӱ��) 
		int saveLayer = canvas.saveLayer(0,0, viewWidth,viewHeight,null, Canvas.ALL_SAVE_FLAG);
		//������ͼ���� ����
		waveSrcRect.set(nowOffSet, 0, nowOffSet+viewWidth/2, viewHeight);
		//������
		canvas.drawBitmap(waveBitmap,waveSrcRect,waveDstRect,mpaint);
		//����ͼƬ����ģʽ
		mpaint.setXfermode(mode);
		//������
		canvas.drawBitmap(circleBitmap, circleSrcRect, circleDstRect,mpaint);
		//��ԭ����ģʽ
		mpaint.setXfermode(null);
		//��ͼ�����
		canvas.restoreToCount(saveLayer);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//��ȡview���
		viewWidth = w;
		viewHeight = h ;
		
		//����ͼ�ľ����ʼ��
		waveSrcRect = new Rect();
		waveDstRect = new Rect(0,0,w,h);
		
		//Բ������ʼ��
		circleSrcRect = new Rect(0,0,circleBitmap.getWidth(),circleBitmap.getHeight());
		
		circleDstRect = new Rect(0,0,viewWidth,viewHeight);

		
	}
}
