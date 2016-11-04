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

	// ������ɫ
	private static final int WAVE_PAINT_COLOR = 0x880000aa;

	// ��һ�������ƶ����ٶ�
	private int oneSeep = 7;

	// �ڶ��������ƶ����ٶ�
	private int twoSeep = 10;

	// ��һ�������ƶ��ٶȵ�����ֵ
	private int oneSeepPxil;
	// �ڶ��������ƶ��ٶȵ�����ֵ
	private int twoSeepPxil;

	// ���ԭʼ���Ƶ�ÿ��y�����
	private float wave[];

	// ��ŵ�һ�����Ƶ�ÿһ��y�����
	private float oneWave[];

	// ��ŵڶ������Ƶ�ÿһ��y�����
	private float twoWave[];

	// ��һ�����Ƶ�ǰ�ƶ��ľ���
	private int oneNowOffSet;
	// �ڶ������Ƶ�ǰ�ƶ���
	private int twoNowOffSet;

	// ����߶�
	private int amplitude = 20;

	// ����
	private Paint mPaint;

	// ������������
	private DrawFilter mDrawFilter;

	// view�Ŀ��
	private int viewWidth;

	// view�߶�
	private int viewHeight;

	// xml���ֹ��췽��
	public MyUi2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	// ��ʼ��
	private void init() {
		// ��������
		mPaint = new Paint();
		// ���û�����ɫ
		mPaint.setColor(WAVE_PAINT_COLOR);
		// ���û滭���Ϊʵ��
		mPaint.setStyle(Style.FILL);
		// �����
		mPaint.setAntiAlias(true);
		// ����ͼƬ���˲��Ϳ����
		mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

		// ��һ�����������ƶ�ֵ
		oneSeepPxil = dpChangPx(oneSeep);

		// �ڶ������������ƶ�ֵ
		twoSeepPxil = dpChangPx(twoSeep);
	}

	// �滭����
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
		// one��ָ �ߵ��˴��Ĳ��Ƶ�λ�� (�����ⷽ����������)
		int one = viewWidth - oneNowOffSet;
		// ��δ�߹��Ĳ��Ʒŵ���ǰ�� ��������ƴ��
		System.arraycopy(wave, oneNowOffSet, oneWave, 0, one);
		// �����߲��Ʒŵ����
		System.arraycopy(wave, 0, oneWave, one, oneNowOffSet);

		// one��ָ �ߵ��˴��Ĳ��Ƶ�λ�� (�����ⷽ����������)
		int two = viewWidth - twoNowOffSet;
		// ��δ�߹��Ĳ��Ʒŵ���ǰ�� ��������ƴ��
		System.arraycopy(wave, twoNowOffSet, twoWave, 0, two);
		// �����߲��Ʒŵ����
		System.arraycopy(wave, 0, twoWave, two, twoNowOffSet);
		
		
	}

	// ��С�ı�
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// ��ȡview�Ŀ��
		viewHeight = h;
		viewWidth = w;

		// ��ʼ�����沨��ͼ������
		wave = new float[w];
		oneWave = new float[w];
		twoWave = new float[w];

		// ���ò���ͼ����
		float zq = (float) (Math.PI * 2 / w);

		// ���ò���ͼ������
		for (int i = 0; i < viewWidth; i++) {
			wave[i] = (float) (amplitude * Math.sin(zq * i));
		}
		

	}

	// dp�����px Ϊ�����ƶ��ٶ��ڸ����ֱ��ʵ��ֻ��Ķ����
	public int dpChangPx(int dp) {
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
		return (int) (metrics.density * dp + 0.5f);
	}

}
