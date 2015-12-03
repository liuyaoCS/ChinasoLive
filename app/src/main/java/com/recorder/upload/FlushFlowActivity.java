package com.recorder.upload;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaso.cl.R;
import com.chinaso.cl.Utils.AnimationUtil;
import com.chinaso.cl.Utils.FileUtil;
import com.chinaso.cl.Utils.RongUtil;
import com.letv.recorder.controller.LetvPublisher;
import com.letv.recorder.ui.RecorderSkin;
import com.letv.recorder.ui.RecorderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
import com.recorder.net.NetworkService;
import com.recorder.net.model.CoverInfo;
import com.recorder.net.model.StopVideoInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class FlushFlowActivity extends Activity implements RongIMClient.OnReceiveMessageListener{

	protected static final String TAG = "FlushFlowActivity";
	private static final int MSG_CALLAPSE_COMMENT =1 ;
	private static final int CALLAPSE_DELAY = 3000;

	private static LetvPublisher publisher;
	private RecorderView rv;
	private RecorderSkin recorderSkin;
	private String mActivityId;
	private TextView msg_count_text;
	private TextView msg_number_text;
	private ImageView msg_like_show;
	private LinearLayout comment_container;

	private int msg_count=0;
	private int msg_number=0;

	private int SX, SY;
	private int DX ,DY;
	public static int DIS_X=60;
	public static int DIS_Y=600;
	public static int DELAY=500;
	private RelativeLayout likeContainer;

	private Handler mHandler;

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
		Log.i("ly", "onCreate");

		mHandler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(android.os.Message msg) {
				switch (msg.what){
					case MSG_CALLAPSE_COMMENT:
						AnimationUtil.executeCommentCollapseAnimation(comment_container);
						break;
					default:
						break;
				}
				return true;
			}
		});

		initView();
		initLetv();
		initRongyun();

		uploadCover();
	}

	private void initView(){
		rv = (RecorderView) findViewById(R.id.rv);//获取rootView
		msg_count_text = (TextView) findViewById(R.id.msg_like);
		msg_number_text= (TextView) findViewById(R.id.msg_number);
		//msg_text_show= (TextView) findViewById(R.id.msg_text_show);
		msg_like_show= (ImageView) findViewById(R.id.msg_like_show);
		likeContainer = (RelativeLayout) findViewById(R.id.container);
		comment_container= (LinearLayout) findViewById(R.id.comment_container);
	}
	private void initLetv(){
		mActivityId=getIntent().getStringExtra("activityId");
		String userId = getIntent().getStringExtra("userId");
		String secretKey = getIntent().getStringExtra("secretKey");

		LetvPublisher.init(mActivityId, userId, secretKey);
		initPublish();//初始化推流器
		initSkin();//初始化皮肤
		bindingPublish();//绑定推流器
	}
	private void initRongyun(){
		RongIMClient.setOnReceiveMessageListener(FlushFlowActivity.this);
		RongUtil.initChatRoom(mActivityId, new RongIMClient.OperationCallback() {
			@Override
			public void onSuccess() {
				Log.i("ly", "init room success");
				msg_number++;
				FlushFlowActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						msg_count_text.setText("点赞数：" + msg_count);
						msg_number_text.setText("人数：" + msg_number);

						if (rv.getPeopleCountView() != null) {
							rv.getPeopleCountView().setText("" + msg_number);
						}
					}
				});
				RongUtil.sendEnterMessage(0, msg_number, mActivityId);
			}

			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {
				Log.e("ly", "init room error-->" + errorCode);
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
	private void uploadCover(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data==null){
			finish();
			return;
		}
		Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		String fileSrc = cursor.getString(idx);

		Log.i("ly", "file src-->" + fileSrc);
		final File cover=FileUtil.compressFile(FlushFlowActivity.this,fileSrc);
		String mimeType = "image/*";
		TypedFile fileToSend = new TypedFile(mimeType, cover);

		NetworkService.getInstance().uploadFile(mActivityId, "chinaso-video", fileToSend, new Callback<CoverInfo>() {
			@Override
			public void success(CoverInfo coverInfo, Response response) {
				Log.i("ly", "upload cover success");
				if (FileUtil.deleteFile(cover)) {
					Log.i("ly", "delete file successfull");
				} else {
					Log.e("ly", "delete file err");
				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.e("ly", "upload cover err-->" + retrofitError);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * onResume的时候需要做一些事情
		 */
		Log.i("ly","onResume");
		if (recorderSkin != null) {
			recorderSkin.onResume();
		}

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				int[] location = new int[2];
				msg_like_show.getLocationOnScreen(location);
				int x = location[0];
				int y = location[1];
				SX = x;
				SY = y;
				DX = SX;
				DY = SY - DIS_Y;
				Log.i("ly", "SX,SY = " + SX + "," + SY);
			}
		}, DELAY);

	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * onPause的时候要作的一些事情
		 */
		Log.i("ly", "onPause");
		if (recorderSkin != null) {
			recorderSkin.onPause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("ly", "onDestroy");
		if(recorderSkin!=null){
			recorderSkin.onDestroy();
		}
		stopVideo();
		msg_number--;
		RongUtil.quitChatRoom(mActivityId, msg_count, msg_number);
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
			final TextMessage textMessage = (TextMessage) messageContent;
			Log.d("ly", "onReceived-TextMessage:" + textMessage.getContent());
			try {
				final JSONObject ret=new JSONObject(textMessage.getContent());
				final String type=ret.getString("type");

				msg_count=ret.getInt("count");
				msg_number=ret.getInt("number");
				FlushFlowActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						msg_count_text.setText("点赞数：" + msg_count);
						msg_number_text.setText("人数：" + msg_number);
						if(rv.getPeopleCountView()!=null){
							rv.getPeopleCountView().setText(""+msg_number);
						}
						if(type.equals("Text")){
							String name=textMessage.getUserInfo().getName();
							if(TextUtils.isEmpty(name)){
								name="匿名";
							}
							String str=name+":"+ret.optString("message");
							AnimationUtil.addCommentItemView(FlushFlowActivity.this,comment_container,str);
							mHandler.removeMessages(MSG_CALLAPSE_COMMENT);
							mHandler.sendEmptyMessageDelayed(MSG_CALLAPSE_COMMENT,CALLAPSE_DELAY);
						}
						if(type.equals("Like")){
							AnimationUtil.executeLikeAnimation(FlushFlowActivity.this, likeContainer, R.id.msg_like_show,
									SX, SY, DX, DY);
						}
					}
				});


			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.d("ly", "onReceived--其他消息，自己来判断处理");
		}

		return false;
	}

}
