package com.example.onenprofessor.dialog;

import com.example.onenprofessor.ConstantValue;
import com.example.onenprofessor.R;
import com.example.onenprofessor.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SetNameDialog {
	private static AlertDialog dialog;
	public static void showSetNameDialog(boolean focusOpen,final Context context,final SharedPreferences sp,final Handler handler){
		if(sp.getString("clientname", "").equals("")||focusOpen){
			View v;
			Button ok;
			Button cancel;
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			v = View.inflate(context,R.layout.dialog_set_name, null);

			ok = (Button) v.findViewById(R.id.bt_set_name_ok);
			cancel = (Button) v.findViewById(R.id.bt_set_name_cancel);
			final EditText name = (EditText) v.findViewById(R.id.e_set_name_name);
			//确定开启热点
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 根据用户输入姓名存储设置
					String mName = name.getText().toString();
					if(StringUtils.isEmpty(mName)){
						Toast.makeText(context, "姓名不能为空", 1000).show();
						return;
					}
					Editor editor= sp.edit();
					editor.putString("clientname", mName);
					editor.commit();
					dialog.dismiss();
					handler.sendEmptyMessage(ConstantValue.UPDATA_CLIENT_DRAWER);
				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					((Activity) context).finish();

				}
			});
			dialog = builder.setView(v).show();
		}
	}
}
