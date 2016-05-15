package com.example.onenprofessor.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothGattServer;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.dialog.OpenFileDialog;
import com.example.onenprofessor.dialog.OpenFileDialog.CallbackBundle;
import com.example.onenprofessor.json.WifiSharedJson;
import com.example.onenprofessor.minterface.OnTranSocket;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;
import com.example.onenprofessor.utils.FileUtils;

public class ServerWifiSharedControl{
	/**
	 * 发送开启命令道客户端，客户端根据命令开启DATAGET线程准备接受数据
	 * @param myServerSocketController
	 */
	public static void sendToClient(MyServerSocketController myServerSocketController,String fileName){
		//1 命令客户端开启线程（辨认时wifishared时。自动开启data接受线程）
		/*OpenGetDataSocketControl.sendCommandToClient(myServerSocketController);*/
		//2发送fileSize，fileName到客户端
		WifiSharedJson wifiSharedJson = null;
		try {
			wifiSharedJson = new WifiSharedJson(String.valueOf(FileUtils.getFileSize(fileName)),fileName,null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		myServerSocketController.sendMsg(wifiSharedJson.jsonObject);
		//3发送文件
		for(MyClientSocketBean bean : myServerSocketController.clientList){
			try {
				myServerSocketController.SendData(FileUtils.getFileInputStream(fileName), bean);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 当客户端接受到wifishared消息时
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static void beClient(MyClientSocketController myClientSocketController,JSONObject json) throws JSONException, IOException{
		//1读取fileSize和fileName,filePath
		final String fileSize = json.getString("fileSize");
		String filePath = json.getString("fileName");
		String fileName = (String) filePath.subSequence(filePath.lastIndexOf("/")+1, filePath.length());
		//2根据fileName创建文件
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ConstantValue.DIR_NAME;
		final File file = new File(path+"/"+fileName);

		if(!file.exists()){
			file.createNewFile();
		}
		//3开启通道后，存储文件逻辑
		OnTranSocket onTranSocket = new OnTranSocket() {
			@Override
			public void onServerDataGet(InputStream is) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClientDataGet(InputStream is) {
				// TODO Auto-generated method stub

				FileOutputStream fos;
				try {
					fos = new FileOutputStream(file);
					//更新界面
					int fileSzie =  Integer.parseInt(fileSize);
					byte[] buffer = new byte[fileSzie];
					int length = 0;
					int size = 0;
					/*for(int i= 0;i<count;i++){
						Log.i("1", i+"/"+count);*/
					while(size < fileSzie){
						length = is.read(buffer);
						size += length;
						fos.write(buffer,0,length);
					}
					/*	}*/
					fos.flush();

					fos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		//4开启get线程
		OpenGetDataSocketControl.beClient(myClientSocketController, onTranSocket);

	}
}
