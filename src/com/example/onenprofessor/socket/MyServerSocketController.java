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
	 * 开启线程循环接受socket连接
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
	 * 将socket经过判断放入list集合中
	 * @param socket
	 */
	public void addSocketToList(Socket socket){
		for(MyClientSocketBean client : clientList){
			if(socket.getInetAddress().toString().equals(client.getIp())){
				//目标ip存在在集合中,判断为datasocket
				Log.i("1","DataSocketToList"+socket.getInetAddress());
				client.setDataSocket(socket);
				return;
			}
		}
		//目标ip不在集合中，新建bean，判断为msgsocket
		Log.i("1","newBeanAddToList"+socket.getInetAddress().toString());
		MyClientSocketBean bean = new MyClientSocketBean();
		bean.setMsgSocket(socket);
		bean.setIp(socket.getInetAddress().toString());
		getMsg(bean);//开启新连接的接受线程
		clientList.add(bean);
		handler.sendEmptyMessage(ConstantValue.UPDTATE_SERVER);
	}
	/**
	 * 向所有人发送命令
	 * @param json
	 */
	public void sendMsg(final JSONObject json){
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//解析json数据向json指定发送
				for(MyClientSocketBean bean : clientList){
					bean.sendMsg(json);
				}
			}
		});
	}
	/**
	 * 开启线程循环的到消息
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
					//得到msg指令，根据指令开启control
					UseControlUtils.selectControlByCommand(handler, json, true, MyServerSocketController.this, client,null);
					//每隔client开一个线程每隔100ms扫描一次
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
	 * 开启新线程来接受数据
	 * @return null出错
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
	 * 开启发送数据
	 * @return
	 */
	public void SendData(final InputStream is,final MyClientSocketBean bean){
		// TODO Auto-generated method stub
		threadPoolExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!isClientDataGetReady&&bean.getDataSocket()==null){
					//查看目标datasocket是否准备完毕
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
	 * 开启发送数据
	 * @return
	 */
	public void SendData(final InputStream is,JSONArray target){
		for(MyClientSocketBean bean : clientList){

			for(int i = 0 ;i<target.length();i++){
				//如果bean存在在目标中则发送
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
	 * 结束controller
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
