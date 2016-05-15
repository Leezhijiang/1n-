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
	 * ���Ϳ���������ͻ��ˣ��ͻ��˸��������DATAGET�߳�׼����������
	 * @param myServerSocketController
	 */
	public static void sendToClient(MyServerSocketController myServerSocketController,String fileName){
		//1 ����ͻ��˿����̣߳�����ʱwifisharedʱ���Զ�����data�����̣߳�
		/*OpenGetDataSocketControl.sendCommandToClient(myServerSocketController);*/
		//2����fileSize��fileName���ͻ���
		WifiSharedJson wifiSharedJson = null;
		try {
			wifiSharedJson = new WifiSharedJson(String.valueOf(FileUtils.getFileSize(fileName)),fileName,null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		myServerSocketController.sendMsg(wifiSharedJson.jsonObject);
		//3�����ļ�
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
	 * ���ͻ��˽��ܵ�wifishared��Ϣʱ
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static void beClient(MyClientSocketController myClientSocketController,JSONObject json) throws JSONException, IOException{
		//1��ȡfileSize��fileName,filePath
		final String fileSize = json.getString("fileSize");
		String filePath = json.getString("fileName");
		String fileName = (String) filePath.subSequence(filePath.lastIndexOf("/")+1, filePath.length());
		//2����fileName�����ļ�
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ConstantValue.DIR_NAME;
		final File file = new File(path+"/"+fileName);

		if(!file.exists()){
			file.createNewFile();
		}
		//3����ͨ���󣬴洢�ļ��߼�
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
					//���½���
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
		//4����get�߳�
		OpenGetDataSocketControl.beClient(myClientSocketController, onTranSocket);

	}
}
