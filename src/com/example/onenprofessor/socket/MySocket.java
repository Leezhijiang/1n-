package com.example.onenprofessor.socket;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public abstract class MySocket {
	/**
	 * 指令发送缓存队列
	 */
	//protected List<String> mMsgList = Collections.synchronizedList(new ArrayList<String>());
	/**
	 * 发送Msg到另一方
	 * @return 是否成功
	 */
	protected abstract boolean sendMsg(JSONObject json);
	/**
	 * 从另一方的到Msg,并解析调取control类
	 */
	protected abstract JSONObject getMsg();
	/**
	 * 发送数据到另一放
	 *@return 是否成功
	 */
	protected abstract boolean sendData(InputStream is);
	/**
	 * 从另一方的到Msg,并解析调取control类做相应操作
	 */
	protected abstract InputStream getData();
	/**
	 * 结束方法，释放socket和流资源
	 */
	protected abstract void end();
}
