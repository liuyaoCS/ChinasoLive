package recorder.pushflowdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.chinaso.cl.ClApp;
import com.chinaso.cl.R;
import com.letv.recorder.controller.LetvPublisher;
import com.letv.recorder.ui.RecorderSkin;
import com.letv.recorder.ui.RecorderView;

import io.rong.common.RLog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
import recorder.net.NetworkService;
import recorder.net.model.StopVideoInfo;
import recorder.net.model.VideoIdInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RecorderDemoActivity extends Activity implements RongIMClient.OnReceiveMessageListener{

	protected static final String TAG = "RecorderActivity";
	
	private static LetvPublisher publisher;
	private RecorderView rv;
	private RecorderSkin recorderSkin;
	private String mActivityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * 全屏五毛特效
		 */
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		win.requestFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_recorder);
		
		rv = (RecorderView) findViewById(R.id.rv);//获取rootView

		//createVideo();
		mActivityId=getIntent().getStringExtra("activityId");
		String userId = getIntent().getStringExtra("userId");
		String secretKey = getIntent().getStringExtra("secretKey");
		LetvPublisher.init(mActivityId, userId, secretKey);

		initPublish();//初始化推流器
		initSkin();//初始化皮肤
		bindingPublish();//绑定推流器

		checkToken();

	}

	private void checkToken() {
		if(!TextUtils.isEmpty(ClApp.TOKEN)){
			//String Token="4c4uxgc2CH6OTy9YqzYabgt5s6w7btOtdq9X3e6JX3mKeaQhrc6pSLheHhiQ2c7ggblmx5D5eDx6+DdA2rgjXA==";
			RongIMClient.connect(ClApp.TOKEN, new RongIMClient.ConnectCallback() {
				@Override
				public void onTokenIncorrect() {
					//Connect Token 失效的状态处理，需要重新获取 Token
					//Log.e("MainActivity", "onTokenIncorrect");
					Log.i("ly", "onTokenIncorrect");
				}

				@Override
				public void onSuccess(String userId) {
					//Log.i("MainActivity", "——onSuccess—-" + userId);
					Log.i("ly",  "——onSuccess—-" + userId);
					RongIMClient.setOnReceiveMessageListener(RecorderDemoActivity.this);
					sendTextMessage("hello");
				}

				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {
					//Log.e("MainActivity", "——onError—-" + errorCode);
					Log.i("ly",  "——onError—-" + errorCode);
				}
			});
		}

	}

	private void createVideo(){
		NetworkService.getInstance().createVideo("ly", new Callback<VideoIdInfo>() {
			@Override
			public void success(VideoIdInfo videoIdInfo, Response response) {
				mActivityId = videoIdInfo.getLetvId();
				//mActivityId = "A2015111900995";
				String userId = getIntent().getStringExtra("userId");
				String secretKey = getIntent().getStringExtra("secretKey");
				LetvPublisher.init(mActivityId, userId, secretKey);

				initPublish();//初始化推流器
				initSkin();//初始化皮肤
				bindingPublish();//绑定推流器
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.e("ly", "create video  err:" + retrofitError);
			}
		});

	}
	private void stopVideo(){
		NetworkService.getInstance().stopVideo(mActivityId, new Callback<StopVideoInfo>() {
			@Override
			public void success(StopVideoInfo stopVideoInfo, Response response) {
				Log.i("ly", "delete video success");
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.e("ly", "delete video err " + retrofitError);
			}
		});
	}


	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * onResume的时候需要做一些事情
		 */
		if (recorderSkin != null) {
			recorderSkin.onResume();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * onPause的时候要作的一些事情
		 */
		if (recorderSkin != null) {
			recorderSkin.onPause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(recorderSkin!=null){
			recorderSkin.onDestroy();
		}
		stopVideo();
		RongIMClient.getInstance().logout();
	}

	/**
	 * 初始化推流器
	 */
	private void initPublish() {
		publisher = LetvPublisher.getInstance();
		publisher.initPublisher(this);
	}
	/**
	 * 初始化皮肤
	 */
	private void initSkin() {
		recorderSkin = new RecorderSkin();
		recorderSkin.build(this, rv);
	}
	/**
	 * 皮肤于推流器关联
	 */
	private void bindingPublish() {
		recorderSkin.BindingPublisher(publisher);
		publisher.setCameraView(recorderSkin.getCameraView());
	}

	@Override
	public boolean onReceived(Message message, int i) {
		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {//文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d("ly", "onReceived-TextMessage:" + textMessage.getContent());
		} else {
			Log.d("ly", "onReceived-其他消息，自己来判断处理");
		}

		return false;
	}
	// 发送文本消息。
	private void sendTextMessage(String str) {

		TextMessage txtMsg=TextMessage.obtain(str);

		sendMessage(txtMsg);
	}
	// 发送消息的封装。
	private void sendMessage(TextMessage message) {

		RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, mActivityId, message, "", "", new RongIMClient.SendMessageCallback() {
			@Override
			public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
				Log.e("ly", "onError errorCode-->" + errorCode);
			}

			@Override
			public void onSuccess(Integer integer) {
				Log.i("ly","onSuccess message-->"+integer);
			}
		});
	}



}
