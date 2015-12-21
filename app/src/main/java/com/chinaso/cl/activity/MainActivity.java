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

import com.chinaso.cl.R;
import com.chinaso.cl.common.Constants;
import com.chinaso.cl.common.UserInfo;
import com.chinaso.cl.fragment.DiscoveryFragment;
import com.chinaso.cl.fragment.TopFragment;
import com.chinaso.cl.fragment.SettingFragment;
import com.chinaso.cl.fragment.HomeFragment;
import com.chinaso.cl.image.ImageCacheManager;


import io.rong.imlib.RongIMClient;
import com.recorder.net.NetworkService;
import com.recorder.net.model.VideoIdInfo;
import com.recorder.upload.FlushFlowActivity;
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

    private String nickname="";
    private String headimgurl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();
        initResources();
        mImageCacheManager = new ImageCacheManager(this);

        Intent it=getIntent();
        if(it.hasExtra("loginType")){
           int type=it.getIntExtra("loginType",0);
            if(type==1){
                nickname=Constants.NAME_DEFAULT;
                headimgurl=Constants.AVATAR_DEFAULT;
            }else if(type==2){
                nickname=UserInfo.nickname;
                headimgurl=UserInfo.headimgurl;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIMClient.getInstance().logout();
    }
    public ImageCacheManager getImageCacheManager() {
        return mImageCacheManager;
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
        NetworkService.getInstance().createVideo(nickname, nickname,headimgurl,Constants.TITLE_DEFAULT, new Callback<VideoIdInfo>() {
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
