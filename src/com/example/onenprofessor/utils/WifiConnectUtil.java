package com.example.onenprofessor.utils;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiConnectUtil {
	//单例模式
	private static WifiConnectUtil instance;
	private WifiConnectUtil(Context context){
		this.context = context;
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	};
	public static WifiConnectUtil getInstance(Context context){
		if(instance==null){
			//新建
			instance = new WifiConnectUtil(context);
		}
		return instance;
	}
	//wifi管理
	private  ConnectivityManager connManager;
	private Context context;
	private WifiManager wifiManager;

	public enum WIFI_AP_STATE {
		WIFI_AP_STATE_DISABLING, WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING,  WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED
	}

	/**判断热点开启状态*/
	public boolean isWifiApEnabled() {
		return getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
	}

	private WIFI_AP_STATE getWifiApState(){
		int tmp;
		try {
			Method method = wifiManager.getClass().getMethod("getWifiApState");
			tmp = ((Integer) method.invoke(wifiManager));
			// Fix for Android 4
			if (tmp > 10) {
				tmp = tmp - 10;
			}
			return WIFI_AP_STATE.class.getEnumConstants()[tmp];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
		}
	}
	/**
	 * wifi热点开关
	 * @param name 设置名称
	 * @param pw 密码
	 * @param enable 开启/关闭
	 * @return
	 */
	public boolean setWifiApEnabled(String name,String pw,boolean enable) {  
		//wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi 
		wifiManager.setWifiEnabled(false);  

		//热点的配置类  
		WifiConfiguration config = new WifiConfiguration();  
		//配置热点的名称(可以在名字后面加点随机数什么的)  
		config.SSID = name;     
		//配置热点的密码 
		config.preSharedKey = pw;      
		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);    
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);                          
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                          
		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                     
		config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);                       
		config.status = WifiConfiguration.Status.ENABLED;    
		//通过反射调用设置热点  
		Method method = null;
		try {
			method = wifiManager.getClass().getMethod(  
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			return (Boolean) method.invoke(wifiManager, config, enable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;  

		}
	} 
	/**
	 * 判断wifi是否正在连接
	 * @return
	 */
	public boolean checkWifiConnect(){
		return wifiManager.WIFI_STATE_ENABLED == wifiManager.getWifiState();

	}
	/**
	 * 开关wifi
	 * @return
	 */
	public boolean setWifiEnabled(boolean enabled){

		return wifiManager.setWifiEnabled(enabled);
	}

	public enum WifiCipherType  
	{  
		WIFICIPHER_WEP,WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID  
	}  



	/**
	 * 连接wifi
	 * @param SSID
	 * @param pw
	 * @return
	 */
	public boolean connectWifi(String SSID,String pw){
		WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, pw,WifiCipherType.WIFICIPHER_WPA);  
		//  
		if(wifiConfig == null)  
		{  
			return false;  
		}  

		WifiConfiguration tempConfig = this.IsExsits(SSID);  

		if(tempConfig != null)  
		{  
			wifiManager.removeNetwork(tempConfig.networkId);  
		}  
		int netID = wifiManager.addNetwork(wifiConfig);

		return  wifiManager.enableNetwork(netID, true);
	}



	private WifiConfiguration CreateWifiInfo(String SSID, String Password,WifiCipherType Type)  
	{  
		WifiConfiguration config = new WifiConfiguration();

		config.allowedAuthAlgorithms.clear();  
		config.allowedGroupCiphers.clear();  
		config.allowedKeyManagement.clear();  
		config.allowedPairwiseCiphers.clear();  
		config.allowedProtocols.clear();  
		config.SSID = "\"" + SSID + "\"";    
		if(Type == WifiCipherType.WIFICIPHER_NOPASS||Password.equals(""))  
		{  
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
			return config;
		}  
		else if(Type == WifiCipherType.WIFICIPHER_WEP)  
		{  
			config.preSharedKey = "\""+Password+"\"";   
			config.hiddenSSID = true;    
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);  
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
			config.wepTxKeyIndex = 0;  
		}  
		else if(Type == WifiCipherType.WIFICIPHER_WPA) {
			config.preSharedKey = "\""+Password+"\"";      
/*			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);    
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP); */                         
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                          
/*			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);  */                   
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);                       
			config.status = WifiConfiguration.Status.ENABLED;    
		}
		else {
			return null;
		}
		return config;  
	}

	//查看以前是否也配置过这个网络  
	private WifiConfiguration IsExsits(String SSID)  
	{  
		List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();  
		for (WifiConfiguration existingConfig : existingConfigs)   
		{ 
			if(existingConfig.SSID!=null){
				if (existingConfig.SSID.equals("\""+SSID+"\""))  
				{  
					return existingConfig;  
				} 
			}
		}  
		return null;   
	}  
	public boolean isWiFiActive( ) {   
	       NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	       return mWifi.isConnected();
    }  
}



