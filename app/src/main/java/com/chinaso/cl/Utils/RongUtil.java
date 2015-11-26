package com.chinaso.cl.Utils;

import android.net.Uri;
import android.util.Log;

import com.chinaso.cl.common.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * Created by chinaso on 2015/11/25.
 */
public class RongUtil {

    public static void quitChatRoom(final String chatroomId,final int count,final int number){
        RongIMClient.getInstance().quitChatRoom(chatroomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.i("ly", "quit chatroom success");
                sendExitMessage(count,number,chatroomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("ly", "quit chatroom onError errorCode-->" + errorCode);
            }
        });
    }
   public static void joinChatRoom(final String chatroomId){
       RongIMClient.getInstance().joinChatRoom(chatroomId, 1, new RongIMClient.OperationCallback() {
           @Override
           public void onSuccess() {
               Log.i("ly", "join chatroom success at roomId-> " + chatroomId);
           }

           @Override
           public void onError(RongIMClient.ErrorCode errorCode) {
               Log.e("ly", "join chatroom onError errorCode-->" + errorCode);
           }
       });
   }
    public static void initChatRoom(final String chatroomId,RongIMClient.OperationCallback callback){
        RongIMClient.getInstance().joinChatRoom(chatroomId, -1, callback);
    }
    /**
     * 发送文本消息
     * @param str 消息内容
     * @param chatroomId 聊天室Id
     */
    public static void sendTextMessage(final int count,final int number,String str, final String chatroomId) {

        JSONObject content=new JSONObject();
        try {
            content.put("type","Text");
            content.put("count",count);
            content.put("number",number);
            content.put("message",str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextMessage txtMsg=TextMessage.obtain(content.toString());
        UserInfo userInfo=new UserInfo(Constants.USERID,Constants.NAME, Uri.parse(Constants.AVATAR));
        txtMsg.setUserInfo(userInfo);

        sendMessage(txtMsg, chatroomId);
    }
    /**
     * 发送进入聊天室消息
     * @param count 点赞数
     * @param number 当前人数
     * @param chatroomId 聊天室Id
     */
    public static void sendLikeMessage(int count,int number, final String chatroomId,RongIMClient.SendMessageCallback callback) {

        JSONObject content=new JSONObject();
        try {
            content.put("type","Like");
            content.put("count",count);
            content.put("number",number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextMessage txtMsg=TextMessage.obtain(content.toString());
        UserInfo userInfo=new UserInfo(Constants.USERID,Constants.NAME, Uri.parse(Constants.AVATAR));
        txtMsg.setUserInfo(userInfo);

        sendMessage(txtMsg, chatroomId,callback);
    }

    /**
     * 发送进入聊天室消息
     * @param count 点赞数
     * @param number 当前人数
     * @param chatroomId 聊天室Id
     */
    public static void sendEnterMessage(int count,int number, final String chatroomId) {

        JSONObject content=new JSONObject();
        try {
            content.put("type","Enter");
            content.put("count",count);
            content.put("number",number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextMessage txtMsg=TextMessage.obtain(content.toString());
        UserInfo userInfo=new UserInfo(Constants.USERID,Constants.NAME, Uri.parse(Constants.AVATAR));
        txtMsg.setUserInfo(userInfo);

        sendMessage(txtMsg, chatroomId);
    }
    /**
     * 发送退出聊天室消息
     * @param count 点赞数
     * @param number 当前人数
     * @param chatroomId 聊天室Id
     */
    public static void sendExitMessage(int count,int number, final String chatroomId) {

        JSONObject content=new JSONObject();
        try {
            content.put("type","Exit");
            content.put("count",count);
            content.put("number",number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextMessage txtMsg=TextMessage.obtain(content.toString());
        UserInfo userInfo=new UserInfo(Constants.USERID,Constants.NAME, Uri.parse(Constants.AVATAR));
        txtMsg.setUserInfo(userInfo);

        sendMessage(txtMsg, chatroomId);
    }
    // 发送消息的封装。
    private static void sendMessage(TextMessage message,final String chatroomId) {

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, chatroomId, message, "", "", new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                Log.e("ly", "onError errorCode-->" + errorCode);
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.i("ly", "onSuccess message-->" + integer);
            }
        }, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                Log.i("ly", "onSuccess message-->-->" + message);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("ly", "onError errorCode-->" + errorCode);
            }
        });
    }
    private static void sendMessage(TextMessage message,final String chatroomId,RongIMClient.SendMessageCallback callback) {

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, chatroomId, message, "", "", callback, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                Log.i("ly", "onSuccess message-->-->" + message);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("ly", "onError errorCode-->" + errorCode);
            }
        });
    }
}
