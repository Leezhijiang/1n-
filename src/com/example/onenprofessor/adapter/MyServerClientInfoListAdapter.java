package com.example.onenprofessor.adapter;

import java.util.List;

import com.example.onenprofessor.R;
import com.example.onenprofessor.bean.MyClientSocketBean;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyServerClientInfoListAdapter extends BaseAdapter{
	private Context context;
	private List<MyClientSocketBean> clientInfos;
	public MyServerClientInfoListAdapter(Context context,List<MyClientSocketBean> clientInfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.clientInfos = clientInfos;
	}
	public void upDataUI(List<MyClientSocketBean> clientInfos){
		this.clientInfos = clientInfos;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clientInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return clientInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyClientSocketBean bean = clientInfos.get(position);
		View v = View.inflate(context, R.layout.lv_item_ip, null);
		TextView name = (TextView) v.findViewById(R.id.tv_name);
		TextView ip = (TextView) v.findViewById(R.id.tv_ip);
		TextView desc = (TextView) v.findViewById(R.id.tv_desc);
		ip.setText(bean.getIp());
		name.setText(bean.getName());
		desc.setText(bean.getDesc());
		return v;
	}

}
