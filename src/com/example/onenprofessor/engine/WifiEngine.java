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
	//����ģʽ
	private static WifiEngine instance;
	private WifiEngine(Context context){
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	};
	public static WifiEngine getInstance(Context context){
		if(instance==null){
			//�½�
			instance = new WifiEngine(context);
		}
		return instance;
	}
	/**
	 * ��ȡ��ǰ���ӵ��ȵ���豸ip
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
	 * �õ�wifiɨ����Ϣ
	 * @return
	 */
	public List<ScanResult> getWifiInfos(){
		return wifiManager.getScanResults();
	}
}
