package com.example.onenprofessor.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.R;
import com.example.onenprofessor.R.id;
import com.example.onenprofessor.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseModeActivity extends Activity {
	private Button btServer;
	private Button btClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_mode);
		createDir();
		btServer = (Button) findViewById(R.id.bt_beServer);
		btClient = (Button) findViewById(R.id.bt_beClient);
		//�����Ϊ���Ͷ˰�ť
		btServer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO ת�����Ͷ�activity
				Intent intent = new Intent(ChooseModeActivity.this,ServerActivity.class);
				startActivity(intent);
			}
		});
		//�����Ϊ���ն˰�ť
		btClient.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO ת�����ն�activity
				Intent intent = new Intent(ChooseModeActivity.this,ClientActivity.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * �������ڴ洢���ļ���
	 */
	private void createDir() {
		// TODO Auto-generated method stub
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ConstantValue.DIR_NAME;
		  File destDir = new File(path);
		  Log.i("1", path);
		  if (!destDir.exists()) {
			  destDir.mkdirs();
		   Log.i("1", "�½��ɹ�");
		  }
	}

}
