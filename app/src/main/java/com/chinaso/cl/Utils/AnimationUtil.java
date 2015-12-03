package com.chinaso.cl.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaso.cl.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class AnimationUtil {
    //评论动画
    private static final int COLLAPSE_ANIMATION_DURATION =1000;
    private static final int COMMENT_VIEW_NUM=5;
    //点赞动画
    private static int ContainerBaseViewNumber=-1;
    public static int CLICK_ANIMATION_DURATION =1000;
    private static final int LIKE_VIEW_NUM=20;
    private static Map<ImageView,ValueAnimator> map=new HashMap<ImageView,ValueAnimator>();
    public static int[] heartIds=new int[]{R.mipmap.heart0,R.mipmap.heart1,R.mipmap.heart2,
            R.mipmap.heart3,R.mipmap.heart4,R.mipmap.heart5,
            R.mipmap.heart6,R.mipmap.heart7,R.mipmap.heart8,
            R.mipmap.heart9,R.mipmap.heart10,R.mipmap.heart11};


    public static void executeLikeAnimation(Context context, final ViewGroup container, int viewId,
                                            final int SX, final int SY, final int DX, final int DY) {

        if(ContainerBaseViewNumber==-1){
            ContainerBaseViewNumber = container.getChildCount();
        }

        final ImageView iv;
        ValueAnimator valueAnimator;
        if(container.getChildCount()>=ContainerBaseViewNumber+LIKE_VIEW_NUM){
            iv= (ImageView) container.getChildAt(ContainerBaseViewNumber);
            valueAnimator=map.get(iv);
            valueAnimator.cancel();
            container.removeViewAt(ContainerBaseViewNumber);
            Log.i("ly","reuse resource");
        }else{
            iv=new ImageView(context);
            valueAnimator = ValueAnimator.ofObject(
                    new BezierEvaluator(SX,SY,DX,DY), new PointF(SX, SY), new PointF(DX, DY));
        }

        iv.setX(SX);
        iv.setY(SY);
        iv.setAlpha(1.0f);
        iv.setScaleX(1);
        iv.setScaleY(1);

        Random random = new Random();
        int index = random.nextInt(heartIds.length);
        iv.setImageDrawable(context.getResources().getDrawable(heartIds[index]));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_RIGHT, viewId);
        lp.addRule(RelativeLayout.ALIGN_BOTTOM, viewId);
        container.addView(iv, lp);

        valueAnimator.setDuration(CLICK_ANIMATION_DURATION);
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
                //container.removeView(iv);
                iv.setAlpha(0.0f);
            }
        });
        valueAnimator.start();

        map.put(iv,valueAnimator);
    }
    public static void executeCommentCollapseAnimation(final ViewGroup container){
        ObjectAnimator animTrans=ObjectAnimator.ofFloat(container, "translationY", 0,0);
        ObjectAnimator animAlpha=ObjectAnimator.ofFloat(container, "alpha", 1.0f,0f);

        AnimatorSet animSet=new AnimatorSet();
        animSet.playTogether(animTrans, animAlpha);
        animSet.setDuration(COLLAPSE_ANIMATION_DURATION);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container.clearAnimation();
            }
        });
        animSet.start();
    }
    public static void addCommentItemView(Context context,final ViewGroup container,String content){
        TextView tv;
        if(container.getChildCount()==COMMENT_VIEW_NUM){
            tv= (TextView) container.getChildAt(0);
            container.removeViewAt(0);
        }else{
            tv=new TextView(context);
        }

        tv.setPadding(DisplayUtil.Dp2Px(context,3),DisplayUtil.Dp2Px(context,3),DisplayUtil.Dp2Px(context,3),DisplayUtil.Dp2Px(context,3));
        tv.setBackgroundColor(Color.parseColor("#80f0f0f0"));
        tv.setText(content);
        tv.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(DisplayUtil.Dp2Px(context,40), DisplayUtil.Dp2Px(context,3), 0, DisplayUtil.Dp2Px(context,3));

        container.addView(tv,lp);
        container.setAlpha(1.0f);
    }
}
