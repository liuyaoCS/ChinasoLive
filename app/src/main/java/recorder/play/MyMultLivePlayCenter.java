package recorder.play;

import android.content.Context;
import android.view.View;

import com.lecloud.skin.actionlive.MultLivePlayCenter;

/**
 * Created by rmss on 2015/11/20.
 */
public class MyMultLivePlayCenter extends MultLivePlayCenter{
    private Context mContext;
    public MyMultLivePlayCenter(Context context) {
        super(context);
    }

    public MyMultLivePlayCenter(Context context, boolean b) {
        super(context, b);
        if(!b) {
            this.mLoadingLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param context
     * @param hasSkin 是否带皮肤
     * @param width   播放器宽度
     * @param height  播放器高度
     */
    public MyMultLivePlayCenter(Context context, boolean hasSkin, int width, int height) {
        super(context, hasSkin, width, height);
        mContext=context;
        if(!hasSkin) {
            this.mLoadingLayout.setVisibility(View.VISIBLE);
        }
    }

    public MyMultLivePlayCenter(Context context, boolean b, boolean b1) {
        super(context, b, b1);
        if(!b) {
            this.mLoadingLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param context
     * @param b   是否带皮肤
     * @param b1
     * @param i   播放器宽度
     * @param i1  播放器高度
     * @param i2  根据播放器尺寸设置surfaceView的分辨率
     */
    public MyMultLivePlayCenter(Context context, boolean b, boolean b1, int i, int i1, int i2) {
        super(context, b, b1, i, i1, i2);
        if(!b) {
            this.mLoadingLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showNotice(int i) {
        super.showNotice(i);

        this.errorView.a(i);
    }

    @Override
    protected void hideNotice() {
        super.hideNotice();

        //this.errorView.b();
    }
}
