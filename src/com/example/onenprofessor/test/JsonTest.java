package com.example.onenprofessor.test;

import org.json.JSONArray;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.onenprofessor.json.SetNameJson;

public class JsonTest extends AndroidTestCase{
	public void sendSetNameJson(){
		JSONArray array = new JSONArray();
		array.put("1");
		array.put("2");
		SetNameJson nameJson = new SetNameJson(array,"abc");
		Log.i("1", nameJson.jsonObject.toString());
	}
}
