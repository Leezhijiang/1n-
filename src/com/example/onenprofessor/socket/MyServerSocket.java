package com.example.onenprofessor.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;








import java.util.Collections;
import java.util.List;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.ClientInfo;
import com.example.onenprofessor.utils.WifiConnectUtil;

import android.R.bool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyServerSocket {
	private ServerSocket mServer;
	private boolean isStartServer;
	private List<ClientInfo> arrayInfos;
	private Handler handler;
	/** 
	 * 消息队列，用于保存SocketServer接收来自于客户机（手机端）的消息 
	 */  
	private List<String> mMsgList = Collections.synchronizedList(new ArrayList<String>()); 

	public MyServerSocket(Handler handler){
		arrayInfos = Collections.synchronizedList(new ArrayList<ClientInfo>());
		this.handler = handler;
	}
	//线程队列，用于接收消息。每个客户机拥有一个线程，每个线程只接收发送给自己的消息
	private List<SocketThread> mThreadList = Collections.synchronizedList(new ArrayList<SocketThread>());  
	public void startSocket(final WifiConnectUtil wifiConnectUtil) {
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				isStartServer = true;
				try {  
					int prot = Integer.parseInt(ConstantValue.SERVER_PORT);//端口可以自己设置，但要和Client端的端口保持一致  
					mServer = new ServerSocket(prot/*,100,Inet4Address.getByAddress(new byte[]{(byte) 196,(byte) 192,43,1})*/);//创建一个ServerSocket  
					Socket socket = null;  
					//用一个循环来检测是否有新的客户机加入 
					startSendMessageThread();
					while(isStartServer) {  
						//accept()方法是一个阻塞的方法，调用该方法后，  
						//该线程会一直阻塞，直到有新的客户机加入，代码才会继续往下走 
						Log.i("1","等待客户端连接");
						socket = mServer.accept();  
						//有新的客户机加入后，则创建一个新的SocketThread线程对象 
						Log.i("1","客户端连接！");
						SocketThread thread = new SocketThread(socket,handler);  
						thread.start();  
						//将该线程添加到线程队列  
						mThreadList.add(thread);  
						ClientInfo info = new ClientInfo();
						info.setIp(socket.getInetAddress().toString());
						boolean chongfu = false;
						/*for(ClientInfo myInfo : arrayInfos){//查重
							if(myInfo.getIp().equals(socket.getInetAddress().toString())){
								chongfu=true;
								myInfo.init();
							}
						}*/
						if(!chongfu){
							arrayInfos.add(info);
						}
						//从列表里去除已经不存在的socket连接
						for(SocketThread socketThread : mThreadList){
							if(!socketThread.socket.isConnected()){
								mThreadList.remove(socketThread);
							}
						}
						Log.i("1", socket.getInetAddress().toString());
					}  

				} catch (Exception e) {  
					e.printStackTrace();  
					Log.i("1", "断开连接");
				}  
				super.run();
			}
		}.start();

	}  
	/** 
	 * 定义一个SocketThread类，用于接收消息 
	 * 
	 */  
	public class SocketThread extends Thread {   
		public Socket socket;//Socket用于获取输入流、输出流  
		public OutputStream os;//BufferedWriter 用于推送消息  
		public InputStream is;//BufferedReader 用于接收消息 
		private Handler handler;
		
		public SocketThread(Socket socket,Handler handler) {  
			this.socket = socket;  
			this.handler = handler;
		}  
		public void endScoket(){
			try {
				socket.close();
				os.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};
		@Override  
		public void run() {  
			super.run();  

			try {  
				//初始化BufferedReader  
				is = socket.getInputStream();
				//初始化BufferedWriter  
				os = socket.getOutputStream();
				//如果isStartServer=true，则说明SocketServer已经启动，  
				//现在需要用一个循环来不断接收来自客户机的消息，并作其他处理
				//初始化客户端在服务端的显示
				while(isStartServer){

					BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
					String msg = bfr.readLine();
					for(ClientInfo info : arrayInfos){
						if(info.getIp().equals(socket.getInetAddress().toString())){
							if(msg.equals(ConstantValue.SOCKET_PRE_READ)){
								Log.i("1",socket.getInetAddress()+"准备接受");
								info.setDesc("准备接受");
							}else if (msg.contains(ConstantValue.SOCKET_ON_READ)) {
								Log.i("1",socket.getInetAddress()+"正在接受"+msg);
								String[] progress = msg.split(";");
								for(String s : progress){
									Log.i("1", s);
								}
								if(progress.length>=3){
									info.setDesc("正在接受"+progress[2]+"/"+progress[1]);
								}else {
									info.setDesc("正在接受");
								}
							}
							else if (msg.equals(ConstantValue.SOCKET_AFTER_READ)) {
								Log.i("1",socket.getInetAddress()+"接受完毕");
								info.setDesc("接受完毕");
							}else if (msg.equals(ConstantValue.SOCKET_CLIENT_BREAK_DOWN)) {
								Log.i("1",socket.getInetAddress()+"断开连接");
								Message handlerMsg= new Message();
								handlerMsg.obj = info.getName();
								handlerMsg.what = ConstantValue.CLIENT_BREAK_DOWN;
								handler.sendMessage(handlerMsg);
								arrayInfos.remove(info);
								mThreadList.remove(this);
							}
							else {

								Log.i("1",msg);
								info.setName(msg);
							}
						}

					}
					//更新界面
					handler.sendEmptyMessage(ConstantValue.UPDTATE_SERVER);
					preRead();
					onRead(is);
					afterRead();
					Thread.sleep(500);
				}

			} catch (Exception e) {  
				e.printStackTrace();  
			}    

		}  
	}  
	/** 
	 * 发送数据用线程 
	 */  
	public void startSendMessageThread() {  
		new Thread(){  
			@Override  
			public void run() {  
				super.run();  
				try {  

					/*如果isStartServer=true，则说明SocketServer已启动， 
                    用一个循环来检测消息队列中是否有消息，如果有，则推送消息到相应的客户机*/  
					while(isStartServer) {  
						//判断消息队列中的长度是否大于0，大于0则说明消息队列不为空  
						if(mMsgList.size() > 0) {  
							//读取消息队列中的第一个消息  
							String data = mMsgList.get(0);  
							int i = 1;
							for(SocketThread to : mThreadList) {  
								int size = mThreadList.size();
								preSend(size,i);
								onSend(to.os,size,i);
								/*BufferedWriter writer = to.writer;  
								writer.write(data);
								writer.flush(); */
								afterSend(size,i);
								i += 1;
								System.out.println("消息推送");
							}  
							//每推送一条消息之后，就要在消息队列中移除该消息  
							mMsgList.remove(0);  
						}  
						Thread.sleep(200);  
					}  
				} catch (Exception e) {  
					e.printStackTrace();  
				}  
			}  
		}.start();  
	}
	/**
	 * 关闭SocketServer
	 */
	public void endScoketServer(){
		isStartServer = false;
		try {
			if(mServer !=null)
				mServer.close();
			for(SocketThread to : mThreadList) { 
				to.endScoket();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 传送数据到客户端
	 * @param data
	 */
	public void sendMsgToClient(){
		mMsgList.add("a");
	}
	/**
	 * 返回当前已经和服务器连接的客户端名称
	 * @return
	 */
	public List<ClientInfo> getClientInfos(){
		return arrayInfos;
	}
	/**
	 * 发送数据
	 */
	public void onSend(OutputStream os,int size,int now) {
		// TODO Auto-generated method stub

	}
	/**
	 * 读取数据
	 */
	public void onRead(InputStream is) {
		// TODO Auto-generated method stub

	}
	/**
	 * 在发送数据到服务器前调用方法
	 */
	public void preSend(int size,int now) {
		// TODO Auto-generated method stub

	}
	/**
	 * 在发送数据到服务器后调用方法
	 */
	public void afterSend(int size,int now) {
		// TODO Auto-generated method stub

	}
	/**
	 * 在读取数据之后
	 */
	public void afterRead() {
		// TODO Auto-generated method stub

	}
	/**
	 * 在读取数据之前
	 */
	public void preRead() {
		// TODO Auto-generated method stub

	}
}
