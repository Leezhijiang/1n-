package com.example.onenprofessor.minterface;

import java.io.InputStream;

public interface OnTranSocket {
	/**
	 * 在读取Data时回调
	 * @param is
	 */
	public void onServerDataGet(InputStream is);
	public void onClientDataGet(InputStream is);
	
}
