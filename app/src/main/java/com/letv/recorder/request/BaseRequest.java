//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.request;

import com.letv.recorder.util.Log;
import com.letv.recorder.util.MD5Utls;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class BaseRequest {
    protected static final String TAG = "RecorderRequest";
    private static final String HTTP_OPENAPI_LIVE = "http://api.open.letvcloud.com/live";
    private static final String HTTP_RTMPLIVE_API = "http://api.live.letvcloud.com";
    private static final String HTTP_API_LIVE_LETVCLOUD_COM = "http://api.live.letvcloud.com";
    public static String machine_state_url = "http://api.live.letvcloud.com/rtmp/getActivityMachineState";
    public static String recorder_url = "http://api.live.letvcloud.com/rtmp/getActivityInfoForUploadSDK";
    public static String open_api_url = "http://api.open.letvcloud.com/live/execute";
    private static final String PAGE_NO_FOUND_404 = "page no found!";
    public static String secretKey = "";
    public static String userId = "";
    public static String activityId = "";
    public static final String key_code = "code";
    public static final String key_response = "response";
    public static final String key_type = "keyType";
    public static final String key_white_list = "key";

    public BaseRequest() {
    }

    public Map<String, String> httpGetRequest(String url) throws ClientProtocolException, IOException {
        HttpGet httpGet = new HttpGet(url);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        Log.w("RecorderRequest", "[httpGetRequest] code:" + statusCode);
        HashMap responses = new HashMap();
        if(statusCode != 404) {
            String res = EntityUtils.toString(response.getEntity(), "utf-8");
            Log.d("RecorderRequest", "[httpGetRequest] " + res);
            responses.put("response", res);
        } else {
            responses.put("response", "page no found!");
        }

        responses.put("code", String.valueOf(statusCode));
        return responses;
    }

    protected Map<String, String> baseRequest(TreeMap<String, String> map) throws ClientProtocolException, IOException {
        map.put("userid", userId);
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        StringBuilder sb = new StringBuilder();
        StringBuilder sbSign = new StringBuilder();
        Iterator iterator = map.entrySet().iterator();

        while(iterator.hasNext()) {
            Entry sign = (Entry)iterator.next();
            sb.append((String)sign.getKey());
            sbSign.append((String)sign.getKey());
            sb.append("=");
            sb.append((String)sign.getValue());
            sbSign.append((String)sign.getValue());
            sb.append("&");
        }

        sbSign.append(secretKey);
        Log.d("RecorderRequest", "[baseRequest] params:" + sbSign.toString());
        String sign1 = MD5Utls.encodeByMD5(sbSign.toString());
        sb.append("sign=");
        sb.append(sign1);
        String url = open_api_url + "?" + sb.toString();
        Log.d("RecorderRequest", "[baseRequest] url:" + url);
        return this.httpGetRequest(url);
    }
}
