package com.example.onenprofessor.activity;

import java.lang.reflect.Constructor;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.R;
import com.example.onenprofessor.adapter.MyClientDrawListAdapter;
import com.example.onenprofessor.adapter.MyServerClientInfoListAdapter;
import com.example.onenprofessor.control.ClientSetNameControl;
import com.example.onenprofessor.control.EndControl;
import com.example.onenprofessor.dialog.SetNameDialog;
import com.example.onenprofessor.socket.MyClientSocketController;
import com.example.onenprofessor.utils.OpenSocketUtils;
import com.example.onenprofessor.utils.StringUtils;
import com.example.onenprofessor.utils.WifiConnectUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClientActivity extends Activity implements OnItemClickListener {
	private ListView drawerList;
	private SharedPreferences sp;
	private FragmentTransaction transaction ;
	private WifiConnectUtil wifiConnectUtil;
	private MyClientSocketController myClientSocketController;
	private OpenSocketUtils openSocketUtils;
	private FragmentManager fm;
	private MyClientDrawListAdapter myClientDrawListAdapter;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ConstantValue.UPDATA_CLIENT_DRAWER:
				//更新用户名，并且将新用户名发送到服务器
				myClientDrawListAdapter.notifyDataSetChanged(sp);
				String name = sp.getString("clientname", "未设置");
				ClientSetNameControl.sendNameToServer(myClientSocketController,name);
				break;
			case ConstantValue.CONN_SERVER_SOCKET:
				Log.i("1", "CONN_SERVER_SOCKET");
				handler.sendEmptyMessage(ConstantValue.UPDATA_CLIENT_DRAWER);
				break;
			case ConstantValue.CLIENT_CONN_WIFI:
				myClientSocketController.startSocketConn(); 
				break;
			case ConstantValue.SERVER_END:
				Toast.makeText(ClientActivity.this, "服务器关闭", 1000).show();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		fm = getFragmentManager(); 
		transaction =  fm.beginTransaction(); 
		wifiConnectUtil = WifiConnectUtil.getInstance(this);
		myClientSocketController = new MyClientSocketController(handler);
		openSocketUtils = new OpenSocketUtils(this,handler, myClientSocketController, null);
		sp = getSharedPreferences("onen.config", MODE_PRIVATE);
		drawerList = (ListView) findViewById(R.id.left_client_drawer);
		myClientDrawListAdapter = new MyClientDrawListAdapter(this,sp);
		drawerList.setAdapter(myClientDrawListAdapter);
		SetNameDialog.showSetNameDialog(false,this, sp,handler);
		drawerList.setOnItemClickListener(this);
		openSocketUtils.openClientSocket();
		selectFragment(3);
	}
	/**
	 * drawlist点击回调函数
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position==0){
			//点击用户名
			SetNameDialog.showSetNameDialog(true,this, sp,handler);
		}
	}
	public void selectFragment(int position){
		// 开启Fragment事务  
		String clazzName = myClientDrawListAdapter.itemClass[position];

		try {
			Class targetClazz = Class.forName(clazzName);
			Constructor constructor = targetClazz.getConstructor(Context.class);
			Fragment targetFragment = (Fragment) constructor.newInstance(ClientActivity.this);
			transaction.replace(R.id.content_client_frame, targetFragment);
			transaction.commit();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void end(){
		EndControl.sendToServer(myClientSocketController);
		wifiConnectUtil.setWifiEnabled(false);
		myClientSocketController.end();
	
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
