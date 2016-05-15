package com.example.onenprofessor.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.R;
import com.example.onenprofessor.control.ServerWifiSharedControl;
import com.example.onenprofessor.dialog.OpenFileDialog;
import com.example.onenprofessor.dialog.OpenFileDialog.CallbackBundle;
import com.example.onenprofessor.socket.MyServerSocketController;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ServerWifiSharedFragment extends Fragment {
	private Button btOpenFile;
	private String filePath;
	private Context context;
	private MyServerSocketController myServerSocketController;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ConstantValue.SEND_FILE:
				String fileName = (String) msg.obj;
				ServerWifiSharedControl.sendToClient(myServerSocketController,fileName);
				break;

			default:
				break;
			}
		};
	};
	public ServerWifiSharedFragment(Context context,MyServerSocketController myServerSocketController) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.myServerSocketController = myServerSocketController;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_server_wifishared,container,false);
		btOpenFile = (Button) v.findViewById(R.id.bt_send_data);
		btOpenFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String path = openFile();
			}
		});
		return v;
	}
	/**
	 * 打开选择文件列表返回文件位置
	 * @return
	 */
	public String openFile(){
		
		Map<String, Integer> images = new HashMap<String, Integer>();  
		// 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹  
		images.put(OpenFileDialog.sRoot, android.R.drawable.ic_menu_search);   // 根目录图标  
		images.put(OpenFileDialog.sParent,android.R.drawable.ic_menu_revert);    //返回上一层的图标  
		images.put(OpenFileDialog.sFolder, android.R.drawable.sym_contact_card);   //文件夹图标  
		images.put(OpenFileDialog.sEmpty,android.R.drawable.ic_menu_upload);  
		OpenFileDialog.createDialog(0, context, "选择文件", new CallbackBundle() {

			@Override
			public void callback(Bundle bundle) {
				// TODO Auto-generated method stub
				filePath = bundle.getString("path");  
				Message msg = new Message();
				msg.what = ConstantValue.SEND_FILE;
				msg.obj = filePath;
				handler.sendMessage(msg);
			}
		}, null, images).show();
		return filePath;
	}
}
