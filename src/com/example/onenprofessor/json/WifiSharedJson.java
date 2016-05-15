package com.example.onenprofessor.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WifiSharedJson extends MyJson{
	private String fileSize;
	private String fileName;

	public WifiSharedJson(JSONObject json) {
		super(json);
		try {
			fileSize = json.getString("fileSize");
			fileSize = json.getString("fileName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	public WifiSharedJson(String fileSize,String fileName,JSONArray target){
		super("wifishared", target);
		try {
			this.fileSize = fileSize;
			this.fileName = fileName;
			jsonObject.put("fileSize", fileSize);
			jsonObject.put("fileName", fileName);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
