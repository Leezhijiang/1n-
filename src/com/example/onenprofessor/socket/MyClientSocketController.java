package com.example.onenprofessor.socket;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.minterface.OnTranSocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ��װmysocket��ʵ���࣬���Ⱪ¶
 * @author J
 *
 */
public class MyClientSocketController {
	public MyClientSocketImp myClientSocket; 
	private ThreadPoolExecutor threadPoolExecutor;
	private List<JSONObject> mSendMsgList = Collections.synchronizedList(new ArrayList<JSONObject>());
	private boolean isGetMsgRunning;
	private boolean isSendMsgRunning;
	private Handler handler;
	public MyClientSocketController(Handler handler) {
		// TODO Auto-generated constructor stub
		threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		myClientSocket = new MyClientSocketImp();
		this.handler = handler;
		isGetMsgRunning = true;
		isSendMsgRunning = true;
	}
	/**
	 * ����socket���ӵ����������ҿ�������/�����߳�
	 */
	public void startSocketConn(){
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				Log.i("1", "CONN_SERVER_SOCKET");
				// TODO Auto-generated method stub
				myClientSocket.createMsgSocket();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				myClientSocket.createDataSocket();
				handler.sendEmptyMessage(ConstantValue.CONN_SERVER_SOCKET);
				sendMsg();
				threadPoolExecutor.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						getMsg();
					}
				});	

			}
		});

	}
	/**
	 * ���msg���б��ȴ�����
	 */
	public void insertMsgList(JSONObject json){
		mSendMsgList.add(json);
	}
	/**
	 * ���msg�б�������
	 * @param msg �����ַ���
	 * @return
	 */
	private boolean sendMsg(){
		Log.i("1", "sendMsg");
		while(isSendMsgRunning){

			if(mSendMsgList.size()>0){
				myClientSocket.sendMsg(mSendMsgList.get(0));

				//TODO control��ʵ��
				mSendMsgList.remove(0);
				//handlerˢ�½���

				//100ms���һ���Ƿ������ݵ���
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	/**
	 * �߳�ѭ����ȡ���������͹��������ݽ���Control���µľ�����ʵ��
	 */
	private void getMsg(){
		while(isGetMsgRunning){

			//TODO control��ʵ��
			try {
				JSONObject json = myClientSocket.getMsg();
				json.getString("command");
				//handlerˢ�½���
				Message msg = new Message();
				msg.obj = json;
				msg.what = ConstantValue.SERVER_GET_MSG;
				handler.sendMessage(msg);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//100ms���һ���Ƿ������ݵ���
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * �������߳�����������
	 * @return
	 */
	public void GetData(final OnTranSocket onTranSocket){
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				onTranSocket.onClientDataGet(myClientSocket.dataIS);
			}
		});		
	};

	/**
	 * ������������
	 * @return
	 */
	public boolean SendData(final InputStream is){
		// TODO Auto-generated method stub
		if(!myClientSocket.sendData(is)){
			handler.sendEmptyMessage(ConstantValue.SNED_DATA_FAILED);
		}
		return true;
	};
	/**
	 * ����controller
	 */
	public void end(){
		isGetMsgRunning = false;
		isSendMsgRunning = false;
		threadPoolExecutor.shutdownNow();
		myClientSocket.end();
	}

}
