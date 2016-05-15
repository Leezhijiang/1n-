package com.example.onenprofessor.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.example.onenprofessor.utils.WifiConnectUtil;

public class WifiEngine {
	private WifiManager wifiManager;
	//单例模式
	private static WifiEngine instance;
	private WifiEngine(Context context){
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	};
	public static WifiEngine getInstance(Context context){
		if(instance==null){
			//新建
			instance = new WifiEngine(context);
		}
		return instance;
	}
	/**
	 * 读取当前连接到热点的设备ip
	 *
	 */
	public ArrayList<String> getConnectedIP() {
		ArrayList<String> connectedIP = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split(" +");
				if (splitted != null && splitted.length >= 4) {
					String ip = splitted[0];
					connectedIP.add(ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connectedIP;
	}
	/**
	 * 得到wifi扫描信息
	 * @return
	 */
	public List<ScanResult> getWifiInfos(){
		return wifiManager.getScanResults();
	}
}
