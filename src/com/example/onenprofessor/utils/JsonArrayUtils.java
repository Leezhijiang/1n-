package com.example.onenprofessor.utils;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonArrayUtils {
	public static boolean isArrayContain(JSONArray array,String target){
		for(int i = 0;i<array.length();i++){
			try {
				if(array.getString(i).equals(target)){
					//�ҵ�Ŀ��
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
