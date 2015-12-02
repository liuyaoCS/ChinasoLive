package recorder.play;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaso.cl.R;
import com.chinaso.cl.Utils.LikeAnimationUtil;
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

	private EditText mMsgEdit;
	private Button mMsgSend;

	private TextView mMsgShow;
	private ImageView mMsgLike;

	private TextView msg_number_text;
	private TextView msg_count_text;


	private int msg_count=0;
	private int msg_number=0;

	private boolean isEntered=false;

	private static String mActivityId = "";
	private boolean isHLS;
	private boolean isBackgroud = false;

	private int SX, SY;
	private int DX ,DY;
	public static int DIS_X=60;
	public static int DIS_Y=600;
	public static int DELAY=500;

	private RelativeLayout container;

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
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				int[] location = new int[2];
				mMsgLike.getLocationOnScreen(location);
				int x = location[0];
				int y = location[1];
				SX = x;
				SY = y;
				DX=SX;
				DY=SY-DIS_Y;
				Log.i("ly","SX,SY = "+SX+","+SY);
			}
		}, DELAY);
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
		RongUtil.quitChatRoom(mActivityId, msg_count, msg_number);
	}

	private void initView(){
		container= (RelativeLayout) findViewById(R.id.container);
		msg_count_text= (TextView) findViewById(R.id.msg_count);
		msg_number_text= (TextView) findViewById(R.id.msg_number);
		mMsgShow= (TextView) findViewById(R.id.msg_text_show);
		mMsgLike = (ImageView) this.findViewById(R.id.msg_like);
		mMsgEdit= (EditText) findViewById(R.id.msg_edit);

		mMsgSend = (Button) this.findViewById(R.id.msg_send);
		mMsgSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMsgShow.setText("我:"+mMsgEdit.getText().toString());
				RongUtil.sendTextMessage(msg_count, msg_number, mMsgEdit.getText().toString(), mActivityId);
			}
		});
		this.mMsgLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				msg_count++;
				RongUtil.sendLikeMessage(msg_count,msg_number,mActivityId,new RongIMClient.SendMessageCallback() {
					@Override
					public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
						Log.e("ly", "onError errorCode-->" + errorCode);
					}

					@Override
					public void onSuccess(Integer integer) {
						Log.i("ly", "onSuccess message-->" + integer);
						PlayActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								LikeAnimationUtil.excuteAnimation(PlayActivity.this, container, R.id.msg_like,
										SX, SY, DX, DY);
								msg_count_text.setText("点赞数："+msg_count);
							}
						});
					}
				});
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
			final TextMessage textMessage = (TextMessage) messageContent;
			Log.d("ly", "onReceived-TextMessage:" + textMessage.getContent());
			//msg_count.setText(textMessage.getContent());
			try {
				final JSONObject ret=new JSONObject(textMessage.getContent());
				msg_count=ret.getInt("count");
				msg_number=ret.getInt("number");
				final String type=ret.getString("type");

				PlayActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						msg_count_text.setText("点赞数：" + msg_count);
						msg_number_text.setText("人数：" + msg_number);
						if(type.equals("Text")){
							String name=textMessage.getUserInfo().getName();
							if(TextUtils.isEmpty(name)){
								name="匿名";
							}
							mMsgShow.setText(name+":"+ret.optString("message"));
						}
						if(type.equals("Like")){
							LikeAnimationUtil.excuteAnimation(PlayActivity.this,container,R.id.msg_like,
									SX,SY,DX,DY);
						}
					}
				});
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
