package com.example.onenprofessor;

import android.R.integer;

public class ConstantValue {
	/**
	 * 客户端ip地址
	 */
	public final static String SERVER_IP = "192.168.43.1";
	/**
	 * 客户端端口
	 */
	public final static String SERVER_PORT = "5053";
	/**
	 * handler提醒更新服务器界面
	 */
	public final static int UPDTATE_SERVER = 1;
	/**
	 * handler发送前
	 */
	public final static int PRE_SEND = 2;
	/**
	 * handler发送后
	 */
	public final static int AFTER_SEND = 3;
	/**
	 * handler发送中
	 */
	public final static int ON_SEND = 4;
	/**
	 * handler读取后
	 */
	public final static int AFTER_READ = 5;
	/**
	 * handler读取前
	 */
	public final static int PRE_READ = 6;
	/**
	 * handler读取中
	 */
	public final static int ON_READ = 7;
	/**
	 * 热点密码
	 */
	public final static String pw = "wifishared1111";
	/**
	 * 热点前面的ssid
	 */
	public final static String SSID = "WifiShared";
	/**
	 * handler连接服务器热点
	 */
	public final static int ON_CONN = 8;
	/**
	 * handler连接服务器热点shibai
	 */
	public final static int BAD_CONN = 9;
	/**
	 * 保存文件夹
	 */
	public final static String DIR_NAME = "WifiSharedDownLoad";
	/**
	 * 正在读取
	 */
	public final static String SOCKET_ON_READ = "*/onread/*";
	/**
	 * 接受读取
	 */
	public final static String SOCKET_PRE_READ = "*/preread/*";
	/**
	 * 读取完毕
	 */
	public final static String SOCKET_AFTER_READ = "*/afterread/*";
	/**
	 * 断开连接
	 */
	public final static String SOCKET_CLIENT_BREAK_DOWN = "*/breakdown/*";/**
	 * handler客户端断开连接
	 */
	public final static int CLIENT_BREAK_DOWN = 10;
	/**
	 * handler客户端断开连接
	 */
	public final static int IS_OS_CACHE = 1024;
	/**
	 * 创建data socket失败
	 */
	public final static int CREATE_DATA_SOCKET_FAILED = 11;
	/**
	 * 发送data数据失败
	 */
	public final static int SNED_DATA_FAILED = 12;
	/**
	 * handler客户端断开连接
	 */
	public final static int CLIENT_CONN_WIFI = 13;
	/**
	 * handler服务器开启热点
	 */
	public final static int SERVER_OPEN_AP = 14;
	/**
	 * 服务器得到Msg
	 */
	public final static int SERVER_GET_MSG = 15;
	/**
	 * 刷新客户端界面
	 */
	public final static int UPDATA_CLIENT = 16;
	/**
	 * 刷新客户端drawer
	 */
	public final static int UPDATA_CLIENT_DRAWER = 17;
	/**
	 *  连接到服务器socket
	 */
	public final static int CONN_SERVER_SOCKET = 18;
	/**
	 *  选取文件完成发送文件
	 */
	public final static int SEND_FILE = 19;
	/**
	 *  客户端关闭
	 */
	public final static int CLIENT_END = 20;
	/**
	 *  服务器关闭
	 */
	public final static int SERVER_END = 21;
}
