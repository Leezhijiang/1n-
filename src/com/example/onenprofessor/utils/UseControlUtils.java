package com.example.onenprofessor.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.control.ClientSetNameControl;
import com.example.onenprofessor.control.EndControl;
import com.example.onenprofessor.control.OpenGetDataSocketControl;
import com.example.onenprofessor.control.ServerWifiSharedControl;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;

public class UseControlUtils {
	/**
	 * 
	 * @param handler ˢ�½�����handler
	 * @param json	���ܵ���json
	 * @param isServer	�Ƿ���Server
	 * @param myServerSocketController
	 * @param bean
	 * @param myClientSocketController
	 */
	public static void selectControlByCommand(Handler handler,JSONObject json,boolean isServer,MyServerSocketController myServerSocketController,MyClientSocketBean bean,MyClientSocketController myClientSocketController){
		String command = null;
		Log.i("1", json.toString());
		try {
			command = json.getString("command");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�ͻ�������������
		if(command.equals("wifishared")){

		}
		//�ͻ���������������
		else if (command.equals("setname")) {//client�������������������
			ClientSetNameControl.beServer(bean, handler, json);
		}
		//���͹ر�����
		else if (command.equals("end")) {
			if(isServer){
				EndControl.beServer(myServerSocketController, bean, handler);
			}else {
				EndControl.beClient(myClientSocketController, handler);
			}
		}
		//����������ͻ�������datasocket
		else if (command.equals("opendatasocket")) {
		}
	}
}
