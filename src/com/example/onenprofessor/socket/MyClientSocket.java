package com.example.onenprofessor.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.onenprofessor.ConstantValue;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

public class MyClientSocket extends Thread/*extends BroadcastReceiver*/{
	public Socket socket;
	private Context context;
	private SharedPreferences sp;
	private String name;
	public BufferedWriter writer;
	private Handler handler;
	private boolean isStillAlive;
	private List<String> mMsgList;
	public MyClientSocket(Context context,Handler handler) {
		// TODO Auto-generated constructor stub
		mMsgList = Collections.synchronizedList(new ArrayList<String>());
		isStillAlive = true;
		this.context = context;
		this.handler = handler;

		sp  = context.getSharedPreferences("config", context.MODE_PRIVATE);
		name = sp.getString("name", "null");
	}
	@Override
	public void run() {
		mMsgList.add(name);

		// TODO Auto-generated method stub\

		try {
			socket = new Socket(/*ConstantValue.SERVER_IP, Integer.parseInt(ConstantValue.SERVER_PORT)*/);
			socket.connect(new InetSocketAddress(ConstantValue.SERVER_IP, Integer.parseInt(ConstantValue.SERVER_PORT)), 5000);
			Thread.sleep(1000);
			//���������߳�
			System.out.println("startSocket!");
			startSendThread();
			while(isStillAlive){
				//500ms���һ��socketͨ���Ƿ���������
				preRead();
				/*	String line = ""; 
				line = reader.readLine();//��������
				System.out.println("line="+line);*/

				onRead(socket.getInputStream());
				afterRead();
				/*if(!StringUtils.isEmpty(line)){
					Message msg = new Message();
					msg.obj = line;
					handler.sendMessage(msg);
					System.out.println("handler�ͻ��˽��ܵ���Ϣ");

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			handler.sendEmptyMessage(ConstantValue.BAD_CONN);
			e.printStackTrace();
		} 		
		super.run();
	}


	public void startSendThread(){
		new Thread(					
				new Runnable() {
					@Override		
					public void run() {							// TODO Auto-generated method stub	
						while(isStillAlive){//ÿ��500ms���һ��mMsglist�Ƿ������ݣ�����з��͵�������			
							if(mMsgList.size()>0){								
								preSend();							
								try {									
									writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));	
									writer.write(mMsgList.get(0)+"\n");  								
									writer.flush();								
									System.out.println("out="+mMsgList.get(0));			
									onSend(socket.getOutputStream());						
									afterSend();						
								} catch (UnsupportedEncodingException e) {	
									// TODO Auto-generated catch block		
									e.printStackTrace();						
								} catch (IOException e) {						
									// TODO Auto-generated catch block			
									e.printStackTrace();					
								}									try {	
									Thread.sleep(100);						
								} catch (InterruptedException e) {		
									// TODO Auto-generated catch block			
									e.printStackTrace();					
								}								
								mMsgList.remove(0);						
							}						
						}
					}

				}).start();
	}


	/*	 */
	/** 	 * ������ȡ�߳� 	 /**/
	public void startReadThread(){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				/*	while(isStillAlive){
					//500ms���һ��socketͨ���Ƿ���������
					preRead();
				String line = ""; 
				try {
					line = reader.readLine();//��������
					System.out.println("line="+line);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					try {
						onRead(socket.getInputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.i("1", "IOEXCEPTION");
						e.printStackTrace();
					}
					afterRead();
				if(!StringUtils.isEmpty(line)){
					Message msg = new Message();
					msg.obj = line;
					handler.sendMessage(msg);
					System.out.println("handler�ͻ��˽��ܵ���Ϣ");

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
			}
		}.start();
	}

	public void sendMsgToServer(String msg){		
		mMsgList.add(msg);
	}


	public void endSocket(){			
		if(socket!=null){			
			try {				
				socket.close();			
				isStillAlive = false;		
			} catch (IOException e) {	
				// TODO Auto-generated catch block	
				e.printStackTrace();		
			}	
		}
	}

	/** 	 * �������� 	 */
	public void onSend(OutputStream os) {			
		// TODO Auto-generated method stub


	}
	/** 	 * ��ȡ���� 	 */
	public void onRead(InputStream is) {		
		// TODO Auto-generated method stub


	}
	/** 	 * �ڷ������ݵ�������ǰ���÷��� 	 */
	public void preSend() {			
		// TODO Auto-generated method stub


	}
	/** 	 * �ڷ������ݵ�����������÷��� 	 */
	public void afterSend() {		
		// TODO Auto-generated method stub

	}
	/** 	 * �ڶ�ȡ����֮�� 	 */

	public void afterRead() {			
		// TODO Auto-generated method stub


	}
	/** 	 * �ڶ�ȡ����֮ǰ 	 */
	public void preRead() {		
		// TODO Auto-generated method stub

	}
	/*	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
			NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);  
			if(info.getState().equals(NetworkInfo.State.CONNECTED)){  

				WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();   
				if(wifiInfo.getSSID().contains(ConstantValue.SSID)){
					if(!receive){
						//���ӵ�������
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						startSocket();
						Log.i("1","����socket");
						mMsgList.add(name);
						handler.sendEmptyMessage(ConstantValue.PRE_SEND);
					}
				}
			}
			}
			}*/

}

