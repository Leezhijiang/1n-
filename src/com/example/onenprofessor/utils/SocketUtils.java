package com.example.onenprofessor.utils;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.onenprofessor.ConstantValue;

/**
 * ��Socket���õĸ�д��
 * @author J
 *
 */
public class SocketUtils {
	/**
	 * �½�һ��socket����
	 * @return socketʵ�����ӳɹ���null����ʧ��
	 */
	public static Socket creatSocket(){
		Socket socket = new Socket(/*ConstantValue.SERVER_IP, Integer.parseInt(ConstantValue.SERVER_PORT)*/);
		try {
			socket.connect(new InetSocketAddress(ConstantValue.SERVER_IP, Integer.parseInt(ConstantValue.SERVER_PORT)), 5000);
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return socket;
	}
}
