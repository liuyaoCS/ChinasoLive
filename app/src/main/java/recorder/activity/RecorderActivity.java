package recorder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.cl.R;
import com.chinaso.cl.Utils.RongUtil;
import com.letv.recorder.controller.LetvPublisher;
import com.letv.recorder.ui.RecorderSkin;
import com.letv.recorder.ui.RecorderView;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
import recorder.net.NetworkService;
import recorder.net.model.StopVideoInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RecorderActivity extends Activity implements RongIMClient.OnReceiveMessageListener{

	protected static final String TAG = "RecorderActivity";
	
	private static LetvPublisher publisher;
	private RecorderView rv;
	private RecorderSkin recorderSkin;
	private String mActivityId;
	private TextView msg_count_text;
	private TextView msg_number_text;

	private int msg_count=0;
	private int msg_number=1;

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
		msg_count_text = (TextView) findViewById(R.id.msg_like);
		msg_number_text= (TextView) findViewById(R.id.msg_number);

		//createVideo();
		mActivityId=getIntent().getStringExtra("activityId");
		String userId = getIntent().getStringExtra("userId");
		String secretKey = getIntent().getStringExtra("secretKey");

		LetvPublisher.init(mActivityId, userId, secretKey);
		initPublish();//初始化推流器
		initSkin();//初始化皮肤
		bindingPublish();//绑定推流器

		RongIMClient.setOnReceiveMessageListener(RecorderActivity.this);
		RongUtil.initChatRoom(mActivityId);

	}

//	private void createVideo(){
//		NetworkService.getInstance().createVideo("ly", new Callback<VideoIdInfo>() {
//			@Override
//			public void success(VideoIdInfo videoIdInfo, Response response) {
//				mActivityId = videoIdInfo.getLetvId();
//				//mActivityId = "A2015111900995";
//				String userId = getIntent().getStringExtra("userId");
//				String secretKey = getIntent().getStringExtra("secretKey");
//				LetvPublisher.init(mActivityId, userId, secretKey);
//
//				initPublish();//初始化推流器
//				initSkin();//初始化皮肤
//				bindingPublish();//绑定推流器
//			}
//
//			@Override
//			public void failure(RetrofitError retrofitError) {
//				Log.e("ly", "create video  err:" + retrofitError);
//			}
//		});
//
//	}
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
		msg_number--;
		RongUtil.quitChatRoom(mActivityId,msg_count,msg_number);
		//RongIMClient.getInstance().logout();
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
			//msg_count.setText(textMessage.getContent());
			try {
				JSONObject ret=new JSONObject(textMessage.getContent());
				//String type=ret.getString("type");
				msg_count=ret.getInt("count");
				msg_number=ret.getInt("number");

				msg_count_text.setText("点赞数："+msg_count);
				msg_number_text.setText("人数："+msg_number);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.d("ly", "onReceived-其他消息，自己来判断处理");
		}

		return false;
	}

}
