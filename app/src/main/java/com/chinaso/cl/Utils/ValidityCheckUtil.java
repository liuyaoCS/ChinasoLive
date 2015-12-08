package com.chinaso.cl.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串有效性检验
 */
public class ValidityCheckUtil {
	private final static Pattern URL_PATTERN = Pattern
			.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

	public static boolean isValidUrl(String string) {
		if(isEmptyText(string))return false;
		Matcher matcher = URL_PATTERN.matcher(string);
		return matcher.matches();
		
	}
	public static boolean isValidJson(String jsonStr){
		if(jsonStr==null || jsonStr.equals("") || jsonStr.trim().equals("") || jsonStr.equals("null")){
			return false;
		}
		try {
			new JSONObject(jsonStr);
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public static boolean isEmptyList(List<?> objects){
		if(objects==null || objects.size()==0){
			return true;
		}else {
			return false;
		}
	}
	public static boolean isEmptyText(CharSequence str) {
        if (str == null || str.length() == 0 || str.equals("null") ||  ((String) str).trim().equals("null"))
            return true;
        else
            return false;
	 }
	public static boolean isEmptyArray(Object[] objs){
		if(objs==null || objs.length==0)return true;
		else return false;
	}
}
