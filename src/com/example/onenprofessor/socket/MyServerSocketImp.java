package com.example.onenprofessor.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

import com.example.onenprofessor.ConstantValue;


public class MyServerSocketImp/* extends MySocket*/{
	private ServerSocket serverSocket;
	/*private Socket msgSocket;
	private Socket dataSocket;
	private BufferedReader msgReader;
	private BufferedWriter msgWriter;
	private InputStream dataIS;
	private OutputStream dataOS;*/

	public MyServerSocketImp() {
		// TODO Auto-generated constructor stub
		try {
			serverSocket = new ServerSocket(Integer.parseInt(ConstantValue.SERVER_PORT));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 开始接受socket连接
	 */
	public Socket acceptSocket(){
		try {
			Socket socket = serverSocket.accept();
			Log.i("1","accpet socket:"+ socket.getInetAddress().toString());
			return socket;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void end(){
		try {
			if (serverSocket!=null) 
				serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
/*	@Override
	protected boolean sendMsg(String msg) {
		// TODO Auto-generated method stub
		try {
			msgWriter.write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected String getMsg() {
		// TODO Auto-generated method stub
		try {
			return msgReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected boolean sendData(InputStream is) {
		// TODO Auto-generated method stub
		try {
			int length = 0;
			byte[] buffer = new byte[ConstantValue.IS_OS_CACHE];
			while((length = is.read(buffer))!=-1){
				dataOS.write(buffer,0,length);
			}
			dataOS.flush();
			is.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected InputStream getData() {
		// TODO Auto-generated method stub
		return dataIS;
	}
	@Override
	protected void end() {
		// TODO Auto-generated method stub
		try {
			msgWriter.close();
			msgReader.close();
			dataIS.close();
			dataOS.close();
			msgSocket.close();
			dataSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 */

