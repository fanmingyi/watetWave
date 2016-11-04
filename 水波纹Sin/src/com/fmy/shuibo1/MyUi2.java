package com.fmy.shuibo1;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.TimeZoneFormat.ParseOption;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MyUi2 extends View {

	// 波纹颜色
	private static final int WAVE_PAINT_COLOR = 0x880000aa;

	// 第一个波纹移动的速度
	private int oneSeep = 7;

	// 第二个波纹移动的速度
	private int twoSeep = 10;

	// 第一个波纹移动速度的像素值
	private int oneSeepPxil;
	// 第二个波纹移动速度的像素值
	private int twoSeepPxil;

	// 存放原始波纹的每个y坐标点
	private float wave[];

	// 存放第一个波纹的每一个y坐标点
	private float oneWave[];

	// 存放第二个波纹的每一个y坐标点
	private float twoWave[];

	// 第一个波纹当前移动的距离
	private int oneNowOffSet;
	// 第二个波纹当前移动的
	private int twoNowOffSet;

	// 振幅高度
	private int amplitude = 20;

	// 画笔
	private Paint mPaint;

	// 创建画布过滤
	private DrawFilter mDrawFilter;

	// view的宽度
	private int viewWidth;

	// view高度
	private int viewHeight;

	// xml布局构造方法
	public MyUi2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	// 初始化
	private void init() {
		// 创建画笔
		mPaint = new Paint();
		// 设置画笔颜色
		mPaint.setColor(WAVE_PAINT_COLOR);
		// 设置绘画风格为实线
		mPaint.setStyle(Style.FILL);
		// 抗锯齿
		mPaint.setAntiAlias(true);
		// 设置图片过滤波和抗锯齿
		mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

		// 第一个波的像素移动值
		oneSeepPxil = dpChangPx(oneSeep);

		// 第二个波的像素移动值
		twoSeepPxil = dpChangPx(twoSeep);
	}

	// 绘画方法
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(mDrawFilter);
		
		oneNowOffSet =oneNowOffSet+oneSeepPxil;
		
		twoNowOffSet = twoNowOffSet+twoSeepPxil;
		
		if (oneNowOffSet>=viewWidth) {
			oneNowOffSet = 0;
		}
		if (twoNowOffSet>=viewWidth) {
			twoNowOffSet = 0;
		}
		reSet();
		
		Log.e("fmy", Arrays.toString(twoWave));
		
		for (int i = 0; i < viewWidth; i++) {
			
			canvas.drawLine(i, viewHeight, i, viewHeight-400-oneWave[i], mPaint);
			canvas.drawLine(i, viewHeight, i, viewHeight-400-twoWave[i], mPaint);
		}
		
		postInvalidate();
	}

	public void reSet() {
		// one是指 走到此处的波纹的位置 (这个理解方法看个人了)
		int one = viewWidth - oneNowOffSet;
		// 把未走过的波纹放到最前面 进行重新拼接
		System.arraycopy(wave, oneNowOffSet, oneWave, 0, one);
		// 把已走波纹放到最后
		System.arraycopy(wave, 0, oneWave, one, oneNowOffSet);

		// one是指 走到此处的波纹的位置 (这个理解方法看个人了)
		int two = viewWidth - twoNowOffSet;
		// 把未走过的波纹放到最前面 进行重新拼接
		System.arraycopy(wave, twoNowOffSet, twoWave, 0, two);
		// 把已走波纹放到最后
		System.arraycopy(wave, 0, twoWave, two, twoNowOffSet);
		
		
	}

	// 大小改变
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 获取view的宽高
		viewHeight = h;
		viewWidth = w;

		// 初始化保存波形图的数组
		wave = new float[w];
		oneWave = new float[w];
		twoWave = new float[w];

		// 设置波形图周期
		float zq = (float) (Math.PI * 2 / w);

		// 设置波形图的周期
		for (int i = 0; i < viewWidth; i++) {
			wave[i] = (float) (amplitude * Math.sin(zq * i));
		}
		

	}

	// dp换算成px 为了让移动速度在各个分辨率的手机的都差不多
	public int dpChangPx(int dp) {
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
		return (int) (metrics.density * dp + 0.5f);
	}

}
