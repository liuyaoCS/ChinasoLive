package com.chinaso.cl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chinaso.cl.R;
import com.chinaso.cl.Utils.SPUtil;
import com.chinaso.cl.common.Constants;
import com.chinaso.cl.common.UserInfo;
import com.recorder.net.NetworkService;
import com.recorder.net.model.UserCheckInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;


import java.util.Map;
import java.util.Set;

import io.rong.imlib.RongIMClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity {

    private final static String appId = "wx448543a14cc8444a";
    private final static String appSecret = "845030800d371f8761775a5f1e1d48f4";
    private UMSocialService mController;

    private Button wlogin;
    private Button nlogin;
    private Activity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        com.umeng.socialize.utils.Log.LOG = true;
        mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
        UMWXHandler wxHandler = new UMWXHandler(mContext, appId, appSecret);
        wxHandler.addToSocialSDK();

        wlogin = (Button) findViewById(R.id.wlogin);
        wlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wLogin();
            }
        });

        nlogin= (Button) findViewById(R.id.nlogin);
        nlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nLogin();
            }
        });
    }
    private void nLogin(){
        UserInfo.TOKEN=SPUtil.getToken(SPUtil.KEY_TOKEN_ANONYMOUS);
        UserInfo.headimgurl=Constants.AVATAR_DEFAULT;
        UserInfo.nickname=Constants.NAME_DEFAULT;
        if(!TextUtils.isEmpty(UserInfo.TOKEN)){
            Log.i("ly", "has token local,check");
            checkToken();
        }else{
            Log.i("ly", "no local token,request");
            createTokenByName(Constants.NAME_DEFAULT,SPUtil.KEY_TOKEN_ANONYMOUS);
        }
    }
    private void wLogin() {
        mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                //Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
                //获取相关授权信息
                // 获取uid
                String uid = value.getString("uid");
                if (!TextUtils.isEmpty(uid)) {
                    // uid不为空，获取用户信息
                    getUserInfo();
                } else {
                    Toast.makeText(LoginActivity.this, "授权失败...",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getUserInfo(){
        mController.getPlatformInfo(mContext, SHARE_MEDIA.WEIXIN, new UMDataListener() {
            @Override
            public void onStart() {
                //此方法已经不被调用了
                Log.i("ly", "onstart");
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                if (status == 200 && info != null) {
                    StringBuilder sb = new StringBuilder();
                    Set<String> keys = info.keySet();
                    for (String key : keys) {
                        sb.append(key + "=" + info.get(key).toString());
                        Log.i("ly", key + "=" + info.get(key).toString());
                        if (key.equals("nickname")) {
                            UserInfo.nickname = info.get(key).toString();
                        }
                        if (key.equals("headimgurl")) {
                            UserInfo.headimgurl = info.get(key).toString();
                        }
                    }
                    Toast.makeText(LoginActivity.this, "status-->" + status + " info-->" + info.toString(), Toast.LENGTH_SHORT).show();

                    if (!TextUtils.isEmpty(UserInfo.nickname)) {
                        UserInfo.TOKEN=SPUtil.getToken(SPUtil.KEY_TOKEN_WEIXIN);
                        if(!TextUtils.isEmpty(UserInfo.TOKEN)){
                            Log.i("ly", "has token local,check");
                            checkToken();
                        }else{
                            Log.i("ly", "no local token,request");
                            createTokenByName(UserInfo.nickname,SPUtil.KEY_TOKEN_WEIXIN);
                        }
                    }

                } else {
                    Log.e("ly", "发生错误：" + status);
                    Toast.makeText(LoginActivity.this, "error status-->" + status, Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    /**
     * 注销本次登陆
     * @param platform
     */
    private void logout(final SHARE_MEDIA platform) {
        mController.deleteOauth(LoginActivity.this, platform, new SocializeListeners.SocializeClientListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int status, SocializeEntity entity) {
                String showText = "解除" + platform.toString() + "平台授权成功";
                if (status != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "解除" + platform.toString() + "平台授权失败[" + status + "]";
                }
                Toast.makeText(LoginActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createTokenByName(String name,final String tokenType) {
        NetworkService.getInstance().getToken(name, new Callback<UserCheckInfo>() {
            @Override
            public void success(UserCheckInfo userCheckInfo, Response response) {
                UserInfo.TOKEN = userCheckInfo.getToken();
                SPUtil.setToken(userCheckInfo.getToken(),tokenType);
                Log.i("ly", "get token success--" + UserInfo.TOKEN);
                checkToken();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(mContext, "获取token失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkToken() {
        if(!TextUtils.isEmpty(UserInfo.TOKEN)){
            RongIMClient.connect(UserInfo.TOKEN, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    //Connect Token 失效的状态处理，需要重新获取 Token
                    Log.e("ly", "onTokenIncorrect");
                    Toast.makeText(mContext, "认证失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String userId) {
                    Log.i("ly", "——onSuccess—-" + userId);
                    Toast.makeText(mContext, "用户" + userId + "登录成功", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(it);
                    finish();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("ly", "——onError—-" + errorCode);
                    Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
