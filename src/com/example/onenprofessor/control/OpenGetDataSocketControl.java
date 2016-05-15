package com.example.onenprofessor.control;

import org.json.JSONObject;

import com.example.onenprofessor.json.OpenGetDataSocketJson;
import com.example.onenprofessor.minterface.OnTranSocket;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;

public class OpenGetDataSocketControl {
	/**
	 * 当受到指令时，客户端应该开启新的data接受数据线程连接到服务器（废弃）
	 */
	public static void beClient(MyClientSocketController myClientSocketController,OnTranSocket onTranSocket){
	}
	/**
	 * 当要开启客户端get服务是,发送命令
	 */
	public static void sendCommandToClient(MyServerSocketController myServerSocketController){
		OpenGetDataSocketJson json = new OpenGetDataSocketJson();
		myServerSocketController.sendMsg(json.jsonObject);
	}
}
