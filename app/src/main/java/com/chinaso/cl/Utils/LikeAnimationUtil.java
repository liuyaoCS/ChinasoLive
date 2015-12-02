package com.chinaso.cl.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chinaso.cl.R;

import java.util.Random;

/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class LikeAnimationUtil {

    public static int ANIM_DURATION=1000;
    public static int[] heartIds=new int[]{R.mipmap.heart0,R.mipmap.heart1,R.mipmap.heart2,
            R.mipmap.heart3,R.mipmap.heart4,R.mipmap.heart5,
            R.mipmap.heart6,R.mipmap.heart7,R.mipmap.heart8,
            R.mipmap.heart9,R.mipmap.heart10,R.mipmap.heart11};

    /**
     *
     */
    public static void excuteAnimation(Context context,final ViewGroup container,int viewId,
                                 final int SX,final int SY,final int DX,final int DY) {
        final ImageView iv = new ImageView(context);
        Random random = new Random();
        int index = random.nextInt(heartIds.length);
        iv.setImageDrawable(context.getResources().getDrawable(heartIds[index]));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_RIGHT, viewId);
        lp.addRule(RelativeLayout.ALIGN_BOTTOM, viewId);
        container.addView(iv, lp);

        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(SX,SY,DX,DY), new PointF(SX, SY), new PointF(DX, DY));
        valueAnimator.setDuration(ANIM_DURATION);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                Log.i("ly", "pointF-->" + pointF.x + "," + pointF.y);

                iv.setX(pointF.x);
                iv.setY(pointF.y);
                iv.setAlpha((pointF.y - DY) / (SY - DY));
                iv.setScaleX(((pointF.y - DY) / (SY - DY)+1)/2);
                iv.setScaleY(((pointF.y - DY) / (SY - DY)+1)/2);

            }

        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container.removeView(iv);
                iv.setAlpha(0);
            }
        });
        valueAnimator.start();
    }
}
