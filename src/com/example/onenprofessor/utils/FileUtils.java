package com.example.onenprofessor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

import android.os.Environment;
import android.os.Handler;

import com.example.onenprofessor.ConstantValue;

public class FileUtils {
	/**
	 * 根据参数创建文件
	 * @param is 文件输入流
	 * @param json 另一端socket传来的msg
	 * @param hanlder 更新界面
	 */
	public static void reciveFile(InputStream is,JSONObject json,Handler handler){
		try {
			String fileName = json.getString("filename");
			String fileSzie = json.getString("filesize");
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+com.example.onenprofessor.ConstantValue.DIR_NAME;
			File file = new File(path+"/"+fileName);

			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			//更新界面
			handler.sendEmptyMessage(ConstantValue.ON_READ);
			int intfileSzie =  Integer.parseInt(fileSzie);
			byte[] buffer = new byte[intfileSzie];
			int length = 0;
			int size = 0;
			//向另一端socket发送正在接受状态

			while(size < intfileSzie){
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
	public static InputStream getFileInputStream(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		FileInputStream fis;
		fis = new FileInputStream(file);
		return fis;
	}
	public static long getFileSize(String fileName) throws IOException{
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		return fis.getChannel().size();
	}
}
