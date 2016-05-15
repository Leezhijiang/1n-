package com.example.onenprofessor.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class MyJson{
	/**
	 * ����
	 */
	public String command ="";
	/**
	 * Ŀ��ip
	 */
	public JSONArray target = new JSONArray();
	/**
	 * Ŀ��json
	 */
	public JSONObject jsonObject ;

	public MyJson(JSONObject json){
		try {
			jsonObject = json;
			command = jsonObject.getString("command");
			target = jsonObject.getJSONArray("target");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public MyJson(String command,JSONArray target){
		jsonObject = new JSONObject();
		this.command = command;
		this.target = target;
		try {
			jsonObject.put("command", command);
			jsonObject.put("target", target);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	};
}
