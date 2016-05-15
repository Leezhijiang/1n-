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
 * ��activity���õĸ߶ȷ�װ��
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
	 * �����ͻ���wifi���ң�����ָ���߳�����socket��192.168.43.1 5053(ip\prot)��
	 * @return �����Ƿ�ɹ�
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
					//�Ѿ�����wifi�ϵ�����
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
							//�ҵ�Ŀ��wifi,�˳�ѭ��,����socket
							Log.i("1", "�ҵ�Ŀ��wifi");
							wifiConnectUtil.connectWifi(info.SSID, ConstantValue.pw);
							while(!wifiConnectUtil.isWiFiActive()){
								//ÿ��100ms���һ��wifi�Ƿ���
								Log.i("1", "û�п���wifi�ٴμ��");
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							//�ӳ�3s�з�������socket
							Log.i("1", "����wifi����handler");
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
	 * �����������ȵ㲢�ҵȴ��ͻ������ӣ�����ָ���߳�
	 * @return �����Ƿ�ɹ�
	 */
	public boolean openServerSocket(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(wifiConnectUtil.isWiFiActive()){
					//�Ѿ�����wifi�ϵ�����
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
