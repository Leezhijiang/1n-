package com.example.onenprofessor.minterface;

import java.io.InputStream;

public interface OnTranSocket {
	/**
	 * �ڶ�ȡDataʱ�ص�
	 * @param is
	 */
	public void onServerDataGet(InputStream is);
	public void onClientDataGet(InputStream is);
	
}
