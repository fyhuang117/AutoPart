package com.autoparts.buyers.model;

/**
 * Created by:Liuhuacheng
 * Created time:15-6-27
 */
public class InquireCarModel {
    private String id;
    private String name;
    private String year_id;//年款 id
    private String year_name;//年款 名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getYear_id() {
        return year_id;
    }

    public void setYear_id(String year_id) {
        this.year_id = year_id;
    }

    public String getYear_name() {
        return year_name;
    }

    public void setYear_name(String year_name) {
        this.year_name = year_name;
    }
}
