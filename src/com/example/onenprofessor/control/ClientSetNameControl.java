package com.example.onenprofessor.control;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.json.SetNameJson;
import com.example.onenprofessor.socket.MyClientSocketController;

public class ClientSetNameControl {
	public static void sendNameToServer(MyClientSocketController myClientSocketController,String name){
		SetNameJson nameJson = new SetNameJson(null,name);
		myClientSocketController.insertMsgList(nameJson.jsonObject);
	}
	public static void beServer(MyClientSocketBean bean,Handler handler,JSONObject json){
		SetNameJson nameJson = new SetNameJson(json);
		bean.setName(nameJson.name);
		handler.sendEmptyMessage(ConstantValue.UPDTATE_SERVER);
	}
}
