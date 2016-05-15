package com.example.onenprofessor.json;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenGetDataSocketJson extends MyJson {

	public OpenGetDataSocketJson(JSONObject json) {
		super(json);
		// TODO Auto-generated constructor stub
	}

	public OpenGetDataSocketJson(JSONArray target) {
		super("opengetdatasocket",target);
	}
	/**
	 * ·¢ËÍÃüÁî²»°üº¬target
	 */
	public OpenGetDataSocketJson() {
		super("opengetdatasocket",null);
	}
}
