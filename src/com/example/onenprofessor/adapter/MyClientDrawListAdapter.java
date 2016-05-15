package com.example.onenprofessor.adapter;

import com.example.onenprofessor.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyClientDrawListAdapter extends BaseAdapter{
	Context context;
	public String[] itemName = new String[]{"用户名","读取文件列表","推送消息","传送文件","控制设备","传输屏幕","shell(ROOT)"};
	public String[] itemClass = new String[]{"","","","com.example.onenprofessor.fragment.ClientWifiSharedFragment","","",""};
	public MyClientDrawListAdapter(Context context,SharedPreferences sp) {
		// TODO Auto-generated constructor stub
		this.context = context;
		itemName[0]=sp.getString("clientname", "用户名");
	}
	public void notifyDataSetChanged(SharedPreferences sp) {
		// TODO Auto-generated method stub
		itemName[0]=sp.getString("clientname", "用户名");
		super.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemName.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemName[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) View.inflate(context,R.layout.drawer_list_item, null).findViewById(R.id.tv_drawer_list_name);
		tv.setText(itemName[position]);
		if(position == 0){
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
		}else {
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
		}
		return tv;
	}

}
