package com.example.onenprofessor.control;

import android.os.Handler;
import android.os.Message;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.json.EndJson;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;

/**
 * ���ͻ��˻��߷�����������һ���ر�ʱ������һ�����͹ر��ź�
 * @author J
 *
 */
public class EndControl {
	/**
	 * �����пͻ��˷��͹ر��ź�
	 * @param myServerSocketController
	 */
	public static void sendToClient(MyServerSocketController myServerSocketController){
		EndJson json = new EndJson();
		myServerSocketController.sendMsg(json.jsonObject);
	}
	/**
	 * ����������͹ر��ź�
	 * @param myClientSocketController
	 */
	public static void sendToServer(MyClientSocketController myClientSocketController){
		EndJson json = new EndJson();
		myClientSocketController.insertMsgList(json.jsonObject);
	}
	/**
	 * �����������ܵ��ͻ��˷������Ĺر��ź�ʱ
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
	 * ������ν��ܵ��������������Ĺر��ź�ʱ
	 */
	public static void beClient(MyClientSocketController myClientSocketController,Handler handler){
		myClientSocketController.end();
		handler.sendEmptyMessage(ConstantValue.SERVER_END);
	}
}
