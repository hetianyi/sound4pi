package com.hty.sound4pi.bean;

public class PlayRequest {
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 播放类型
	 * 1：播放给定的字节流
	 * 2：播放本机指定位置的mp3文件
	 */
	private String type;
	/**
	 * type=1时为16进制字符串，需转换为字节
	 * type=2时为mp3路径字符串
	 */
	private String data;
	/**
	 * 是否合并任务，当列队有当前app的播放任务时是否合并为最新的
	 */
	private boolean join = false;
	/**
	 * 是否等待前面的播放队列播放完成再播放
	 * wait=false则开启新线程立即播放音乐
	 */
	private boolean wait = true;
	/**
	 * 命令：pause，start，操作正在播放的任务
	 */
	private String cmd;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public boolean isJoin() {
		return join;
	}
	public void setJoin(boolean join) {
		this.join = join;
	}
	public boolean isWait() {
		return wait;
	}
	public void setWait(boolean wait) {
		this.wait = wait;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
}
