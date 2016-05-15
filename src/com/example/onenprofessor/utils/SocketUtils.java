package com.example.onenprofessor.utils;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.onenprofessor.ConstantValue;

/**
 * 给Socket调用的复写类
 * @author J
 *
 */
public class SocketUtils {
	/**
	 * 新建一个socket连接
	 * @return socket实例连接成功，null连接失败
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
