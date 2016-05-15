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
 * 封装mysocket的实现类，对外暴露
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
	 * 开启socket连接到服务器并且开启接收/发送线程
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
	 * 添加msg到列表，等待传送
	 */
	public void insertMsgList(JSONObject json){
		mSendMsgList.add(json);
	}
	/**
	 * 检查msg列表传送数据
	 * @param msg 数据字符串
	 * @return
	 */
	private boolean sendMsg(){
		Log.i("1", "sendMsg");
		while(isSendMsgRunning){

			if(mSendMsgList.size()>0){
				myClientSocket.sendMsg(mSendMsgList.get(0));

				//TODO control类实现
				mSendMsgList.remove(0);
				//handler刷新界面

				//100ms检测一次是否有数据到来
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
	 * 线程循环读取服务器发送过来的数据交给Control包下的具体类实现
	 */
	private void getMsg(){
		while(isGetMsgRunning){

			//TODO control类实现
			try {
				JSONObject json = myClientSocket.getMsg();
				json.getString("command");
				//handler刷新界面
				Message msg = new Message();
				msg.obj = json;
				msg.what = ConstantValue.SERVER_GET_MSG;
				handler.sendMessage(msg);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//100ms检测一次是否有数据到来
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 开启新线程来接受数据
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
	 * 开启传送数据
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
	 * 结束controller
	 */
	public void end(){
		isGetMsgRunning = false;
		isSendMsgRunning = false;
		threadPoolExecutor.shutdownNow();
		myClientSocket.end();
	}

}
