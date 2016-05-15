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
	 * ��Ϣ���У����ڱ���SocketServer���������ڿͻ������ֻ��ˣ�����Ϣ 
	 */  
	private List<String> mMsgList = Collections.synchronizedList(new ArrayList<String>()); 

	public MyServerSocket(Handler handler){
		arrayInfos = Collections.synchronizedList(new ArrayList<ClientInfo>());
		this.handler = handler;
	}
	//�̶߳��У����ڽ�����Ϣ��ÿ���ͻ���ӵ��һ���̣߳�ÿ���߳�ֻ���շ��͸��Լ�����Ϣ
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
					int prot = Integer.parseInt(ConstantValue.SERVER_PORT);//�˿ڿ����Լ����ã���Ҫ��Client�˵Ķ˿ڱ���һ��  
					mServer = new ServerSocket(prot/*,100,Inet4Address.getByAddress(new byte[]{(byte) 196,(byte) 192,43,1})*/);//����һ��ServerSocket  
					Socket socket = null;  
					//��һ��ѭ��������Ƿ����µĿͻ������� 
					startSendMessageThread();
					while(isStartServer) {  
						//accept()������һ�������ķ��������ø÷�����  
						//���̻߳�һֱ������ֱ�����µĿͻ������룬����Ż���������� 
						Log.i("1","�ȴ��ͻ�������");
						socket = mServer.accept();  
						//���µĿͻ���������򴴽�һ���µ�SocketThread�̶߳��� 
						Log.i("1","�ͻ������ӣ�");
						SocketThread thread = new SocketThread(socket,handler);  
						thread.start();  
						//�����߳���ӵ��̶߳���  
						mThreadList.add(thread);  
						ClientInfo info = new ClientInfo();
						info.setIp(socket.getInetAddress().toString());
						boolean chongfu = false;
						/*for(ClientInfo myInfo : arrayInfos){//����
							if(myInfo.getIp().equals(socket.getInetAddress().toString())){
								chongfu=true;
								myInfo.init();
							}
						}*/
						if(!chongfu){
							arrayInfos.add(info);
						}
						//���б���ȥ���Ѿ������ڵ�socket����
						for(SocketThread socketThread : mThreadList){
							if(!socketThread.socket.isConnected()){
								mThreadList.remove(socketThread);
							}
						}
						Log.i("1", socket.getInetAddress().toString());
					}  

				} catch (Exception e) {  
					e.printStackTrace();  
					Log.i("1", "�Ͽ�����");
				}  
				super.run();
			}
		}.start();

	}  
	/** 
	 * ����һ��SocketThread�࣬���ڽ�����Ϣ 
	 * 
	 */  
	public class SocketThread extends Thread {   
		public Socket socket;//Socket���ڻ�ȡ�������������  
		public OutputStream os;//BufferedWriter ����������Ϣ  
		public InputStream is;//BufferedReader ���ڽ�����Ϣ 
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
				//��ʼ��BufferedReader  
				is = socket.getInputStream();
				//��ʼ��BufferedWriter  
				os = socket.getOutputStream();
				//���isStartServer=true����˵��SocketServer�Ѿ�������  
				//������Ҫ��һ��ѭ�������Ͻ������Կͻ�������Ϣ��������������
				//��ʼ���ͻ����ڷ���˵���ʾ
				while(isStartServer){

					BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
					String msg = bfr.readLine();
					for(ClientInfo info : arrayInfos){
						if(info.getIp().equals(socket.getInetAddress().toString())){
							if(msg.equals(ConstantValue.SOCKET_PRE_READ)){
								Log.i("1",socket.getInetAddress()+"׼������");
								info.setDesc("׼������");
							}else if (msg.contains(ConstantValue.SOCKET_ON_READ)) {
								Log.i("1",socket.getInetAddress()+"���ڽ���"+msg);
								String[] progress = msg.split(";");
								for(String s : progress){
									Log.i("1", s);
								}
								if(progress.length>=3){
									info.setDesc("���ڽ���"+progress[2]+"/"+progress[1]);
								}else {
									info.setDesc("���ڽ���");
								}
							}
							else if (msg.equals(ConstantValue.SOCKET_AFTER_READ)) {
								Log.i("1",socket.getInetAddress()+"�������");
								info.setDesc("�������");
							}else if (msg.equals(ConstantValue.SOCKET_CLIENT_BREAK_DOWN)) {
								Log.i("1",socket.getInetAddress()+"�Ͽ�����");
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
					//���½���
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
	 * �����������߳� 
	 */  
	public void startSendMessageThread() {  
		new Thread(){  
			@Override  
			public void run() {  
				super.run();  
				try {  

					/*���isStartServer=true����˵��SocketServer�������� 
                    ��һ��ѭ���������Ϣ�������Ƿ�����Ϣ������У���������Ϣ����Ӧ�Ŀͻ���*/  
					while(isStartServer) {  
						//�ж���Ϣ�����еĳ����Ƿ����0������0��˵����Ϣ���в�Ϊ��  
						if(mMsgList.size() > 0) {  
							//��ȡ��Ϣ�����еĵ�һ����Ϣ  
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
								System.out.println("��Ϣ����");
							}  
							//ÿ����һ����Ϣ֮�󣬾�Ҫ����Ϣ�������Ƴ�����Ϣ  
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
	 * �ر�SocketServer
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
	 * �������ݵ��ͻ���
	 * @param data
	 */
	public void sendMsgToClient(){
		mMsgList.add("a");
	}
	/**
	 * ���ص�ǰ�Ѿ��ͷ��������ӵĿͻ�������
	 * @return
	 */
	public List<ClientInfo> getClientInfos(){
		return arrayInfos;
	}
	/**
	 * ��������
	 */
	public void onSend(OutputStream os,int size,int now) {
		// TODO Auto-generated method stub

	}
	/**
	 * ��ȡ����
	 */
	public void onRead(InputStream is) {
		// TODO Auto-generated method stub

	}
	/**
	 * �ڷ������ݵ�������ǰ���÷���
	 */
	public void preSend(int size,int now) {
		// TODO Auto-generated method stub

	}
	/**
	 * �ڷ������ݵ�����������÷���
	 */
	public void afterSend(int size,int now) {
		// TODO Auto-generated method stub

	}
	/**
	 * �ڶ�ȡ����֮��
	 */
	public void afterRead() {
		// TODO Auto-generated method stub

	}
	/**
	 * �ڶ�ȡ����֮ǰ
	 */
	public void preRead() {
		// TODO Auto-generated method stub

	}
}
