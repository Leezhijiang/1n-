package com.example.onenprofessor.socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.utils.SocketUtils;

public class MyClientSocketImp extends MySocket{
	protected Socket msgSocket;
	protected Socket dataSocket;
	public BufferedReader msgReader;
	public BufferedWriter msgWriter;
	public InputStream dataIS;
	public OutputStream dataOS;

	public MyClientSocketImp() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 开启指令socket连接到服务器
	 * @return 是否连接成功
	 */
	public boolean createMsgSocket(){
		msgSocket = SocketUtils.creatSocket();

		try {
			msgWriter = new BufferedWriter(new OutputStreamWriter(msgSocket.getOutputStream()));
			msgReader = new BufferedReader(new InputStreamReader(msgSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return msgSocket == null? false :true;
	}
	/**
	 * 开启数据socket连接到服务器
	 * @return 是否连接成功
	 */
	public boolean createDataSocket(){
		dataSocket = SocketUtils.creatSocket();
		try {
			dataOS = dataSocket.getOutputStream();
			dataIS = dataSocket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return dataSocket == null? false :true;
	}
	@Override
	protected boolean sendMsg(JSONObject json) {
		// TODO Auto-generated method stub
		try {
			if(msgWriter!=null){
				msgWriter.write(json.toString()+"\n");
				Log.i("1","sendMsg:"+json.toString());
				msgWriter.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("1","sendMsg:"+json.toString()+"failed");
			return false;
		}
		return true;
	}

	@Override
	protected JSONObject getMsg() {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject;
			try {
				String line = msgReader.readLine();
				Log.i("1", "GetMsg:"+line);
				jsonObject = new JSONObject(line);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return jsonObject;
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
			if(msgWriter!=null)
				msgWriter.close();
			if(msgReader!=null)
				msgReader.close();
			if(dataIS!=null)
				dataIS.close();
			if(dataOS!=null)
				dataOS.close();
			if(msgSocket!=null)
				msgSocket.close();
			if(dataSocket!=null)
				dataSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
