package com.example.onenprofessor.control;

import org.json.JSONObject;

import com.example.onenprofessor.json.OpenGetDataSocketJson;
import com.example.onenprofessor.minterface.OnTranSocket;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;

public class OpenGetDataSocketControl {
	/**
	 * ���ܵ�ָ��ʱ���ͻ���Ӧ�ÿ����µ�data���������߳����ӵ���������������
	 */
	public static void beClient(MyClientSocketController myClientSocketController,OnTranSocket onTranSocket){
	}
	/**
	 * ��Ҫ�����ͻ���get������,��������
	 */
	public static void sendCommandToClient(MyServerSocketController myServerSocketController){
		OpenGetDataSocketJson json = new OpenGetDataSocketJson();
		myServerSocketController.sendMsg(json.jsonObject);
	}
}
