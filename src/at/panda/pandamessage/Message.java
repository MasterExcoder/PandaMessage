package at.panda.pandamessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class Message {
	
	InetAddress ip;
	int port;
	DatagramSocket socket;
	DatagramPacket packet;
	String content;
	
	
	
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public DatagramSocket getSocket() {
		return socket;
	}
	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}
	public DatagramPacket getPacket() {
		return packet;
	}
	public void setPacket(DatagramPacket packet) {
		this.packet = packet;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	

}
