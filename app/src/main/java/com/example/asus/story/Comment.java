package com.example.asus.story;

/**
 * Created by Ashutosh on 8/1/2017.
 */

public class Comment {
    int _userid;
    String _name,_comment;
    byte[] _profile;
    String _url;
    public Comment(){

    }
    public Comment(int id,String name,byte[] profile, String comment){
        this._userid=id;
        this._name=name;
        this._profile=profile;
        this._comment=comment;
    }
    public int get_userid(){
        return this._userid;
    }
    public void set_userid(int id){
        this._userid=id;
    }
    public String get_name(){
        return this._name;
    }
    public void set_name(String name){
        this._name=name;
    }
    public byte[] get_profile(){
        return this._profile;
    }
    public void set_profile(byte[] profile){
        this._profile=profile;
    }
    public String get_comment(){
        return this._comment;
    }
    public void set_comment(String comment){
        this._comment=comment;
    }
    public String get_url(){
        return this._url;
    }
    public void set_url(String url){
        this._url=url;
    }
}
