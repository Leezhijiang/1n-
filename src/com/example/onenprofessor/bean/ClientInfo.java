package com.example.onenprofessor.bean;

public class ClientInfo {
	private String name;
	private String ip;
	private String desc = "准备就绪";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void init(){
		desc = "准备就绪";
	}
}
