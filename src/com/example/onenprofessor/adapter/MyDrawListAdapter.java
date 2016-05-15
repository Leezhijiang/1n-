package com.example.onenprofessor.adapter;

import com.example.onenprofessor.R;

import android.R.color;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyDrawListAdapter extends BaseAdapter{
	Context context;
	private String[] itemName = new String[]{"������","��ȡ�ļ��б�","������Ϣ","�����ļ�","�����豸","������Ļ","shell(ROOT)"};
	public String[] itemClass = new String[]{"","","","com.example.onenprofessor.fragment.ServerWifiSharedFragment","","",""};
	public MyDrawListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
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
		TextView tv = (TextView) View.inflate(context,R.layout.drawer_list_item, null);
		tv.setText(itemName[position]);
		if(position == 0){
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			tv.setEnabled(false);
		}else {
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
		}
		return tv;
	}

}
