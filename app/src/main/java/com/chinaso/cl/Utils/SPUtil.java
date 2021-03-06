package com.chinaso.cl.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 应用设置帮助类，SharedPreference存储
 * @author liuyao
 *
 */
public final class SPUtil {
	public static Context mContext;
    /**
     * 信息存储文件、键值
     */
	public static final String FILENAME = "chinasolive";
	public static final String KEY_TOKEN_WEIXIN= "Token_weixin";
    public static final String KEY_TOKEN_ANONYMOUS= "Token_anonymous";
	
	public static void init(Context context){
		mContext=context;
	}


    /**
     * 设置Token
     */
    public static void setToken(String data,String type){
        setString(FILENAME,type,data);
    }

    /**
     * 读取Token
     * @return
     */
    public static String getToken(String type){
        return getString(FILENAME,type,"");
    }

    /**
     * 设置Int型键值
     * @param key
     * @param value
     */
    public static void setInteger(String filename,String key, int value){
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }
    
    /**
     * 设置String型键值
     * @param key
     * @param value
     */
    public static void setString(String filename,String key, String value){
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    /**
     * 设置Boolean型键值
     * @param key
     * @param value
     */
    public static void setBoolean(String filename,String key, boolean value){
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    /**
     * 设置Long型键值
     * @param key
     * @param value
     */
    public static void setLong(String filename,String key, long value){
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.commit();
    }
    
    /**
     * 获取Int型值
     * @param key
     * @return
     */
    public static int getInteger(String filename,String key,Integer defaultValue){
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }
    
    /**
     * 获取String型值
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String filename,String key, String defaultValue){
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }
    
    
    /**
     * 获取Boolean型值
     * @param key
     * @return
     */
    public static boolean getBoolean(String filename,String key,boolean defaultValue){
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }
    
    /**
     * 获取Long型值
     * @param key
     * @return
     */
    public static long getLong(String filename,String key,long defaultValue){
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getLong(key, defaultValue);
    }

}
