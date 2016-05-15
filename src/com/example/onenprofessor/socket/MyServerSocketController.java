package com.example.onenprofessor.socket;

import java.io.InputStream;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.minterface.OnTranSocket;
import com.example.onenprofessor.utils.JsonArrayUtils;
import com.example.onenprofessor.utils.UseControlUtils;

public class MyServerSocketController {
	private ThreadPoolExecutor threadPoolExecutor;
	private Handler handler;
	private boolean isGetMsgRunning;
	private boolean isAcceptRunning;
	private boolean isClientDataGetReady;
	public List<MyClientSocketBean> clientList = Collections.synchronizedList(new ArrayList<MyClientSocketBean>());
	private MyServerSocketImp myServerSocketImp = new MyServerSocketImp();
	public MyServerSocketController(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		isAcceptRunning = true;
		isGetMsgRunning = true;
		isClientDataGetReady = false;
		threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	}
	/**
	 * �����߳�ѭ������socket����
	 */
	public void startAcceptSocket(){
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(isAcceptRunning){
					Socket socket = myServerSocketImp.acceptSocket();
					if(socket!=null)
						addSocketToList(socket);
				}
			}
		});
	}
	/**
	 * ��socket�����жϷ���list������
	 * @param socket
	 */
	public void addSocketToList(Socket socket){
		for(MyClientSocketBean client : clientList){
			if(socket.getInetAddress().toString().equals(client.getIp())){
				//Ŀ��ip�����ڼ�����,�ж�Ϊdatasocket
				Log.i("1","DataSocketToList"+socket.getInetAddress());
				client.setDataSocket(socket);
				return;
			}
		}
		//Ŀ��ip���ڼ����У��½�bean���ж�Ϊmsgsocket
		Log.i("1","newBeanAddToList"+socket.getInetAddress().toString());
		MyClientSocketBean bean = new MyClientSocketBean();
		bean.setMsgSocket(socket);
		bean.setIp(socket.getInetAddress().toString());
		getMsg(bean);//���������ӵĽ����߳�
		clientList.add(bean);
		handler.sendEmptyMessage(ConstantValue.UPDTATE_SERVER);
	}
	/**
	 * �������˷�������
	 * @param json
	 */
	public void sendMsg(final JSONObject json){
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//����json������jsonָ������
				for(MyClientSocketBean bean : clientList){
					bean.sendMsg(json);
				}
			}
		});
	}
	/**
	 * �����߳�ѭ���ĵ���Ϣ
	 * @param client
	 * @return
	 */
	public JSONObject getMsg(final MyClientSocketBean client){
		threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(isGetMsgRunning){
					JSONObject json = client.getMsg();
					//�õ�msgָ�����ָ���control
					UseControlUtils.selectControlByCommand(handler, json, true, MyServerSocketController.this, client,null);
					//ÿ��client��һ���߳�ÿ��100msɨ��һ��
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		return null;
	}
	/**
	 * �������߳�����������
	 * @return null����
	 */
	public void GetData(final MyClientSocketBean bean,final OnTranSocket onTranSocket){
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(bean.dataSocket==null){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				onTranSocket.onServerDataGet(bean.dataIS);
			}
		});
	};

	/**
	 * ������������
	 * @return
	 */
	public void SendData(final InputStream is,final MyClientSocketBean bean){
		// TODO Auto-generated method stub
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!isClientDataGetReady&&bean.getDataSocket()==null){
					//�鿴Ŀ��datasocket�Ƿ�׼�����
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(!bean.sendData(is)){
					handler.sendEmptyMessage(ConstantValue.SNED_DATA_FAILED);
				}
			}
		});

	};
	/**
	 * ������������
	 * @return
	 */
	public void SendData(final InputStream is,JSONArray target){
		for(MyClientSocketBean bean : clientList){

			for(int i = 0 ;i<target.length();i++){
				//���bean������Ŀ��������
				if(JsonArrayUtils.isArrayContain(target, bean.getIp())){
					SendData(is, bean);
				}
			}

		}
	};
	public List<MyClientSocketBean> getClientList(){
		return clientList;
	}
	/**
	 * ����controller
	 */
	public void end(){
		isAcceptRunning = false;
		isGetMsgRunning = false;
		isClientDataGetReady = true;
		for(MyClientSocketBean bean : clientList){
			bean.end();
		}
		threadPoolExecutor.shutdownNow();
		myServerSocketImp.end();
	}

}
