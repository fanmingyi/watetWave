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

	
	//波形图
	Bitmap waveBitmap;
	
	//圆形遮罩图
	Bitmap circleBitmap;
	
	//波形图src
	Rect waveSrcRect;
	//波形图dst
	Rect waveDstRect;
	
	//圆形遮罩src
	Rect circleSrcRect;

	//圆形遮罩dst
	Rect circleDstRect;
	
	//画笔
	Paint mpaint;
	
	//图片遮罩模式
	PorterDuffXfermode mode;
	
	//控件的宽
	int viewWidth;
	//控件的高
	int viewHeight;
	
	//图片过滤器
	PaintFlagsDrawFilter paintFlagsDrawFilter ;
	
	//每次移动的距离
	int speek = 10 ;
	
	//当前移动距离
	int nowOffSet;
	
	public MyUi1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
	}
	
	//初始化
	private void init() {
		
		mpaint = new Paint();
		//处理图片抖动
		mpaint.setDither(true);
		//抗锯齿
		mpaint.setAntiAlias(true);
		//设置图片过滤波
		mpaint.setFilterBitmap(true);
		//设置图片遮罩模式
		mode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
		
		//给画布直接设定参数
		paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		
		//初始化图片
		//使用drawable获取的方式，全局只会生成一份，并且系统会进行管理，
		//而BitmapFactory.decode()出来的则decode多少次生成多少张，务必自己进行recycle；
		
		//获取波形图
		waveBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.wave_2000)).getBitmap();
		
		//获取圆形遮罩图
		circleBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.circle_500)).getBitmap();
		
		//不断刷新波形图距离 读者可以先不看这部分内容  因为需要结合其他方法
		new Thread(){
			public void run() {
				while (true) {
					try {
						//移动波形图
						nowOffSet=nowOffSet+speek;
						//如果移动波形图的末尾那么重新来
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
		
		//给图片直接设置过滤效果
		canvas.setDrawFilter(paintFlagsDrawFilter);
		//给图片上色
		canvas.drawColor(Color.TRANSPARENT);
		//添加图层 注意!!!!!使用图片遮罩模式会影响全部此图层(也就是说在canvas.restoreToCount 所有图都会受到影响) 
		int saveLayer = canvas.saveLayer(0,0, viewWidth,viewHeight,null, Canvas.ALL_SAVE_FLAG);
		//画波形图部分 矩形
		waveSrcRect.set(nowOffSet, 0, nowOffSet+viewWidth/2, viewHeight);
		//画矩形
		canvas.drawBitmap(waveBitmap,waveSrcRect,waveDstRect,mpaint);
		//设置图片遮罩模式
		mpaint.setXfermode(mode);
		//画遮罩
		canvas.drawBitmap(circleBitmap, circleSrcRect, circleDstRect,mpaint);
		//还原画笔模式
		mpaint.setXfermode(null);
		//将图层放上
		canvas.restoreToCount(saveLayer);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//获取view宽高
		viewWidth = w;
		viewHeight = h ;
		
		//波形图的矩阵初始化
		waveSrcRect = new Rect();
		waveDstRect = new Rect(0,0,w,h);
		
		//圆球矩阵初始化
		circleSrcRect = new Rect(0,0,circleBitmap.getWidth(),circleBitmap.getHeight());
		
		circleDstRect = new Rect(0,0,viewWidth,viewHeight);

		
	}
}
