package recorder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.cl.R;
import com.chinaso.cl.Utils.RongUtil;
import com.lecloud.common.base.util.Logger;
import com.lecloud.skin.PlayerStateCallback;
import com.lecloud.skin.actionlive.MultLivePlayCenter;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class PlayActivity extends Activity implements RongIMClient.OnReceiveMessageListener{
	private RelativeLayout mPlayerLayoutView;
	private MultLivePlayCenter mPlayerView;

	private Button mMsgLike;
	private Button mMsgText;

	private TextView msg_number_text;
	private TextView msg_count_text;

	private int msg_count=0;
	private int msg_number=0;

	private boolean isEntered=false;

	private static String mActivityId = "";
	private boolean isHLS;
	private boolean isBackgroud = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.vedio_layout);

		Intent intent = getIntent();
		mActivityId = intent.getStringExtra("activityID");
		isHLS = intent.getBooleanExtra("isHLS", false);


		RongIMClient.setOnReceiveMessageListener(PlayActivity.this);
		RongUtil.joinChatRoom(mActivityId);
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (this.mPlayerView != null) {
			if (isBackgroud) {
				if(mPlayerView.getCurrentPlayState() == PlayerStateCallback.PLAYER_VIDEO_PAUSE){
//	        		this.mPlayerView.resumeVideo();
	        	}else{
	        		Logger.e("LiveActivity", "已回收，重新请求播放");
	        	}
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.mPlayerView != null) {
			isBackgroud = true;
		}
	}


	@Override
	protected void onDestroy() {
		this.mPlayerView.destroyVideo();
		this.mPlayerLayoutView.removeAllViews();
		super.onDestroy();
		isBackgroud = false;

		msg_number--;
		RongUtil.quitChatRoom(mActivityId,msg_count,msg_number);
	}

	private void initView(){
		msg_count_text= (TextView) findViewById(R.id.msg_count);
		msg_number_text= (TextView) findViewById(R.id.msg_number);
		this.mMsgLike = (Button) this.findViewById(R.id.msg_like);
		mMsgText = (Button) this.findViewById(R.id.msg_text);
		mMsgText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RongUtil.sendTextMessage("hello", mActivityId);
			}
		});
		this.mMsgLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				msg_count++;
				RongUtil.sendLikeMessage(msg_count,msg_number,mActivityId);
			}
		});

		this.mPlayerLayoutView = (RelativeLayout) this.findViewById(R.id.layout_player);

		Display display=getWindowManager().getDefaultDisplay();
		int height=display.getHeight();
		int width=display.getWidth();
		Log.i("ly", "height= " + height + " width= " + width);
		mPlayerView = new MyMultLivePlayCenter(this, false,width,height);
		mPlayerView.setRelease(false);
		mPlayerView.isShowSubLiveView(false);

		this.mPlayerLayoutView.addView(this.mPlayerView.getPlayerView());

		mPlayerView.playAction(mActivityId);
//		mPlayerView.changeOrientation(Configuration.ORIENTATION_LANDSCAPE);
		mPlayerView.setPlayerStateCallback(new PlayerStateCallback() {

			@Override
			public void onStateChange(int state, Object... extra) {
				if(state == PlayerStateCallback.PLAYER_VIDEO_PLAY){
//					mPlayerView.setVisiableActiveSubLiveView(true);
				}
			}
		});
	}
	@Override
	public boolean onReceived(Message message, int i) {
		MessageContent messageContent = message.getContent();


		if (messageContent instanceof TextMessage) {//文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d("ly", "onReceived-TextMessage:" + textMessage.getContent());
			//msg_count.setText(textMessage.getContent());
			try {
				JSONObject ret=new JSONObject(textMessage.getContent());
				msg_count=ret.getInt("count");
				msg_number=ret.getInt("number");

				msg_count_text.setText("点赞数："+msg_count);
				msg_number_text.setText("人数："+msg_number);
				if(!isEntered){
					msg_number++;
					RongUtil.sendEnterMessage(msg_count,msg_number,mActivityId);
					isEntered=true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.d("ly", "onReceived-其他消息，自己来判断处理");
		}

		return false;
	}

}
