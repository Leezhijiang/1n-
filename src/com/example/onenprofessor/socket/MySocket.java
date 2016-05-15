package com.example.onenprofessor.socket;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public abstract class MySocket {
	/**
	 * ָ��ͻ������
	 */
	//protected List<String> mMsgList = Collections.synchronizedList(new ArrayList<String>());
	/**
	 * ����Msg����һ��
	 * @return �Ƿ�ɹ�
	 */
	protected abstract boolean sendMsg(JSONObject json);
	/**
	 * ����һ���ĵ�Msg,��������ȡcontrol��
	 */
	protected abstract JSONObject getMsg();
	/**
	 * �������ݵ���һ��
	 *@return �Ƿ�ɹ�
	 */
	protected abstract boolean sendData(InputStream is);
	/**
	 * ����һ���ĵ�Msg,��������ȡcontrol������Ӧ����
	 */
	protected abstract InputStream getData();
	/**
	 * �����������ͷ�socket������Դ
	 */
	protected abstract void end();
}
