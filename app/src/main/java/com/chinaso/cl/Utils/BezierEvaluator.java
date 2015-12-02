package com.chinaso.cl.Utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class BezierEvaluator implements TypeEvaluator<PointF> {


	private int SX,SY;
	private int DX,DY;
	public BezierEvaluator(int sx,int sy,int dx,int dy){
		SX=sx;
		SY=sy;
		DX=dx;
		DY=dy;
	}
	@Override
	public PointF evaluate(float fraction, PointF startValue,
			PointF endValue) {
		final float t = fraction;
		float oneMinusT = 1.0f - t;
		PointF point = new PointF();

		PointF point0 = (PointF)startValue;

		PointF point1 = new PointF();
		point1.set(SX+50, (SY+DY)/2);

		PointF point2 = new PointF();
		point2.set(SX-50, (SY+DY)/2);

		PointF point3 = (PointF)endValue;

		point.x = oneMinusT * oneMinusT * oneMinusT * (point0.x)
				+ 3 * oneMinusT * oneMinusT * t * (point1.x)
				+ 3 * oneMinusT * t * t * (point2.x)
				+ t * t * t * (point3.x);

		point.y = oneMinusT * oneMinusT * oneMinusT * (point0.y)
				+ 3 * oneMinusT * oneMinusT * t * (point1.y)
				+ 3 * oneMinusT * t * t * (point2.y)
				+ t * t * t * (point3.y);
		return point;
	}
}