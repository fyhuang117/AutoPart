package com.autoparts.buyers.model;

import java.io.Serializable;

/**
 * Created by:Liuhuacheng
 * Created time:14-12-16
 */
public class CommonLetterModel implements Serializable{
    public int user_position;
    public String user_id;
    public String user_image;
    public String user_name;
    public String user_description;
    public String user_key;
    public String user_phone;
    public String user_sex;
    public String user_degree;
    public String user_resume;

    public int getUser_position() {
        return user_position;
    }

    public void setUser_position(int user_position) {
        this.user_position = user_position;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_description() {
        return user_description;
    }

    public void setUser_description(String user_description) {
        this.user_description = user_description;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_degree() {
        return user_degree;
    }

    public void setUser_degree(String user_degree) {
        this.user_degree = user_degree;
    }

    public String getUser_resume() {
        return user_resume;
    }

    public void setUser_resume(String user_resume) {
        this.user_resume = user_resume;
    }
}
