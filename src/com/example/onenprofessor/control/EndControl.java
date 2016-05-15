package com.example.onenprofessor.control;

import android.os.Handler;
import android.os.Message;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.json.EndJson;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;

/**
 * 当客户端或者服务器其中有一方关闭时，向另一方发送关闭信号
 * @author J
 *
 */
public class EndControl {
	/**
	 * 向所有客户端发送关闭信号
	 * @param myServerSocketController
	 */
	public static void sendToClient(MyServerSocketController myServerSocketController){
		EndJson json = new EndJson();
		myServerSocketController.sendMsg(json.jsonObject);
	}
	/**
	 * 向服务器发送关闭信号
	 * @param myClientSocketController
	 */
	public static void sendToServer(MyClientSocketController myClientSocketController){
		EndJson json = new EndJson();
		myClientSocketController.insertMsgList(json.jsonObject);
	}
	/**
	 * 当服务器接受到客户端发送来的关闭信号时
	 */
	public static void beServer(MyServerSocketController myServerSocketController,MyClientSocketBean bean,Handler handler){
		myServerSocketController.clientList.remove(bean);
		bean.end();
		Message msg = new Message();
		msg.obj = bean;
		msg.what = ConstantValue.CLIENT_END;
		handler.sendMessage(msg);
	}
	/**
	 * 当湖库段接受到服务器发送来的关闭信号时
	 */
	public static void beClient(MyClientSocketController myClientSocketController,Handler handler){
		myClientSocketController.end();
		handler.sendEmptyMessage(ConstantValue.SERVER_END);
	}
}
