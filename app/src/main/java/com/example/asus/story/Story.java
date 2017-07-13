/*package com.example.asus.story;

import com.orm.SugarRecord;

/**
 * Created by asus on 6/28/2017.
 */
/*
public class Story extends SugarRecord {
    public String title,desc;
    public byte[] img;
    Story(){ }
    Story(String title,String desc,byte[] img)
    {
    }
    //getters and setters
    //public int getID(){return id;}
    public String getTitle(){
        return title;
    }
    public String getDesc(){
        return desc;
    }
    public byte[] getImg(){
        return img;
    }

    //public void setID(int id) {this.id = id;}

    public void setTitle(String title){
        this.title = title;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setImg(byte[] img){
        this.img = img;
    }

}*/

package com.example.asus.story;

import java.io.Serializable;

/**
 * Created by asus on 6/19/2017.
 */

public class Story implements Serializable {

    //private variables
    int _id;
    String _caption, _desc;
    String _path, _email, _url;
    String _cat_id;


    byte[] _img;

    // Empty constructor
    public Story(){

    }
    // constructor
    public Story(int id, String caption, byte[] img, String desc){
        this._id = id;
        this._caption = caption;
        this._img = img;
        this._desc = desc;
    }

    // constructor
    public Story(String caption, byte[] img, String desc){

        this._caption = caption;
        this._img = img;
        this._desc = desc;

    }
    public Story(String caption, String desc, String path, String cat_id, String email){
        this._path = path;
        this._cat_id = cat_id;
        this._caption = caption;
        this._desc = desc;
        this._email = email;

    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting first name
    public String getCaption(){
        return this._caption;
    }

    // setting first name
    public void setcaption(String caption){
        this._caption = caption;
    }

    // getting first name
    public String getDesc(){
        return this._desc;
    }

    // setting first name
    public void setdesc(String desc){
        this._desc = desc;
    }

    //getting profile pic
    public byte[] getImage(){
        return this._img;
    }

    //setting profile pic
    public void setImage(byte[] b){this._img=b;}

    //Getting path
    public String getPath(){
        return this._path;
    }
    // setting path
    public void setPath(String path){

        this._path = path;
    }

    //Getting path
    public String getUrl(){
        return this._url;
    }
    // setting path
    public void setUrl(String Url){

        this._url = Url;
    }

    //Getting Email
    public String getEmail(){
        return this._email;
    }
    // setting Email
    public void setEmail(String email){

        this._email = email;
    }

    //Getting Cat_id
    public String getCat_id(){
        return this._cat_id;
    }
    // setting path
    public void setCat_id(String cat_id){

        this._cat_id = cat_id;
    }

}

