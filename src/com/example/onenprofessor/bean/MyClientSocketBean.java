package com.example.onenprofessor.bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import com.example.onenprofessor.socket.MyClientSocketImp;

/**
 * 存放在服务器的CLientsocket
 * @author J
 *
 */
public class MyClientSocketBean extends MyClientSocketImp{
	private String ip ="";
	private String name ="";
	private String desc ="";
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Socket getMsgSocket(){
		return msgSocket;
	}
	public void setMsgSocket(Socket socket){
		super.msgSocket = socket;
		try {
			super.msgWriter = new BufferedWriter(new OutputStreamWriter(super.msgSocket.getOutputStream()));
			super.msgReader = new BufferedReader(new InputStreamReader(super.msgSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Socket getDataSocket(){
		return dataSocket;
	}
	public void setDataSocket(Socket socket){
		super.dataSocket = socket;
		try {
			super.dataOS = super.msgSocket.getOutputStream();
			super.dataIS = super.msgSocket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void end(){
		super.end();
	}
}
