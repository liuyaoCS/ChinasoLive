//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.request;

import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.bean.RecorderInfo;
import com.letv.recorder.bean.SearchActivityInfo;
import com.letv.recorder.callback.RequestCallback;
import com.letv.recorder.request.BaseRequest;
import com.letv.recorder.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecorderRequest extends BaseRequest {
    private static final String key_msg = "errMsg";
    private static final String key_errorCode = "errCode";
    private static final String key_method = "method";
    private static final String key_activityid = "activityId";
    private static final String key_ver = "ver";
    private static final String key_userid = "userid";

    public RecorderRequest() {
    }

    public static void init(String userid, String activitId, String secretkey) {
        userId = userid;
        activityId = activitId;
        secretKey = secretkey;
    }

    public void onlineCountRequest(final String activityId, final RequestCallback callback) {
        (new Thread(new Runnable() {
            public void run() {
                TreeMap map = new TreeMap();
                map.put("method", "letv.cloudlive.activity.getOnlineCount");
                map.put("activityId", activityId);
                map.put("ver", "3.0");

                try {
                    Map ret = RecorderRequest.this.baseRequest(map);
                    if(ret != null) {
                        int e = Integer.valueOf((String)ret.get("code")).intValue();
                        final String response = (String)ret.get("response");
                        if(response != null && !response.isEmpty()) {
                            RecorderRequest.this.requestCallbackHelper(e, response, new RecorderRequest.Parser200Callback() {
                                public void on200Parser() throws JSONException {
                                    JSONObject onlineJson = new JSONObject(response);
                                    String userCount = onlineJson.optString("userCount");
                                    if(callback != null) {
                                        callback.onSucess(userCount);
                                    }

                                }
                            }, callback);
                        }
                    }
                } catch (ClientProtocolException var5) {
                    var5.printStackTrace();
                } catch (IOException var6) {
                    var6.printStackTrace();
                } catch (JSONException var7) {
                    var7.printStackTrace();
                }

            }
        })).start();
    }

    private void requestCallbackHelper(int code, String response, RecorderRequest.Parser200Callback callback200, RequestCallback callback) throws JSONException {
        switch(code) {
            case 200:
                if(callback200 != null) {
                    callback200.on200Parser();
                }
                break;
            case 400:
                Map errorMap = this.error400Parser(response);
                if(errorMap != null && callback != null) {
                    callback.onFailed(400, "code:" + (String)errorMap.get("errCode") + "," + (String)errorMap.get("errMsg"));
                }
                break;
            case 404:
                if(callback != null) {
                    callback.onFailed(404, response);
                }
                break;
            case 500:
                String errMsg = this.error500Parser(response);
                if(callback != null) {
                    callback.onFailed(500, errMsg);
                }
        }

    }

    public void searchActivityByUserIDRequest(final String userId, final RequestCallback callback) {
        (new Thread(new Runnable() {
            public void run() {
                String method = "letv.cloudlive.activity.search";
                TreeMap map = new TreeMap();
                map.put("method", method);
                map.put("userid", userId);
                map.put("ver", "3.0");

                try {
                    Map e = RecorderRequest.this.baseRequest(map);
                    if(e != null) {
                        int code = Integer.valueOf((String)e.get("code")).intValue();
                        final String response = (String)e.get("response");
                        if(response != null && !response.isEmpty()) {
                            RecorderRequest.this.requestCallbackHelper(code, response, new RecorderRequest.Parser200Callback() {
                                public void on200Parser() throws JSONException {
                                    ArrayList infos = RecorderRequest.this.searchActivityParser(response);
                                    if(callback != null) {
                                        callback.onSucess(infos);
                                    }

                                }
                            }, callback);
                        }
                    }
                } catch (ClientProtocolException var6) {
                    var6.printStackTrace();
                } catch (IOException var7) {
                    var7.printStackTrace();
                } catch (JSONException var8) {
                    var8.printStackTrace();
                }

            }
        })).start();
    }

    public void searchActivityRequest(final String activityId, final RequestCallback callback) {
        (new Thread(new Runnable() {
            public void run() {
                String method = "letv.cloudlive.activity.search";
                TreeMap map = new TreeMap();
                map.put("method", method);
                map.put("activityId", activityId);
                map.put("ver", "3.0");

                try {
                    Map e = RecorderRequest.this.baseRequest(map);
                    if(e != null) {
                        int code = Integer.valueOf((String)e.get("code")).intValue();
                        final String response = (String)e.get("response");
                        if(response != null && !response.isEmpty()) {
                            RecorderRequest.this.requestCallbackHelper(code, response, new RecorderRequest.Parser200Callback() {
                                public void on200Parser() throws JSONException {
                                    ArrayList infos = RecorderRequest.this.searchActivityParser(response);
                                    if(callback != null) {
                                        callback.onSucess(infos);
                                    }

                                }
                            }, callback);
                        }
                    }
                } catch (ClientProtocolException var6) {
                    var6.printStackTrace();
                } catch (IOException var7) {
                    var7.printStackTrace();
                } catch (JSONException var8) {
                    var8.printStackTrace();
                }

            }
        })).start();
    }

    private String error500Parser(String ret500) throws JSONException {
        JSONObject json500 = new JSONObject(ret500);
        String errMsg = json500.optString("errMsg");
        return errMsg;
    }

    private Map<String, String> error400Parser(String ret400) throws JSONException {
        JSONObject json400 = new JSONObject(ret400);
        String errorCode = json400.optString("errCode");
        String errMsg = json400.optString("errMsg");
        HashMap map = new HashMap();
        map.put("errCode", errorCode);
        map.put("errMsg", errMsg);
        return map;
    }

    private ArrayList<SearchActivityInfo> searchActivityParser(String response) throws JSONException {
        JSONArray searchAJsonA = new JSONArray(response);
        ArrayList infos = new ArrayList();

        for(int i = 0; i < searchAJsonA.length(); ++i) {
            JSONObject searchAJson = searchAJsonA.optJSONObject(i);
            SearchActivityInfo info = new SearchActivityInfo();
            info.activityId = searchAJson.optString("activityId");
            info.activityName = searchAJson.optString("activityName");
            info.activityStatus = searchAJson.optInt("activityStatus");
            info.coverImgUrl = searchAJson.optString("coverImgUrl");
            info.startTime = searchAJson.optString("startTime");
            info.createTime = searchAJson.optLong("createTime");
            info.description = searchAJson.optString("description");
            info.endTime = searchAJson.optString("endTime");
            info.liveNum = searchAJson.optInt("liveNum");
            info.needIpWhiteList = searchAJson.optInt("needIpWhiteList");
            info.needRecord = searchAJson.optInt("needRecord");
            info.needTimeShift = searchAJson.optInt("needTimeShift");
            info.neededPushAuth = searchAJson.optInt("neededPushAuth");
            info.pushIpWhiteList = searchAJson.optString("pushIpWhiteList");
            info.pushUrlValidTime = searchAJson.optInt("pushUrlValidTime");
            info.startTime = searchAJson.optString("startTime");
            info.userCount = searchAJson.optInt("userCount");
            infos.add(info);
        }

        return infos;
    }

    public void liveRequest3(final String activityId, final RequestCallback callback) {
        this.getPushTokenRequest(activityId, new RequestCallback() {
            public void onSucess(Object object) {
                String token = (String) object;
                StringBuilder sb = new StringBuilder();
                sb.append("?");
                sb.append("activityId=" + activityId);
                sb.append("&");
                sb.append("token=" + token);
                sb.append("&");
                sb.append("ver=v4");
                String url = RecorderRequest.recorder_url + sb.toString();
                Log.d("RecorderRequest", "[liveRequest] url:" + url);
                boolean code = false;

                try {
                    Map e = RecorderRequest.this.httpGetRequest(url);
                    int code1 = Integer.valueOf((String) e.get("code")).intValue();
                    final String response = (String) e.get("response");
                    if (response != null && !response.isEmpty()) {
                        RecorderRequest.this.requestCallbackHelper(code1, response, new RecorderRequest.Parser200Callback() {
                            public void on200Parser() throws JSONException {
                                RecorderInfo recorderInfo = RecorderRequest.this.liveParser(response);
                                if (callback != null) {
                                    callback.onSucess(recorderInfo);
                                }

                            }
                        }, callback);
                    }
                } catch (ClientProtocolException var8) {
                    var8.printStackTrace();
                } catch (IOException var9) {
                    var9.printStackTrace();
                } catch (JSONException var10) {
                    var10.printStackTrace();
                } catch (Exception var11) {
                    var11.printStackTrace();
                }

            }

            public void onFailed(int statusCode, String errorMsg) {
                if (callback != null) {
                    callback.onFailed(statusCode, errorMsg);
                }

            }
        });
    }

    public void machineStateRequest(final String activityId, final RequestCallback callback) {
        (new Thread(new Runnable() {
            int code = 0;

            public void run() {
                try {
                    StringBuilder e = new StringBuilder();
                    e.append("?");
                    e.append("activityId=" + activityId);
                    e.append("&");
                    e.append("ver=v4");
                    String url = RecorderRequest.machine_state_url + e.toString();
                    Map ret = RecorderRequest.this.httpGetRequest(url);
                    this.code = Integer.valueOf((String)ret.get("code")).intValue();
                    final String response = (String)ret.get("response");
                    if(response != null && !response.isEmpty()) {
                        RecorderRequest.this.requestCallbackHelper(this.code, response, new RecorderRequest.Parser200Callback() {
                            public void on200Parser() throws JSONException {
                                RecorderInfo parseMachineState = RecorderRequest.this.machineStateParser(response);
                                if(callback != null) {
                                    callback.onSucess(parseMachineState);
                                }

                            }
                        }, callback);
                    }
                } catch (ClientProtocolException var5) {
                    var5.printStackTrace();
                } catch (IOException var6) {
                    var6.printStackTrace();
                } catch (JSONException var7) {
                    var7.printStackTrace();
                }

            }
        })).start();
    }

    private RecorderInfo machineStateParser(String response) throws JSONException {
        RecorderInfo recorderInfo = new RecorderInfo();
        JSONObject root = new JSONObject(response);
        recorderInfo.activityId = root.optString("activityId");
        recorderInfo.liveNum = root.optString("liveNum");
        JSONArray childJson = root.optJSONArray("lives");

        for(int i = 0; i < childJson.length(); ++i) {
            JSONObject thirdJson = childJson.optJSONObject(i);
            LivesInfo info = new LivesInfo();
            info.machine = thirdJson.optInt("machine");
            info.liveId = thirdJson.optString("liveId");
            info.status = thirdJson.optInt("status");
            recorderInfo.livesInfos.add(info);
        }

        return recorderInfo;
    }

    public RecorderInfo liveParser(String response) throws JSONException {
        RecorderInfo recorderInfo = new RecorderInfo();
        JSONObject root = new JSONObject(response);
        recorderInfo.activityId = root.optString("activityId");
        recorderInfo.liveNum = root.optString("liveNum");
        recorderInfo.activityName = root.optString("activityName");
        recorderInfo.playerPageUrl = root.optString("playerPageUrl");
        recorderInfo.description = root.optString("description");
        recorderInfo.coverImgUrl = root.optString("coverImgUrl");
        JSONArray childJson = root.optJSONArray("lives");

        for(int i = 0; i < childJson.length(); ++i) {
            JSONObject thirdJson = childJson.optJSONObject(i);
            LivesInfo info = new LivesInfo();
            info.machine = thirdJson.optInt("machine");
            info.liveId = thirdJson.optString("liveId");
            info.status = thirdJson.optInt("status");
            info.streamId = thirdJson.optInt("streamId");
            info.pushUrl = thirdJson.optString("pushUrl");
            recorderInfo.livesInfos.add(info);
        }

        return recorderInfo;
    }

    public void getPushTokenRequest(final String activityId, final RequestCallback callback) {
        (new Thread(new Runnable() {
            public void run() {
                String method = "letv.cloudlive.activity.getPushToken";
                TreeMap map = new TreeMap();
                map.put("method", method);
                map.put("activityId", activityId);
                map.put("ver", "3.0");

                try {
                    Map e = RecorderRequest.this.baseRequest(map);
                    int code = Integer.valueOf((String)e.get("code")).intValue();
                    final String response = (String)e.get("response");
                    if(response != null && !response.isEmpty()) {
                        RecorderRequest.this.requestCallbackHelper(code, response, new RecorderRequest.Parser200Callback() {
                            public void on200Parser() throws JSONException {
                                JSONObject tokenJson = new JSONObject(response);
                                String token = tokenJson.optString("token");
                                if(callback != null) {
                                    callback.onSucess(token);
                                }

                            }
                        }, callback);
                    }
                } catch (ClientProtocolException var6) {
                    var6.printStackTrace();
                } catch (IOException var7) {
                    var7.printStackTrace();
                } catch (JSONException var8) {
                    var8.printStackTrace();
                }

            }
        })).start();
    }

    public interface Parser200Callback {
        void on200Parser() throws JSONException;
    }
}
