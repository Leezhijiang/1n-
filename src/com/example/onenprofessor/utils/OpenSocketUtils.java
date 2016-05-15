package com.example.onenprofessor.utils;

import java.util.List;
import java.util.Random;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.engine.WifiEngine;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;
import com.example.onenprofessor.socket.MyServerSocketImp;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.util.Log;

/**
 * 给activity调用的高度封装类
 * @author Jiang
 *
 */
public class OpenSocketUtils {
	private Random random;
	private WifiConnectUtil wifiConnectUtil;
	private WifiEngine wifiEngine;
	boolean isConnct;
	private List<ScanResult> wifiInfos;
	private MyClientSocketController myClientSocketController;
	private MyServerSocketController myServerSocketController;
	private Handler handler;
	/**
	 * 开启客户端wifi并且，开启指令线程连接socket到192.168.43.1 5053(ip\prot)，
	 * @return 开启是否成功
	 */
	public OpenSocketUtils(Context context,Handler handler,MyClientSocketController myClientSocketController, MyServerSocketController myServerSocketController) {
		// TODO Auto-generated constructor stub
		wifiConnectUtil = WifiConnectUtil.getInstance(context);
		random = new Random();
		wifiEngine = WifiEngine.getInstance(context);
		this.handler = handler;
		this.myClientSocketController = myClientSocketController;
		this.myServerSocketController = myServerSocketController;
	}
	public boolean openClientSocket(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(wifiConnectUtil.isWiFiActive()){
					//已经连接wifi断掉重连
					wifiConnectUtil.setWifiEnabled(false);
				}else if(wifiConnectUtil.isWifiApEnabled()){
					wifiConnectUtil.setWifiApEnabled(null, null, false);
				}
				wifiConnectUtil.setWifiEnabled(true);
				isConnct = false;
				while(!isConnct){
					wifiInfos = wifiEngine.getWifiInfos();
					for(ScanResult info : wifiInfos){
						if(info.SSID.contains(ConstantValue.SSID)){
							//找到目标wifi,退出循环,开启socket
							Log.i("1", "找到目标wifi");
							wifiConnectUtil.connectWifi(info.SSID, ConstantValue.pw);
							while(!wifiConnectUtil.isWiFiActive()){
								//每隔100ms检测一次wifi是否开启
								Log.i("1", "没有开启wifi再次检测");
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							//延迟3s中发送连接socket
							Log.i("1", "开启wifi发送handler");
							handler.sendEmptyMessageDelayed(ConstantValue.CLIENT_CONN_WIFI, 3000);
							isConnct = true;
							break;
						}
					}
				}
			}
		}).start();
		return false;
	};
	/**
	 * 开启服务器热点并且等待客户端连接，开启指令线程
	 * @return 开启是否成功
	 */
	public boolean openServerSocket(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(wifiConnectUtil.isWiFiActive()){
					//已经连接wifi断掉重连
					wifiConnectUtil.setWifiEnabled(false);
				}else if(wifiConnectUtil.isWifiApEnabled()){
					wifiConnectUtil.setWifiApEnabled(null, null, false);
				}
				wifiConnectUtil.setWifiApEnabled(ConstantValue.SSID+random.nextInt(99),ConstantValue.pw,true);
				while(!wifiConnectUtil.isWifiApEnabled()){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(ConstantValue.SERVER_OPEN_AP);
			}
		}).start();
		return false;
	};
	public void end(){
		isConnct =true;
	}
}
