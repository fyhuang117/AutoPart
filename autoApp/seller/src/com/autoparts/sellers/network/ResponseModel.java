package com.autoparts.sellers.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ResponseModel implements Serializable{
	public int status;
	public int allcount;
	public String reason;
	public String code;
	public String jsonArrayStr;
	
	private Map<String,String> map = new HashMap<String, String>();
	
	private Vector<HashMap<String, Object>> maps = new Vector<HashMap<String,Object>>();

	public Vector<HashMap<String, Object>> getMaps() {
		return maps;
	}

	public void setMaps(Vector<HashMap<String, Object>> maps) {
		this.maps = maps;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
        this.map = map;
	}

    private Map<String,Map<String,String>> objectMap
            = new HashMap<String,Map<String,String>>();

    public Map<String, Map<String,String>> getObjectMap() {
        return objectMap;
    }


	public int getState() {
		if(map.containsKey("rspCode")){
            try {
                this.status = Integer.parseInt(map.get("rspCode"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                status = -2;
            }
        }
		return status;
	}

	public void setState(int status) {
		this.status = status;
	}

	public String getDiscription() {
		if(map.containsKey("rspDesc")){
			this.reason = map.get("rspDesc");
		}
		return reason;
	}

	public void setDiscription(String reason) {
		this.reason = reason;
	}

	public String getJsonArrayStr() {
		if(map.containsKey("jsonArrayStr")){
			this.jsonArrayStr = map.get("jsonArrayStr");
		}
		return jsonArrayStr;
	}

	public void setJsonArrayStr(String jsonArrayStr) {
		this.jsonArrayStr = jsonArrayStr;
	}
	
	public int getAllcount() {
		if(map.containsKey("total")){
            try {
                this.allcount = Integer.parseInt(map.get("total"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                allcount = 0;
            }
		}
		return allcount;
	}
	
	public void setAllcount(int total) {
		this.allcount = total;
	}

}
