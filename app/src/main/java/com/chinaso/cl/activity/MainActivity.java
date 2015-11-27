package com.chinaso.cl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.cl.ClApp;
import com.chinaso.cl.R;
import com.chinaso.cl.common.Constants;
import com.chinaso.cl.fragment.DiscoveryFragment;
import com.chinaso.cl.fragment.TopFragment;
import com.chinaso.cl.fragment.SettingFragment;
import com.chinaso.cl.fragment.HomeFragment;
import com.chinaso.cl.image.ImageCacheManager;


import java.util.Random;

import io.rong.imlib.RongIMClient;
import recorder.net.NetworkService;
import recorder.net.model.UserCheckInfo;
import recorder.net.model.VideoIdInfo;
import recorder.upload.FlushFlowActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    TextView home_text,top_text,discovery_text,setting_text;
    Fragment homeFragment,topFragment,discoveryFragment,settingFragment;
    Fragment mCurrentFragment;
    ImageView record;
    FragmentManager fm;
    FragmentTransaction ft;
    Resources rs;
    int menuTextBgColor,menuTextBgCurrentColor;
    private ImageCacheManager mImageCacheManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //LeCloud.init(getApplicationContext());
        initView();
        initFragment();
        initResources();
        mImageCacheManager = new ImageCacheManager(this);

//        Random random=new Random();
//        int i=random.nextInt(10);
        createUser(Constants.USERID+new Random().nextInt(20));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIMClient.getInstance().logout();
    }
    public ImageCacheManager getImageCacheManager() {
        return mImageCacheManager;
    }
    private void createUser(String name) {
        NetworkService.getInstance().getToken(name, new Callback<UserCheckInfo>() {
            @Override
            public void success(UserCheckInfo userInfo, Response response) {
                ClApp.TOKEN = userInfo.getToken();
                Log.i("ly", "get token success--" + ClApp.TOKEN);
                checkToken();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "获取token失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkToken() {
        if(!TextUtils.isEmpty(ClApp.TOKEN)){
            RongIMClient.connect(ClApp.TOKEN, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    //Connect Token 失效的状态处理，需要重新获取 Token
                    Log.e("ly", "onTokenIncorrect");
                    Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String userId) {
                    //Log.i("MainActivity", "——onSuccess—-" + userId);
                    //RongIMClient.setOnReceiveMessageListener(MainActivity.this);
                    Log.i("ly", "——onSuccess—-" + userId);
                    Toast.makeText(MainActivity.this, "用户" + userId + "登录成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    //Log.e("MainActivity", "——onError—-" + errorCode);
                    Log.e("ly", "——onError—-" + errorCode);
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void initView(){

        home_text= (TextView) findViewById(R.id.home_text);
        home_text.setOnClickListener(this);
        top_text= (TextView) findViewById(R.id.top_text);
        top_text.setOnClickListener(this);
        discovery_text= (TextView) findViewById(R.id.discovery_text);
        discovery_text.setOnClickListener(this);
        setting_text= (TextView) findViewById(R.id.setting_text);
        setting_text.setOnClickListener(this);

        record= (ImageView) findViewById(R.id.record_btn);
        record.setOnClickListener(this);
    }
    private void initFragment(){
        homeFragment=new HomeFragment();
        topFragment=new TopFragment();
        discoveryFragment=new DiscoveryFragment();
        settingFragment=new SettingFragment();
        mCurrentFragment=homeFragment;

        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.content,homeFragment,"homeFragment")
                .add(R.id.content, discoveryFragment, "discoveryFragment").hide(discoveryFragment)
                .add(R.id.content, settingFragment, "settingFragment").hide(settingFragment)
                .add(R.id.content, topFragment, "topFragment").hide(topFragment)
                .commit();
    }
    private void initResources(){
        rs= getResources();
        menuTextBgColor=rs.getColor(R.color.menu_text_bg);
        menuTextBgCurrentColor=rs.getColor(R.color.menu_text_bg_current);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        clearSelection();
        int id=v.getId();
        switch(id){
            case R.id.home_text:
                home_text.setTextColor(menuTextBgCurrentColor);
                Drawable drawableHome = rs.getDrawable(R.mipmap.menu_home_current);
                home_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableHome, null, null);
                switchFragment("homeFragment");
                break;
            case R.id.top_text:
                top_text.setTextColor(menuTextBgCurrentColor);
                Drawable drawableTop = rs.getDrawable(R.mipmap.menu_top_current);
                top_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableTop, null, null);
                switchFragment("topFragment");
                break;
            case R.id.record_btn:
                createVideo();
                break;
            case R.id.discovery_text:
                discovery_text.setTextColor(menuTextBgCurrentColor);
                Drawable drawableDiscovery = rs.getDrawable(R.mipmap.menu_discovery_current);
                discovery_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableDiscovery, null, null);
                switchFragment("discoveryFragment");
                break;
            case R.id.setting_text:
                setting_text.setTextColor(menuTextBgCurrentColor);
                Drawable drawableSetting = rs.getDrawable(R.mipmap.menu_setting_current);
                setting_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableSetting, null, null);
                switchFragment("settingFragment");
                break;
            default:
                break;
        }
    }
    private void createVideo(){
        NetworkService.getInstance().createVideo(Constants.USERID,Constants.NAME,Constants.AVATAR,Constants.TITLE, new Callback<VideoIdInfo>() {
            @Override
            public void success(VideoIdInfo videoIdInfo, Response response) {
                String activityId = videoIdInfo.getLetvId();


                Intent intent = new Intent(MainActivity.this, FlushFlowActivity.class);
                intent.putExtra("activityId", activityId);
                intent.putExtra("userId", Constants.userId);
                intent.putExtra("secretKey", Constants.secretKey);
                startActivity(intent);

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("ly", "create video  err:" + retrofitError);
            }
        });

    }

    @SuppressLint("NewApi")
    private void clearSelection() {
        home_text.setTextColor(menuTextBgColor);
        Drawable drawableHome = rs.getDrawable(R.mipmap.menu_home);
        home_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableHome, null, null);

        top_text.setTextColor(menuTextBgColor);
        Drawable drawableTop = rs.getDrawable(R.mipmap.menu_top);
        top_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableTop, null, null);

        discovery_text.setTextColor(menuTextBgColor);
        Drawable drawableDiscovery = rs.getDrawable(R.mipmap.menu_discovery);
        discovery_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableDiscovery, null, null);

        setting_text.setTextColor(menuTextBgColor);
        Drawable drawableSetting = rs.getDrawable(R.mipmap.menu_setting);
        setting_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableSetting, null, null);

    }
    public void switchFragment( String toTag) {
        Fragment to=fm.findFragmentByTag(toTag);
        if (mCurrentFragment != to) {
            fm.beginTransaction().hide(mCurrentFragment).show(to).commit();
            mCurrentFragment = to;
        }
    }

}
