package com.example.onenprofessor.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SetNameJson extends MyJson{
	public String name = "";
	
	public SetNameJson(JSONArray target,String name){
		super("setname", target);
		this.name = name;
		try {
			jsonObject.put("name", name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public SetNameJson(JSONObject json){
		super(json);
		try {
			name = jsonObject.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
