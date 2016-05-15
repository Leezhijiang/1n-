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
	 * @param handler 刷新界面用handler
	 * @param json	接受到的json
	 * @param isServer	是否是Server
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
		//客户端请求发送数据
		if(command.equals("wifishared")){

		}
		//客户端请求设置姓名
		else if (command.equals("setname")) {//client向服务器申请设置名称
			ClientSetNameControl.beServer(bean, handler, json);
		}
		//发送关闭请求
		else if (command.equals("end")) {
			if(isServer){
				EndControl.beServer(myServerSocketController, bean, handler);
			}else {
				EndControl.beClient(myClientSocketController, handler);
			}
		}
		//服务器命令客户端连接datasocket
		else if (command.equals("opendatasocket")) {
		}
	}
}
