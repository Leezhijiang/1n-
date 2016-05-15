package com.example.onenprofessor.activity;

import java.lang.reflect.Constructor;

import org.json.JSONObject;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.R;
import com.example.onenprofessor.adapter.MyDrawListAdapter;
import com.example.onenprofessor.adapter.MyServerClientInfoListAdapter;
import com.example.onenprofessor.bean.MyClientSocketBean;
import com.example.onenprofessor.control.EndControl;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.socket.MyServerSocketController;
import com.example.onenprofessor.utils.OpenSocketUtils;
import com.example.onenprofessor.utils.WifiConnectUtil;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Path.Op;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class ServerActivity extends Activity {
	private ListView drawerList;
	private ListView clientInfoList;
	private MyServerClientInfoListAdapter infoAdapter;
	private MyDrawListAdapter adapter;
	private FragmentManager fm;
	private WifiConnectUtil wifiConnectUtil;
	private FragmentTransaction transaction ;
	private OpenSocketUtils openSocketUtils;
	private MyServerSocketController myServerSocketController;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ConstantValue.SERVER_GET_MSG://服务器得到msg
				Toast.makeText(ServerActivity.this,((JSONObject)msg.obj).toString(), 1000).show();
				break;
			case ConstantValue.UPDTATE_SERVER://有socket连接到服务器刷新界面
				infoAdapter.upDataUI(myServerSocketController.getClientList());
			case ConstantValue.SERVER_OPEN_AP:
				Log.i("1", "开始开启socket");
				myServerSocketController.startAcceptSocket();
				break;
			case ConstantValue.CLIENT_END:
				MyClientSocketBean bean = (MyClientSocketBean)msg.obj;
				Toast.makeText(ServerActivity.this, "客户端:"+bean.getName()+"关闭", 1000).show();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		wifiConnectUtil = WifiConnectUtil.getInstance(this);
		myServerSocketController = new MyServerSocketController(handler);
		fm = getFragmentManager(); 
		infoAdapter = new MyServerClientInfoListAdapter(this,myServerSocketController.getClientList());
		adapter = new MyDrawListAdapter(this);
		transaction =  fm.beginTransaction(); 
		clientInfoList = (ListView) findViewById(R.id.lv_client_infos);
		drawerList= (ListView) findViewById(R.id.left_drawer);
		openSocketUtils = new OpenSocketUtils(this,handler, null, myServerSocketController);
		openSocketUtils.openServerSocket();
		drawerList.setAdapter(adapter);
		clientInfoList.setAdapter(infoAdapter);
		selectFragment(3);
	}
	public void selectFragment(int position){
		// 开启Fragment事务  
		String clazzName = adapter.itemClass[position];

		try {
			Class targetClazz = Class.forName(clazzName);
			Constructor constructor = targetClazz.getConstructor(Context.class,MyServerSocketController.class);
			Fragment targetFragment = (Fragment) constructor.newInstance(ServerActivity.this,myServerSocketController);
			transaction.replace(R.id.content_frame, targetFragment);
			transaction.commit();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void end(){
		EndControl.sendToClient(myServerSocketController);
		wifiConnectUtil.setWifiApEnabled(null, null, false);
		myServerSocketController.end();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		end();
		super.onBackPressed();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		end();
		super.onDestroy();
	}
}
