package com.example.asus.story.utils;

import android.content.Context;

/**
 * Created by asus on 8/5/2017.
 */

public class Session {
    private static Session instance = null;
    Context context;

    public static Session getSession(Context context) {
        if(instance == null) {
            instance = new Session(context);
        }
        return instance;
    }
    protected Session(Context context) {
        this.context = context;
    }
    public static void ClearSession(Context context)
    {
        Utils.ClearShared(context);
    }

    public void setUserId(String userId) {Utils.setShared(context,Utils.userId,userId);}
    public String getUserId() {
        return Utils.getShared(context,Utils.userId,"");
    }

    public void setUserName(String userName) {Utils.setShared(context,Utils.userName,userName);}
    public String getUserName() {
        return Utils.getShared(context,Utils.userName,"");
    }

    public void setUserFbId(String userFbId) {Utils.setShared(context,Utils.userFbId,userFbId);}
    public String getUserFbId() {
        return Utils.getShared(context,Utils.userFbId,"");
    }

    public void setUserEmail(String userEmail) {Utils.setShared(context,Utils.userEmail,userEmail);}
    public String getUserEmail() {
        return Utils.getShared(context,Utils.userEmail,"");
    }

    public void setUserImg(String userImgUrl) {Utils.setShared(context,Utils.userImg,userImgUrl);}
    public String getUserImg() {
        return Utils.getShared(context,Utils.userImg,"");
    }

}
